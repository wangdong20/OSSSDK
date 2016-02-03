package com.imscv.osssdk;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by David Wong on 2016/1/13.
 */
public class BaseOssDispatcher implements OSSDispatcher {

    private Context mContext;

    private OSSClient oss;

    private static final String TAG = BaseOssDispatcher.class.getSimpleName();

    public static final String END_POINT = "oss-cn-shenzhen.aliyuncs.com";
    private String CREDENTIALPROVIDER_URL = "http://trobot.imscv.com:82/api/getSTS?data={\"connectType\":id,\"param\":\"sn\"}";

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
                        String urlId = CREDENTIALPROVIDER_URL.replaceAll("id", "3");
                        String urlLast = urlId.replaceAll("sn", SNUtils.getSN(mContext));
                        URL stsUrl = new URL(urlLast);
                        HttpURLConnection conn = (HttpURLConnection) stsUrl.openConnection();
                        conn.setRequestMethod("POST");
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
                putSample.putObjectFromLocalFile();
                break;
            case RequestWrapper.RequestType.MULTIPART:
                MultipartUploadSamples multiSample = new MultipartUploadSamples(oss, wrapper);
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
                resuambleSample.resumableUploadWithRecordPathSetting();
                break;
            case RequestWrapper.RequestType.DELETE:
                ManageObjectSamples manageObjectSamples = new ManageObjectSamples(oss, wrapper);
                manageObjectSamples.asyncDeleteObject();
                break;
            case RequestWrapper.RequestType.GET:
                GetObjectSamples getSample = new GetObjectSamples(oss, wrapper);
                getSample.asyncGetObjectSample();
                break;
        }
    }

}
