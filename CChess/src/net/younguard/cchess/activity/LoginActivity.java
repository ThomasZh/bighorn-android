package net.younguard.cchess.activity;



import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.account.cmd.DeviceLoginReq;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.cchess.isApplication;
import net.younguard.cchess.util.DeviceUtil;

import com.example.cchess.R;
import com.younguard.CChess.config.AppConfig;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	
    private EditText etUserName;
    private EditText etPass;
    private Button btnRegister;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		initAction("com.cc.chengcai");
		etUserName = (EditText) findViewById(R.id.et_username);
		etPass = (EditText) findViewById(R.id.et_userpass);
		btnRegister = (Button) findViewById(R.id.bt_register);
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DeviceLoginReq  reqCmd = new DeviceLoginReq();
				reqCmd.setDeviceId(DeviceUtil.getRawDeviceId(LoginActivity.this));
				if(AppConfig.account!=null){
					
					reqCmd.setAccountId(AppConfig.account.getAccountId());
				}
				TlvObject msgTlv = null;
				try {
					msgTlv = BroadcastCommandParser.encode(reqCmd);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				isApplication.session.write(msgTlv);
			}
		});
		
		
	}
	@Override
	public void ConsultReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.ConsultReceive(context, intent);
		Toast.makeText(this, "hahahha", 1).show();
	}

	

}
