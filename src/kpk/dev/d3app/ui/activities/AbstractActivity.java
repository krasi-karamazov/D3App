package kpk.dev.d3app.ui.activities;

import kpk.dev.d3app.R;
import kpk.dev.d3app.application.D3ApplicationBase;
import kpk.dev.d3app.listeners.DatabaseReadyListener;
import kpk.dev.d3app.services.DatabaseService;
import kpk.dev.d3app.services.DatabaseService.LocalBinder;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;

public abstract class AbstractActivity extends FragmentActivity implements DatabaseReadyListener {
	private SQLiteDatabase mDatabase;
	private boolean mBound;
	private D3ApplicationBase mApplicationBase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplicationBase = (D3ApplicationBase)getApplication();
		Intent serviceIntent = new Intent(this, DatabaseService.class);
		overridePendingTransition(R.anim.activity_anim_exit, R.anim.activity_anim_enter);
		if(!mBound){
			try{
				bindService(serviceIntent, serviceConnection, Service.BIND_AUTO_CREATE);
			}catch(IllegalStateException e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	final public void databaseReady() {
		initComponents();
	}
	
	final public D3ApplicationBase getApplicationBase(){
		return mApplicationBase;
	}
	
	protected abstract void initComponents();
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LocalBinder binder = (LocalBinder)service;
			mDatabase = binder.getService().getDatabase();
			mBound = true;
			databaseReady();
		}
	};
	
	final protected SQLiteDatabase getDatabase() {
		return mDatabase;
	}
	@Override
	protected void onDestroy() {
		if(mBound){
			unbindService(serviceConnection);
		}
		super.onDestroy();
		
	};
}
