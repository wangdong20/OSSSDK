package com.imscv.osssdk;

/**
 * Created by David Wong on 2016/1/18.
 */
public interface OnResponseListener {
    public void onSuccess(RequestWrapper wrapper);
    public void onFail(String message);
}
