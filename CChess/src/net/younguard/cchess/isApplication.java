package net.younguard.cchess;

import java.io.UnsupportedEncodingException;

import net.younguard.bighorn.BroadcastCommandParser;
import net.younguard.bighorn.comm.Command;
import net.younguard.bighorn.comm.tlv.TlvObject;

import org.apache.mina.core.session.IoSession;

import android.app.Application;
import android.content.res.Configuration;

public class isApplication extends Application{
	public static IoSession session=null;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	public static void send(Object reqCmd ){
		TlvObject msgTlv = null;
		try {
			msgTlv = BroadcastCommandParser.encode((Command) reqCmd);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isApplication.session.write(msgTlv);
	}

}
