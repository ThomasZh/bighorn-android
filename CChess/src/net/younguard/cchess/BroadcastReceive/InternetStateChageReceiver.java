package net.younguard.cchess.BroadcastReceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetStateChageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			Log.d("mark", "网络状态已经改变");
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {

				String name = info.getTypeName();
				Log.i("cc", "当前网络名称：" + name);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

					}
				}).start();

			}
		}
	}
}
