package com.imscv.osssdk;

/**
 * Created by David Wong on 2016/1/12.
 */
public class RequestWrapper {
    private int requestType;
    private String testBucket;
    private String testObject;
    private String uploadFilePath;
    private OnResponseListener responseListener;

    /**
     * 0 putObject 1 multipartupload 2 resuambleupload 3 delete object in server
     */
    public interface RequestType {
        int PUT = 0;
        int MULTIPART = 1;
        int RESUAMBLE = 2;
        int DELETE = 3;
        int GET = 4;
    }

    public RequestWrapper(int requestType, String testBucket, String testObject, String uploadFilePath) {
        this.requestType = requestType;
        this.testBucket = testBucket;
        this.testObject = testObject;
        this.uploadFilePath = uploadFilePath;
    }

    public RequestWrapper(int requestType, String testBucket, String testObject, String uploadFilePath, OnResponseListener responseListener) {
        this.requestType = requestType;
        this.testBucket = testBucket;
        this.testObject = testObject;
        this.uploadFilePath = uploadFilePath;
        this.responseListener = responseListener;
    }

    public OnResponseListener getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(OnResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public String getTestBucket() {
        return testBucket;
    }

    public void setTestBucket(String testBucket) {
        this.testBucket = testBucket;
    }

    public String getTestObject() {
        return testObject;
    }

    public void setTestObject(String testObject) {
        this.testObject = testObject;
    }

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }

    @Override
    public boolean equals(Object o) {
        RequestWrapper wrapper = (RequestWrapper)o;
        if(requestType == wrapper.requestType && testObject.equals(wrapper.getTestObject())
                && uploadFilePath.equals(wrapper.getUploadFilePath())) {
            return true;
        } else {
            return false;
        }
    }
}
