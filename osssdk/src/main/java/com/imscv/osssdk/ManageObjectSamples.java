package com.imscv.osssdk;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CopyObjectRequest;
import com.alibaba.sdk.android.oss.model.CopyObjectResult;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;

/**
 * Created by David Wong on 2016/2/1.
 */
public class ManageObjectSamples extends OssSamples {

//    public ManageObjectSamples(OSS client, String testBucket, String testObject) {
//        this.oss = client;
//        this.testBucket = testBucket;
//        this.testObject = testObject;
//    }

    /**
     * @param client
     * @param wrapper The RequestWrapper's uploadFilePath can be null.
     */
    public ManageObjectSamples(OSS client, RequestWrapper wrapper) {
        super(client, wrapper);
    }

    // 检查文件是否存在
    public void checkIsObjectExist() {
        try {
            if (oss.doesObjectExist(testBucket, testObject)) {
                Log.d("doesObjectExist", "object exist.");
            } else {
                Log.d("doesObjectExist", "object does not exist.");
            }
        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    // 只获取一个文件的元信息
    public void headObject() {
        // 创建同步获取文件元信息请求
        HeadObjectRequest head = new HeadObjectRequest(testBucket, testObject);

        OSSAsyncTask task = oss.asyncHeadObject(head, new OSSCompletedCallback<HeadObjectRequest, HeadObjectResult>() {
            @Override
            public void onSuccess(HeadObjectRequest request, HeadObjectResult result) {
                Log.d("headObject", "object Size: " + result.getMetadata().getContentLength());
                Log.d("headObject", "object Content Type: " + result.getMetadata().getContentType());
                if(responseListener != null) {
                    responseListener.onSuccess(wrapper);
                }
            }

            @Override
            public void onFailure(HeadObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if(responseListener != null) {
                        responseListener.onFail(clientExcepion.getMessage());
                    }
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    if(responseListener != null) {
                        responseListener.onFail(serviceException.getRawMessage());
                    }
                }
            }
        });

        task.waitUntilFinished();
    }

    // 复制object到一个新的object，再把它copy出来的object删除，调用同步接口
    public void CopyAndDeleteObject() {
        // 创建copy请求
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(testBucket, testObject,
                testBucket, "testCopy");
        // 设置copy文件新的元信息(Content Type)
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/octet-stream");
        copyObjectRequest.setNewObjectMetadata(objectMetadata);

        try {
            // 发送同步copy请求
            CopyObjectResult copyResult = oss.copyObject(copyObjectRequest);
            // 获取copy文件的元信息
            HeadObjectRequest head = new HeadObjectRequest(testBucket, "testCopy");
            HeadObjectResult result = oss.headObject(head);

            // 同步删除该copy文件
            DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, "testCopy");
            DeleteObjectResult deleteResult = oss.deleteObject(delete);
            if (deleteResult.getStatusCode() == 204) {
                Log.d("CopyAndDeleteObject", "Success.");
            }
            if(responseListener != null) {
                responseListener.onSuccess(wrapper);
            }
        }
        // 本地异常
        catch (ClientException e) {
            e.printStackTrace();
        }
        // 服务异常
        catch (ServiceException e) {
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
            if(responseListener != null) {
                responseListener.onFail(e.getRawMessage());
            }
        }

    }

    // 复制object到一个新的object，再把它copy出来的object删除，调用异步接口
    public void asyncCopyAndDeleteObject() {
        // 创建copy请求
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(testBucket, testObject,
                testBucket, "testCopy");

        //设置copy文件新元信息（Content Type）
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/octet-stream");
        copyObjectRequest.setNewObjectMetadata(objectMetadata);

        // 异步copy
        OSSAsyncTask copyTask = oss.asyncCopyObject(copyObjectRequest, new OSSCompletedCallback<CopyObjectRequest, CopyObjectResult>() {
            @Override
            public void onSuccess(CopyObjectRequest request, CopyObjectResult result) {
                Log.d("copyObject", "copy success!");

            }

            @Override
            public void onFailure(CopyObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        // 等待直到操作完成
        copyTask.waitUntilFinished();

        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, "testCopy");
        // 异步删除
        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("asyncCopyAndDelObject", "success!");
                if(responseListener != null) {
                    responseListener.onSuccess(wrapper);
                }
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    if(responseListener != null) {
                        responseListener.onFail(serviceException.getRawMessage());
                    }
                }
            }

        });
        deleteTask.waitUntilFinished();
    }

    public void deleteObject() {
        // 同步删除该文件
        try {
            DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, "testObject");
            DeleteObjectResult deleteResult = oss.deleteObject(delete);
            if (deleteResult.getStatusCode() == 204) {
                Log.d("CopyAndDeleteObject", "Success.");
                if(responseListener != null) {
                    responseListener.onSuccess(wrapper);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(responseListener != null) {
                responseListener.onFail(e.getMessage());
            }
        }
    }

    public void asyncDeleteObject() {
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(testBucket, "testObject");
        // 异步删除
        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                Log.d("asyncCopyAndDelObject", "success!");
                if(responseListener != null) {
                    responseListener.onSuccess(wrapper);
                }
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    if(responseListener != null) {
                        responseListener.onFail(serviceException.getRawMessage());
                    }
                }
            }

        });
        deleteTask.waitUntilFinished();
    }
}
