package com.imscv.osssdk;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by David Wong on 2016/1/11.
 */
public class SNUtils {
    public static String getSN(Context context) {
        final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (deviceId != null) {
            return deviceId;
        } else {
            return android.os.Build.SERIAL;
        }
    }
}
