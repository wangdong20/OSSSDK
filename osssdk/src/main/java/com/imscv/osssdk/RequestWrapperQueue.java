package com.imscv.osssdk;

import android.content.Context;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by David Wong on 2016/1/12.
 */
public class RequestWrapperQueue {
    private BlockingQueue<RequestWrapper> mQueue;
    private RequestDispatcher mRequestDispatcher;
    private static RequestWrapperQueue mInstance;

    private void newQueue(Context context) {
        if(mQueue == null) {
            mQueue = new LinkedBlockingQueue<>();
        }

        if(mRequestDispatcher == null) {
            mRequestDispatcher = new RequestDispatcher(mQueue, new BaseOssDispatcher(context));
        }

        start();
    }

    private RequestWrapperQueue(Context context) {
        newQueue(context);
    }

    public static RequestWrapperQueue getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new RequestWrapperQueue(context);
        }
        return mInstance;
    }

    public void add(RequestWrapper requestWrapper) {
        mQueue.add(requestWrapper);
    }

    public void start() {
        stop();
        mRequestDispatcher.start();
    }

    public void stop() {
        mRequestDispatcher.quit();
    }

    public void cancelAll() {
        stop();
        mQueue.clear();
    }

}
