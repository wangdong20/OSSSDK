package com.imscv.osssdkexample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.imscv.osssdk.OnSuccessListener;
import com.imscv.osssdk.RequestWrapper;
import com.imscv.osssdk.RequestWrapperQueue;
import com.imscv.osssdk.UploadFileInfoInServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTestUploadBtn;
    private TextView mTestDownloadBtn;

    public static final String BUCKET = "service-robot";

    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+
            "/DCIM/Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestUploadBtn = (TextView) findViewById(R.id.test_upload_btn);
        mTestUploadBtn.setOnClickListener(this);

        mTestDownloadBtn = (TextView) findViewById(R.id.test_download_btn);
        mTestDownloadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_upload_btn:
                testUpload();
                break;
            case R.id.test_download_btn:
                testDownload();
                break;
        }
    }

    private String generatePathInServer(String name) {
        Calendar now = Calendar.getInstance();
        return "robot/" + SNUtils.getSN(this) + "/" + now.get(Calendar.YEAR) +
                "/" + (now.get(Calendar.MONTH) + 1) + "/" + name;
    }

    private void testDownload() {
        RequestWrapperQueue queue = RequestWrapperQueue.getInstance(this);
        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
                BUCKET, generatePathInServer("20160108115205.jpg"),
                PHOTO_PATH + "/" + "20160108115205.jpg", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已下载20160108115205.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
                BUCKET, generatePathInServer("20160108182657.jpg"),
                PHOTO_PATH + "/" + "20160108182657.jpg", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已下载20160108182657.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
                BUCKET, generatePathInServer("20160108182702.jpg"),
                PHOTO_PATH + "/" + "20160108182702.jpg", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已下载20160108182702.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
                BUCKET, generatePathInServer("VID_20160112_181714.mp4"),
                PHOTO_PATH + "/" + "VID_20160112_181714.mp4", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已下载VID_20160112_181714.mp4", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
    }

    private void testUpload() {
        RequestWrapperQueue queue = RequestWrapperQueue.getInstance(this);
        queue.add(new RequestWrapper(RequestWrapper.RequestType.PUT,
                BUCKET, generatePathInServer("20160108115205.jpg"),
                PHOTO_PATH + "/" + "20160108115205.jpg", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已上传20160108115205.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.PUT,
                BUCKET, generatePathInServer("20160108182657.jpg"),
                PHOTO_PATH + "/" + "20160108182657.jpg", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已上传20160108182657.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.PUT,
                BUCKET, generatePathInServer("20160108182702.jpg"),
                PHOTO_PATH + "/" + "20160108182702.jpg", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已上传20160108182702.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.RESUAMBLE,
                BUCKET, generatePathInServer("VID_20160112_181714.mp4"),
                PHOTO_PATH + "/" + "VID_20160112_181714.mp4", new OnSuccessListener() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已上传VID_20160112_181714.mp4", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
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
                            Toast.makeText(MainActivity.this, "code is " + code + obj.toString(), Toast.LENGTH_SHORT).show();
                            if(code.equals("N00000")) {
                                Toast.makeText(MainActivity.this, obj.getString("Message"), Toast.LENGTH_SHORT).show();
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

        Volley.newRequestQueue(this).add(request);
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
