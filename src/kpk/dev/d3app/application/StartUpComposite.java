package kpk.dev.d3app.application;

import kpk.dev.d3app.R;
import kpk.dev.d3app.ui.activities.SplashScreenActivity;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
import kpk.dev.d3app.util.Utils;
import android.content.Context;
import android.content.SharedPreferences;

public class StartUpComposite {
	private SplashScreenActivity mActivity;
	private long mUpdatePeriod;
	private long mTimeOfLastUpdate;
	public enum StartUpMode{
		FirstTimeWithoutConnection,
		FirstTimeWithConnection,
		NotFirstTimeWithoutConnection,
		NotFirstTimeWithConnection;
	}
	
	public StartUpComposite(SplashScreenActivity activity) {
		mActivity = activity;
	}
	
	public StartUpMode init(){
		SharedPreferences sharedPrefs = mActivity.getSharedPreferences(D3Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		StartUpMode startUpMode = null;
		if(!sharedPrefs.getBoolean(D3Constants.FIRST_LOGIN_COMPLETE_KEY, false) && !Utils.isConnectedToInternet(mActivity)) {
			startUpMode = StartUpMode.FirstTimeWithoutConnection;
		}else if(sharedPrefs.getBoolean(D3Constants.FIRST_LOGIN_COMPLETE_KEY, false) && !Utils.isConnectedToInternet(mActivity)){
			startUpMode = StartUpMode.NotFirstTimeWithoutConnection;
		}else if(sharedPrefs.getBoolean(D3Constants.FIRST_LOGIN_COMPLETE_KEY, false) && Utils.isConnectedToInternet(mActivity)){
			int updatePeriodIndex = sharedPrefs.getInt(D3Constants.SHARED_PREFS_UPDATE_INTERVAL_KEY, 3);
			final String[] arr = mActivity.getResources().getStringArray(R.array.update_interval_values_array);
			mUpdatePeriod = Long.valueOf(arr[updatePeriodIndex]);
			mTimeOfLastUpdate = sharedPrefs.getLong(D3Constants.LAST_UPDATE_TIME_KEY, -1);
			startUpMode = StartUpMode.NotFirstTimeWithConnection;
		}else if(!sharedPrefs.getBoolean(D3Constants.FIRST_LOGIN_COMPLETE_KEY, false) && Utils.isConnectedToInternet(mActivity)){
			startUpMode = StartUpMode.FirstTimeWithConnection;
		}
		
		return startUpMode;
	}
	
	public long getUpdatePeriod() {
		return mUpdatePeriod;
	}
	
	public long getLastUpdateTime() {
		return mTimeOfLastUpdate;
	}
}
