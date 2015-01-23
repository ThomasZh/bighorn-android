package com.example.minam.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.view.View;

public class QueneManager {
	
	
	private static Map<Integer,Timer> map_timer = new HashMap<Integer,Timer>();
    private static  Map<Integer,View> map_pro = new HashMap<Integer,View>();
	public static Map<Integer,Timer>  getTimerList(){
    	
    	return map_timer;
    }
	public static Map<Integer,View>  getGressList(){
		
		return map_pro;
	}
	

}
 