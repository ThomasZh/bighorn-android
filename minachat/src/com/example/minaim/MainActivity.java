package com.example.minaim;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import net.younguard.bighorn.broadcast.cmd.BroadcastCommandParser;
import net.younguard.bighorn.broadcast.cmd.CommandTag;
import net.younguard.bighorn.broadcast.cmd.MsgPangResp;
import net.younguard.bighorn.broadcast.cmd.MsgPingReq;
import net.younguard.bighorn.broadcast.cmd.MsgPongResp;
import net.younguard.bighorn.broadcast.cmd.QueryOnlineNumReq;
import net.younguard.bighorn.broadcast.cmd.QueryOnlineNumResp;
import net.younguard.bighorn.comm.Command;
import net.younguard.bighorn.comm.codec.TlvPackageCodecFactory;
import net.younguard.bighorn.comm.tlv.TlvObject;
import net.younguard.bighorn.comm.util.DatetimeUtil;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.helpers.Util;

import android.R.color;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minaim.ClientHandler.Callback;
import com.example.minam.util.MinaUtil;

public class MainActivity extends Activity implements Callback {
	private ClientHandler ch;
	private LinearLayout sline; // ��̬��������¼
	int timestamp; // ��¼��ǰʱ��
	TextView people_num; // ��¼�û�����
	private SocketConnector connector;
	private IoFilter filter;
	private SocketAddress soketAddress;
	private ConnectFuture future;
	private Boolean isNetWork;
	EditText sendContent;
	private Button sendBtn;
	private TextView tv_net;
	private TextView tv_sever;
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
	int reConnect = 0;
	private Timer timer1;
	private Boolean isCon;
	private EditText text;
	private ProgressBar bar;
	private IoSession session;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// /---------------------

		// /---------------------
		init();
		super.onCreate(savedInstanceState);
		// ��ʼ������ؼ�
		setContentView(R.layout.activity_main);
		text = new EditText(this);
		bar = (ProgressBar) findViewById(R.id.progress);
		String useName = MinaUtil.hasName(this);
		Boolean hasName = TextUtils.isEmpty(useName);
		if (hasName) {
			// Toast.makeText(this,"����������", 1).show();
			Builder dialog = new Builder(this);

			dialog.setTitle("给自己一个响亮的名称吧");

			// .setIcon(android.R.drawable.ic_dialog_info)
			dialog.setView(text);

			dialog.setPositiveButton("开始聊天",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String name = text.getText().toString();
							if (TextUtils.isEmpty(name)) {
								Toast.makeText(MainActivity.this, "你还没有输入昵称呢",
										1).show();

								try {
									Field field = dialog.getClass()
											.getSuperclass()
											.getDeclaredField("mShowing");
									field.setAccessible(true);
									// ����mShowingֵ����ƭandroidϵͳ
									field.set(dialog, false);
								} catch (Exception e) {
									e.printStackTrace();
								}

							} else {
								SharedPreferences share = getPreferences(Context.MODE_PRIVATE);
								Editor editor = share.edit();
								editor.putString("name", name);

								editor.commit();
								try {
									Field field = dialog.getClass()
											.getSuperclass()
											.getDeclaredField("mShowing");
									field.setAccessible(true);
									// ����mShowingֵ����ƭandroidϵͳ
									field.set(dialog, true);
								} catch (Exception e) {
									e.printStackTrace();
								}

							}

						}
					}).setNegativeButton("残忍拒绝",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							System.exit(0);
						}
					});

			// dialog.show();
			dialog.create();
			dialog.setCancelable(false);
			dialog.show();

		}
		people_num = (TextView) findViewById(R.id.people);
		tv_net = (TextView) findViewById(R.id.network);
		tv_sever = (TextView) findViewById(R.id.server);
		tv_net.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Settings.ACTION_SETTINGS);
				startActivityForResult(intent, 0);
			}
		});
		sline = (LinearLayout) findViewById(R.id.msgTxt);
		sendBtn = (Button) findViewById(R.id.sendBtn);
		sendContent = (EditText) findViewById(R.id.sendContent);

		IntentFilter mFilter = new IntentFilter(); // ����ע��㲥
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);

		// start(true);

	}

	@Override
	public void connected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loggedIn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loggedOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnected(IoSession session) {
		// TODO Auto-generated method stub
		Log.i("�̲�", "�̲�");
		if (isNetWork) {

			start();
		}

	}

	@Override
	public void messageReceived(Object message) {
		// BROADCAST OK user6: vg
		/*
		 * String[] result = message.split(" ", 3); String status = result[1];
		 * System.out.println(status);
		 */

		/* new StringBuilder(result[2]); */
		msg_obj = message;
		mHandler.postDelayed(mUpdateUITimerTask, 0);

	}

	private Object msg_obj;
	// ����ˢ��textview, ����UI�ĸ���Ҫ�����߳���
	private final Runnable mUpdateUITimerTask = new Runnable() {
		public void run() {
			// logger.debug("Session recv...");

			if (msg_obj != null && msg_obj instanceof TlvObject) {
				TlvObject pkg = (TlvObject) msg_obj;
				// TlvByteUtilPrinter.hexDump("message body: ", pkg.getValue());

				Command respCmd = null;
				try {
					// decode all the message to request command
					respCmd = BroadcastCommandParser.decode(pkg);
				} catch (UnsupportedEncodingException uee) {
					// logger.warn(uee.getMessage());
					// session.close(true);
					return;// break the logic blow
				}

				switch (pkg.getTag()) {
				case CommandTag.MESSAGE_PONG_RESPONSE:
					MsgPongResp pongRespCmd = (MsgPongResp) respCmd;
					String msg = pongRespCmd.getContent();
					String name = pongRespCmd.getUsername();
					show(name + ":" + msg);

					/*LinearLayout line = new LinearLayout(MainActivity.this);
					line.setOrientation(LinearLayout.VERTICAL);
					RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					sline.addView(line, lp3);
					RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(
							200, LayoutParams.WRAP_CONTENT);

					TextView tv = new TextView(MainActivity.this);

					tv.setText(name + ":" + msg);
					tv.setBackgroundColor(Color.GRAY);

					View view = new View(MainActivity.this);
					RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, 30);

					line.setGravity(Gravity.LEFT);
					line.addView(tv, lp1);
					line.addView(view, lp2);*/
					
					
					
					Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
					
					t.setToNow(); // ȡ��ϵͳʱ�䡣 
					
	                View view =   getLayoutInflater().inflate(R.layout.receivemsgview,null);
	                TextView tv_time = (TextView) view.findViewById(R.id.msgtime);
	                TextView tv_name = (TextView) view.findViewById(R.id.rename);
	                tv_name.setText(name);
	                tv_time.setText(String.valueOf(t.hour+":"+t.minute+":"+t.second));
	                TextView tv_content = (TextView) view.findViewById(R.id.msgcontent);
	                tv_content.setText(msg);
	                sline.addView(view);
	                

					break;
				case CommandTag.MESSAGE_PANG_RESPONSE:
					MsgPangResp pangRespCmd = (MsgPangResp) respCmd;

					break;

				case CommandTag.QUERY_ONLINE_NUMBER_RESPONSE:
					QueryOnlineNumResp qonRespCmd = (QueryOnlineNumResp) respCmd;
					int num = qonRespCmd.getNum();
					bar.setVisibility(View.GONE);
					people_num.setVisibility(View.VISIBLE);
					people_num.setText("当前在线人数(" + num + ")");

					break;
				}

			}

		}
	};
	private final Handler mHandler = new Handler();

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// �жϰ汾��ʽ,����汾 > 2.3,��������Ӧ�ĳ�����д���,�Ա�Ӱ���������
	@TargetApi(9)
	private static void init() {
		String strVer = android.os.Build.VERSION.RELEASE; // ��õ�ǰϵͳ�汾
		strVer = strVer.substring(0, 3).trim(); // ��ȡǰ3���ַ� 2.3.3ת����2.3
		float fv = Float.valueOf(strVer);
		if (fv > 2.3) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
					.build());
		}
	}

	// ���������߼�

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void show(String msg) {
		NotificationCompat.Builder mBuilder = // Notification �ļ�����

		new NotificationCompat.Builder(this)

		.setSmallIcon(R.drawable.ic_launcher) // ��û������largeicon����Ϊ��ߵĴ�icon��������largeicon����Ϊ���½ǵ�Сicon��������������Ӱ��Notifications
												// area��ʾ��ͼ��

				.setContentTitle("���꾯����") // ����

				.setContentText(msg) // ����

				// .setNumber(3) //������Ϣ����

				// .setContentInfo("3") //����ͬ�ϣ�������Ϣ������

				// .setLargeIcon(smallicon) //largeicon��

				.setDefaults(Notification.DEFAULT_SOUND)// ������������ΪĬ������

				// .setVibrate(vT) //�����𶯣���������Ϊ��long vT[]={300,100,300,100};
				// ���������õƹ�.setLights(argb, onMs, offMs)

				.setOngoing(false) // trueʹnotification��Ϊongoing���û������ֶ����������QQ,false���߲�������Ϊ��ͨ��֪ͨ

				.setAutoCancel(true); // ���֮���Զ���ʧ

		Intent resultIntent = new Intent(this, ResultActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(this);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager manager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1000, mBuilder.build());
	}

	/**
	 * ��server�˽������ӣ����������ӽ��
	 * 
	 */
	public Boolean initCon() {

		connector = new NioSocketConnector();
		filter = new ProtocolCodecFilter(new TlvPackageCodecFactory());
		connector.getFilterChain().addLast("vestigge", filter);
		soketAddress = new InetSocketAddress("54.186.197.254", 13103);
		ch = new ClientHandler(MainActivity.this);
		connector.setHandler(ch);
		future = connector.connect(soketAddress);
		future.join();
		return future.isConnected();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		// start(true);
	}

	public void start() {
		isNetWork = MinaUtil.isNetworkConnected(this);

		if (!initCon() && isNetWork) {

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					reConnect = reConnect + 5000;
					if (reConnect == 10000) {
						tv_sever.setVisibility(View.VISIBLE);
					}
					start();

				}
			};
			TimerTask task = new TimerTask() {
				public void run() {
					Message message = new Message();

					message.what = 1;
					handler.sendMessage(message);
				}
			};
			if (timer1 != null) {

				timer1.cancel();
			}
			timer1 = new Timer(true);
			timer1.schedule(task, 0, reConnect); // ��ʱ0ms��ִ�У�30000msִ��һ��

		} else {
			if (timer1 != null) {

				timer1.cancel();
			}
			reConnect = 0;
			tv_sever.setVisibility(View.GONE);
			sendContent.setText("");
			session = future.getSession(); // ��������session

			// ÿ����ʮ����µ�ǰ����
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					timestamp = DatetimeUtil.currentTimestamp();
					QueryOnlineNumReq qonReqCmd = new QueryOnlineNumReq(
							timestamp);

					try {
						TlvObject qonTlv = BroadcastCommandParser
								.encode(qonReqCmd);
						session.write(qonTlv);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			TimerTask task = new TimerTask() {
				public void run() {
					Message message = new Message();

					message.what = 1;
					handler.sendMessage(message);
				}
			};

			Timer timer = new Timer(true);
			timer.schedule(task, 0, 30000); // ��ʱ0ms��ִ�У�30000msִ��һ��

		}

	}

	private BroadcastReceiver mReceiver = /**
	 * @author Administrator ��������״�������仯
	 */
	new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.d("mark", "����״̬�Ѿ��ı�");
				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					tv_net.setVisibility(View.GONE);
					String name = info.getTypeName();
					Log.i("mark", "��ǰ�������ƣ�" + name);
					start();
				} else {
					Log.d("mark", "û�п�������");
					tv_net.setVisibility(View.VISIBLE);
					tv_sever.setVisibility(View.GONE);
				}
			}
		}
	};

	/*
	 * ��������̷��Ͱ�ť,������Ϣ�����¼�
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

				/* ��������� */
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager.isActive()) {
					inputMethodManager.hideSoftInputFromWindow(
							MainActivity.this.getCurrentFocus()
									.getWindowToken(), 0);
				}
				timestamp = DatetimeUtil.currentTimestamp();
				String sendMsgText = sendContent.getText().toString();
				
				System.out.println(sendMsgText);
				MsgPingReq reqCmd = new MsgPingReq(timestamp,
						MinaUtil.hasName(MainActivity.this), sendMsgText);
				TlvObject msgTlv = null;
				try {
					msgTlv = BroadcastCommandParser.encode(reqCmd);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				session.write(msgTlv);

				sendContent.setText("");
				
				
				Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�  
				t.setToNow(); // ȡ��ϵͳʱ�䡣  
				
				
				
                View view =   getLayoutInflater().inflate(R.layout.sendmsgview,null);
                TextView tv_time = (TextView) view.findViewById(R.id.msgtime);
                tv_time.setText(String.valueOf(t.hour+":"+t.minute+":"+t.second));
                TextView tv_content = (TextView) view.findViewById(R.id.msgcontent);
                tv_content.setText(sendMsgText);
                sline.addView(view);
                
				
				return true;
			}
		}
		 return super.dispatchKeyEvent(event); 

	}

}
