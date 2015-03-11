package net.younguard.cchess;


import net.younguard.cchess.activity.BaseActivity;
import net.younguard.cchess.activity.LoginActivity;
import net.younguard.cchess.util.BaseActivityUtil;

import com.example.cchess.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.test).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseActivityUtil.startActivity(MainActivity.this, LoginActivity.class, false);
				
			}
		});
		
	}
	
	
		

}
