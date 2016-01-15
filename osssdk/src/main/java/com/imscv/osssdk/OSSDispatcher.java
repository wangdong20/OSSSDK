package com.imscv.osssdk;

/**
 * Created by David Wong on 2016/1/13.
 */
public interface OSSDispatcher {
    /**
     * Perform the RequestWrapper
     * @param wrapper The RequestWrapper will be performed
     */
    public void performRequest(RequestWrapper wrapper);
}
