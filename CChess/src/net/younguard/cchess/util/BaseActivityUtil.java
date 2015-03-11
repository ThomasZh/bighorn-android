package net.younguard.cchess.util;


import com.example.cchess.R;

import android.app.Activity;
import android.content.Intent;



public class BaseActivityUtil
{
    @SuppressWarnings("rawtypes")
    public static void startActivity(Activity source,Class target,boolean isNeedFinish)
    {
        startActivity(source, target, isNeedFinish, false);
    }
    
    
    @SuppressWarnings("rawtypes")
    public static void back2Activity(Activity source,Class target,boolean isNeedFinish)
    {
        startActivity(source, target, isNeedFinish, true);
    }
    
    @SuppressWarnings("rawtypes")
    private static void startActivity(Activity source,Class target,boolean isNeedFinish,boolean isBack)
    {
        if (source == null || target == null)
        {
            return;
        }
        
        if (isNeedFinish)
        {
            source.finish();
        }
        
        Intent it = new Intent(source,target);
        source.startActivity(it);
        
        if (!isBack)
        {
            source.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        else
        {
            source.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }
    
    
    public static void startActivity(Activity activity,Intent intent,boolean isNeedFinish,boolean isBack)
    {
        if (intent == null || activity == null)
        {
            return;
        }
        
        if (isNeedFinish)
        {
            activity.finish();
        }
        
        activity.startActivity(intent);
        
        if (!isBack)
        {
            activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        else
        {
            activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
    }
    
  
    public static void simpleBack(Activity activity)
    {
        if (activity == null)
        {
            return;
        }
        
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    
   
    public static void setStartTransition(Activity activity)
    {
        if (activity == null)
        {
            return;
        }
        
        activity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
    
    public static void setBackTransition(Activity activity)
    {
        if (activity == null)
        {
            return;
        }
        
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    
    
    public static void setUpTransition(Activity activity)
    {
        if (activity == null)
        {
            return;
        }
        
        activity.overridePendingTransition(R.anim.push_up_in, 0);
    }
    
    
    public static void setDownTransition(Activity activity)
    {
        if (activity == null)
        {
            return;
        }
        
        activity.overridePendingTransition(R.anim.push_down_out, R.anim.push_down_out);
    }
    
}
