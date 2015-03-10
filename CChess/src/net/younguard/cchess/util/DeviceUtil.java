package net.younguard.cchess.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.UUID;

/**
 * Created by liwenzhi on 14-9-24.
 */
public class DeviceUtil {

    /**
     * get device id
     *
     * @param ctx
     * @return
     */
    public static String getUniqueDeviceId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        String id = tm.getDeviceId();
        if (id != null) {
            return UUID.nameUUIDFromBytes(id.getBytes()).toString();
        }

        return null;
    }

    public static String getRawDeviceId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

        return tm.getDeviceId();
    }

    public static String getDeviceOS() {
        return android.os.Build.MODEL+","+ Build.VERSION.CODENAME+","+Build.VERSION.RELEASE;
    }


    public static String getDeviceManufacture() {
        return android.os.Build.BRAND;
    }
    
    public static int dip2px(Context context, float dipValue){ 
        float density = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue*density + 0.5f);
    }

}
