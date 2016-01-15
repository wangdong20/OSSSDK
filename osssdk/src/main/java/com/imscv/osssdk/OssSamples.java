package com.imscv.osssdk;

import com.alibaba.sdk.android.oss.OSS;

/**
 * Created by David Wong on 2016/1/14.
 */
public class OssSamples {
    protected OSS oss;
    protected String testBucket;
    protected String testObject;
    protected String uploadFilePath;
    protected OnSuccessListener successListener;

    public OssSamples(OSS oss, String testBucket, String testObject, String uploadFilePath) {
        this.oss = oss;
        this.testBucket = testBucket;
        this.testObject = testObject;
        this.uploadFilePath = uploadFilePath;
    }

    public OssSamples(OSS client, RequestWrapper wrapper) {
        this.oss = client;
        this.testBucket = wrapper.getTestBucket();
        this.testObject = wrapper.getTestObject();
        this.uploadFilePath = wrapper.getUploadFilePath();
    }

    public void setOnSuccessListener(OnSuccessListener listener) {
        successListener = listener;
    }

}
