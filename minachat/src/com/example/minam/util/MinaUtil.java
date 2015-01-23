package com.example.minam.util;

import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.EditText;

public class MinaUtil {
	
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**�ж��û��Ƿ�����ǳ�
	 * @return
	 */
	public static String getNameFromSD(Activity activity){
		SharedPreferences edit = activity.getPreferences(Context.MODE_PRIVATE);
		String name  = edit.getString("name","");
		return name;
	}
	 public static String getUniqueDeviceId(Context ctx) {
	        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

	        String id = tm.getDeviceId();
	        if (id != null) {
	            return UUID.nameUUIDFromBytes(id.getBytes()).toString();
	        }

	        return null;
	    }
	
	 
		public static void saveRegisterId(Context context, String registerId){
			SharedPreferences settings = context.getSharedPreferences("User", Context.MODE_PRIVATE);
			Editor editor = settings.edit();
			editor.putString("RegisterId", registerId);
			editor.commit();
		}
		
		//获取头像更新的时间
		public static String getRegisterId(Context context){
			SharedPreferences settings = context.getSharedPreferences("User", Context.MODE_PRIVATE);
			String registerId= settings.getString("RegisterId", "");
			
			return registerId;
		}
	

}
