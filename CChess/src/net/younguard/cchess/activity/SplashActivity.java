package net.younguard.cchess.activity;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.account.cmd.RegisterDeviceNotifyTokenReq;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.cchess.MainActivity;
import net.younguard.cchess.isApplication;
import net.younguard.cchess.Handler.ClientHandler;
import net.younguard.cchess.util.BaseActivityUtil;
import net.younguard.cchess.util.DeviceUtil;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.cchess.R;

public class SplashActivity extends Activity {
	private IoSession session;
	private SocketConnector connector;
	private IoFilter filter;
	private SocketAddress soketAddress;
	private ConnectFuture future;
	private ClientHandler ch;
	private TextView tvShwoInit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aplash);
		tvShwoInit = (TextView) findViewById(R.id.mShowInfoTv);
		IntentFilter mFilter = new IntentFilter(); // 代码注册广播
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);

	}

	private BroadcastReceiver mReceiver = /**
	 * @author Administrator 监听网络状况发生变化
	 */
	new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.d("mark", "网络状态已经改变");
				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {

					String name = info.getTypeName();
					Log.i("cc", "当前网络名称：" + name);
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (initCon()) {
								Log.i("cc", "server is connected.....");
								session = future.getSession();
								isApplication.session= session;
								RegisterDeviceNotifyToken();
								BaseActivityUtil.startActivity(SplashActivity.this, MainActivity.class,true);
								
							
							} else {
								Log.i("cc", "server isnot connected.....");
								tvShwoInit.setText("连接失败！");
							}
						}
					}).start();

				}
			}
		}
	};

	/**
	 * 与server端建立连接，并返回连接结果
	 * 
	 */
	public Boolean initCon() {

		// TODO Auto-generated method stub
		connector = new NioSocketConnector();
		filter = new ProtocolCodecFilter(new TlvPackageCodecFactory());
		connector.getFilterChain().addLast("vestigge", filter);
		soketAddress = new InetSocketAddress("54.186.197.254", 13103); 
		connector.setHandler(new ClientHandler(this));
		future = connector.connect(soketAddress);
		future.join();
		return future.isConnected();

	}
	public void  RegisterDeviceNotifyToken(){
		RegisterDeviceNotifyTokenReq  reqCmd = new RegisterDeviceNotifyTokenReq();
		reqCmd.setDeviceId(DeviceUtil.getRawDeviceId(this));
		reqCmd.setOsVersion(DeviceUtil.getDeviceOS());
		reqCmd.setNotifyToken("now  is null");
		TlvObject msgTlv = null;
		try {
			msgTlv = BroadcastCommandParser.encode(reqCmd);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isApplication.session.write(msgTlv);
	}

}
