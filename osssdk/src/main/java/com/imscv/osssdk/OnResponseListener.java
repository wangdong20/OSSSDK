package com.imscv.osssdk;

/**
 * Created by David Wong on 2016/1/18.
 */
public interface OnResponseListener {
    public void onSuccess();
    public void onFail(String message);
}
