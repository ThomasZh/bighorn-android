package net.younguard.cchess.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class BaseActivity extends Activity {
	BroadcastReceiver mReceiver =null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				ConsultReceive(context,intent);
			}
		};
	}

	public void ConsultReceive(Context context, Intent intent) {

	}
	public void initAction(String action){
		IntentFilter mFilter = new IntentFilter(); // ´úÂë×¢²á¹ã²¥
		mFilter.addAction(action);
		registerReceiver(mReceiver, mFilter);

		
	}
}
