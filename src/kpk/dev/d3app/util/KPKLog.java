package kpk.dev.d3app.util;

import android.util.Log;

public class KPKLog {
	public static Boolean DEBUG_MODE;
	public static String LOG_TAG;
	
	public static void d(String msg){
		if(DEBUG_MODE){
			Log.d(LOG_TAG, msg);
		}
	}
	
	public static void e(String msg){
		if(DEBUG_MODE){
			Log.e(LOG_TAG, msg);
		}
	}
	
	public static void w(String msg){
		if(DEBUG_MODE){
			Log.w(LOG_TAG, msg);
		}
	}
	
	public static void i(String msg){
		if(DEBUG_MODE){
			Log.i(LOG_TAG, msg);
		}
	}
}
