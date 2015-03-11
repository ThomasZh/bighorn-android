package net.younguard.cchess.Handler;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.CommandTag;
import net.younguard.bighorn.account.cmd.DeviceLoginResp;
import net.younguard.bighorn.account.cmd.RegisterDeviceNotifyTokenResp;
import net.younguard.bighorn.comm.Command;
import net.younguard.bighorn.comm.tlv.TlvObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderException;

import com.younguard.CChess.config.AppConfig;
import com.younguard.CChess.param.AccountParams;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ClientHandler extends IoHandlerAdapter {
	private Context context;
	public ClientHandler(Context context) {
		// TODO Auto-generated method stub
		this.context = context;

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		super.messageSent(session, message);
		Log.i("cc....", "messageSent..................");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
		Log.i("cc....", "sessionClosed..................");

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
		Log.i("cc....", "sessionCreated..................");

	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		super.messageReceived(session, message);
		Log.i("cc....", "messageReceived..................");

		if (message != null && message instanceof TlvObject) {
			TlvObject pkg = (TlvObject) message;
			Command respCmd = null;
			try {

				respCmd = BroadcastCommandParser.decode(pkg);

			} catch (UnsupportedEncodingException uee) {

				Log.i("cc", "response  Ω‚Œˆ ß∞‹£°");
				return;// break the logic blow
			}

			switch (pkg.getTag()) {
			case CommandTag.REGISTER_NOTIFY_TOKEN_RESPONSE:
				
				RegisterDeviceNotifyTokenResp registerDeviceNotifyToken = (RegisterDeviceNotifyTokenResp) respCmd;
				AppConfig.account = new AccountParams();
				AppConfig.account.setAccountId(registerDeviceNotifyToken.getAccountId());
				Log.i("cc", "accountId  save  ok ......");

				break;
			case CommandTag.DEVICE_LOGIN_RESPONSE:
				
				DeviceLoginResp deviceLogin = (DeviceLoginResp) respCmd;
				Intent in = new Intent("com.cc.chengcai");
				context.sendBroadcast(in);
				Log.i("cc", "login succufull......");
				
				break;
			}

		}

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
		Log.i("cc....", "exceptionCaught..................");
		if (cause instanceof TimeoutException) {
			Log.i("cc....", "TimeoutException..................");
		}
		if (cause instanceof SocketException) {
			Log.i("cc....", "«ÎºÏ≤ÈÕ¯¬Á..................");
		}
		if (cause instanceof ProtocolDecoderException) {
			Log.i("cc....", "Ω‚Œˆ ß∞‹..................");
		}
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);
		Log.i("cc....", "sessionOpened..................");
	}

}