package com.imscv.osssdkexample;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David Wong on 2016/1/27.
 */
public class DataParamStringRequest extends StringRequest {

    private String param;

    public DataParamStringRequest(int method, String url, String param, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.param = param;
    }

    public DataParamStringRequest(String url, String param, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);
        this.param = param;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map map = new HashMap();
        map.put("data", param);
        return map;
    }
}
