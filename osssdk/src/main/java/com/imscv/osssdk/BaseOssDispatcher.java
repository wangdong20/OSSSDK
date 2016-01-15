package com.imscv.osssdk;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David Wong on 2016/1/13.
 */
public class BaseOssDispatcher implements OSSDispatcher {

    private Context mContext;

    private OSSClient oss;

    private static final String TAG = BaseOssDispatcher.class.getSimpleName();

    public static final String END_POINT = "oss-cn-shenzhen.aliyuncs.com";
    public static final String CREDENTIALPROVIDER_URL = "http://trobot.imscv.com:82/api/getSTS";

    public BaseOssDispatcher(Context context) {
        mContext = context;
    }

    @Override
    public void performRequest(final RequestWrapper wrapper) {
        if (oss == null) {
            OSSCredentialProvider credetialProvider = new OSSFederationCredentialProvider() {
                @Override
                public OSSFederationToken getFederationToken() {
                    try {
                        URL stsUrl = new URL(CREDENTIALPROVIDER_URL);
                        HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
                        InputStream input = conn.getInputStream();
                        String jsonText = IOUtils.readStreamAsString(input, OSSConstants.DEFAULT_CHARSET_NAME);
                        JSONObject jsonObjs = new JSONObject(jsonText);
                        String ak = jsonObjs.getString("AccessKeyId");
                        String sk = jsonObjs.getString("AccessKeySecret");
                        String token = jsonObjs.getString("SecurityToken");
                        String expiration = jsonObjs.getString("Expiration");
                        OSSFederationToken federationToken = new OSSFederationToken(ak, sk, token, expiration);
                        Log.d(TAG, federationToken.toString());
                        return federationToken;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            ClientConfiguration conf = new ClientConfiguration();

            conf.setConnectionTimeout(5000);

            conf.setMaxErrorRetry(3);

            oss = new OSSClient(mContext, END_POINT, credetialProvider, conf);
        }
        switch (wrapper.getRequestType()) {
            case RequestWrapper.RequestType.PUT:
                PutObjectSamples putSample = new PutObjectSamples(oss, wrapper);
                putSample.setOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        uploadFileInfoToServer(wrapper);
                    }
                });
                putSample.putObjectFromLocalFile();
                break;
            case RequestWrapper.RequestType.MULTIPART:
                MultipartUploadSamples multiSample = new MultipartUploadSamples(oss, wrapper);
                multiSample.setOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        uploadFileInfoToServer(wrapper);
                    }
                });
                try {
                    multiSample.multipartUpload();
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RequestWrapper.RequestType.RESUAMBLE:
                ResuambleUploadSamples resuambleSample = new ResuambleUploadSamples(oss, wrapper);
                resuambleSample.setOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess() {
                        uploadFileInfoToServer(wrapper);
                    }
                });
                resuambleSample.resumableUploadWithRecordPathSetting();
                break;
            case RequestWrapper.RequestType.DELETE:
                // TODO
                break;
            case RequestWrapper.RequestType.GET:
                GetObjectSamples getSample = new GetObjectSamples(oss, wrapper);
                getSample.asyncGetObjectSample();
                break;
        }
    }

    public void uploadFileInfoToServer(RequestWrapper wrapper) {
        Gson gson = new Gson();
        final String json = gson.toJson(wrapper2Info(wrapper));
        final StringRequest request = new StringRequest(Request.Method.POST, "http://trobot.imscv.com:82/api/upload",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String code = obj.getString("code");
                            Toast.makeText(mContext, "code is " + code + obj.toString(), Toast.LENGTH_SHORT).show();
                            if(code.equals("N00000")) {
                                Toast.makeText(mContext, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap();
                map.put("data", json);
                return map;
            }
        };

        Volley.newRequestQueue(mContext).add(request);
    }

    private UploadFileInfoInServer wrapper2Info(RequestWrapper wrapper) {
        File file = new File(wrapper.getUploadFilePath());
        if(!file.exists()) {
            return null;
        }

        String fileName = file.getName();
        String fileUrl = wrapper.getTestObject();
        long fileSize = file.length();
        String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
        int fileType = fileFormat.equals("jpg") ? 1 : 2;

        return new UploadFileInfoInServer(fileName, fileUrl, fileSize, fileType, fileFormat);
    }
}
