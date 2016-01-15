package com.imscv.osssdk;

import android.os.Process;

import java.util.concurrent.BlockingQueue;

/**
 * Created by David Wong on 2016/1/13.
 */
public class RequestDispatcher extends Thread {

    /** The queue of requests to service. */
    private BlockingQueue<RequestWrapper> mQueue;

    private OSSDispatcher mOssDispatcher;

    /** Used for telling us to die. */
    private volatile boolean mQuit = false;

    public RequestDispatcher(BlockingQueue<RequestWrapper> mQueue, OSSDispatcher dispatcher) {
        this.mQueue = mQueue;
        mOssDispatcher = dispatcher;
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        RequestWrapper request;
        while(true) {
            request = null;
            try {
                // Take a request from the queue.
                request = mQueue.take();
            } catch (InterruptedException e) {
                // We may have been interrupted because it was time to quit.
                if (mQuit) {
                    return;
                }
                continue;
            }
            if(request != null) {
                mOssDispatcher.performRequest(request);
            }
        }
    }

    /**
     * Forces this dispatcher to quit immediately.  If any requests are still in
     * the queue, they are not guaranteed to be processed.
     */
    public void quit() {
        mQuit = true;
        interrupt();
    }
}
