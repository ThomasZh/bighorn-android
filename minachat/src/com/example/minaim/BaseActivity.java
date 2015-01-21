/**   
 * 文件名：BaseActivity.java   
 * 版本号：        
 * 日期：2012-6-20 
 * 创建人：
 * Copyright wadata 版权所有
 * 变更：
 */
 
package com.example.minaim;
 
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;
 
/**
 * 名称：BaseActivity 
 * 描述： 
 * 创建人： 
 * 日期：2012-6-20 下午5:53:35 
 * 变更：
 */
 
public class BaseActivity extends Activity {
	     mApplication app;
        @Override
        protected void onStop() {
                // TODO Auto-generated method stub
                super.onStop();
 
                if (!isAppOnForeground()) {
                        //app 进入后台
                	if(app==null){
                		app = (mApplication) getApplication();
                	}
                	app.isAlive=false;
                    goBack(); 
                        //全局变量isActive = false 记录当前已经进入后台
                }
        }
 
        @Override
        protected void onResume() {
                // TODO Auto-generated method stub
                super.onResume();
 
                if(app==null){
            		app = (mApplication) getApplication();
            	}
                if(app.isAlive==false){
                	goForward();
                }
            	app.isAlive=true;
                //if (!isActive) {
                        //app 从后台唤醒，进入前台
                         
                        //isActive = true;
                //}
        }
 
        /**
         * 程序是否在前台运行
         * 
         * @return
         */
        public boolean isAppOnForeground() {
                // Returns a list of application processes that are running on the
                // device
                 
                ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                String packageName = getApplicationContext().getPackageName();
 
                List<RunningAppProcessInfo> appProcesses = activityManager
                                .getRunningAppProcesses();
                if (appProcesses == null)
                        return false;
 
                for (RunningAppProcessInfo appProcess : appProcesses) {
                        // The name of the process that this object is associated with.
                        if (appProcess.processName.equals(packageName)
                                        && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                return true;
                        }
                }
 
                return false;
        }
        public void goBack(){}
        public void goForward(){}
}