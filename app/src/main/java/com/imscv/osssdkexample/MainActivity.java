package com.imscv.osssdkexample;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.imscv.osssdk.OnResponseListener;
import com.imscv.osssdk.RequestWrapper;
import com.imscv.osssdk.RequestWrapperQueue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTestUploadBtn;
    private TextView mTestDownloadBtn;
    private TextView mTestDeleteBtn;

    public static final String BUCKET = "your-bucket";

    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+
            "/DCIM/Camera";

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTestUploadBtn = (TextView) findViewById(R.id.test_upload_btn);
        mTestUploadBtn.setOnClickListener(this);

        mTestDownloadBtn = (TextView) findViewById(R.id.test_download_btn);
        mTestDownloadBtn.setOnClickListener(this);

        mTestDeleteBtn = (TextView) findViewById(R.id.test_delete_btn);
        mTestDeleteBtn.setOnClickListener(this);

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
        return "oss/" + name;
    }

    private void testDownload() {
        RequestWrapperQueue queue = RequestWrapperQueue.getInstance(this);
        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
                BUCKET, generatePathInServer("20160108115205.jpg"),
                PHOTO_PATH + "/" + "20160108115205.jpg", new OnResponseListener() {
            @Override
            public void onSuccess(final RequestWrapper wrapper) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已下载20160108115205.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
                BUCKET, generatePathInServer("20160122135716.jpg"),
                PHOTO_PATH + "/" + "20160122135716.jpg", new OnResponseListener() {
            @Override
            public void onSuccess(final RequestWrapper wrapper) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已下载20160122135716.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
                BUCKET, generatePathInServer("20160122135721.jpg"),
                PHOTO_PATH + "/" + "20160122135721.jpg", new OnResponseListener() {
            @Override
            public void onSuccess(final RequestWrapper wrapper) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已下载20160122135721.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
//        queue.add(new RequestWrapper(RequestWrapper.RequestType.GET,
//                BUCKET, generatePathInServer("VID_20160112_181714.mp4"),
//                PHOTO_PATH + "/" + "VID_20160112_181714.mp4", new OnResponseListener() {
//            @Override
//            public void onSuccess() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "已下载VID_20160112_181714.mp4", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onFail(final String message) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }));
    }

    private void testUpload() {
        RequestWrapperQueue queue = RequestWrapperQueue.getInstance(this);
        queue.add(new RequestWrapper(RequestWrapper.RequestType.PUT,
                BUCKET, generatePathInServer("20160108115205.jpg"),
                PHOTO_PATH + "/" + "20160108115205.jpg", new OnResponseListener() {
            @Override
            public void onSuccess(final RequestWrapper wrapper) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已上传20160108115205.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.PUT,
                BUCKET, generatePathInServer("20160122135716.jpg"),
                PHOTO_PATH + "/" + "20160122135716.jpg", new OnResponseListener() {
            @Override
            public void onSuccess(final RequestWrapper wrapper) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已上传20160122135716.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
        queue.add(new RequestWrapper(RequestWrapper.RequestType.PUT,
                BUCKET, generatePathInServer("20160122135721.jpg"),
                PHOTO_PATH + "/" + "20160122135721.jpg", new OnResponseListener() {
            @Override
            public void onSuccess(final RequestWrapper wrapper) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "已上传20160122135721.jpg", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFail(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }));
//        queue.add(new RequestWrapper(RequestWrapper.RequestType.RESUAMBLE,
//                BUCKET, generatePathInServer("VID_20160112_181714.mp4"),
//                PHOTO_PATH + "/" + "VID_20160112_181714.mp4", new OnResponseListener() {
//            @Override
//            public void onSuccess() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "已上传VID_20160112_181714.mp4", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onFail(final String message) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }));
    }

}
