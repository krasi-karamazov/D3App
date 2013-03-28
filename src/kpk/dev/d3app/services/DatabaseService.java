package kpk.dev.d3app.services;

import kpk.dev.d3app.database.D3AppSQLitehelper;
import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;

public class DatabaseService extends Service {
	private SQLiteDatabase mDatabase;
	private LocalBinder mBinder;
	@Override
	public void onCreate() {
		super.onCreate();
		D3AppSQLitehelper openHelper = D3AppSQLitehelper.getInstance(getApplicationContext());
		mDatabase = openHelper.getWritableDatabase();
		mBinder = new LocalBinder(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public SQLiteDatabase getDatabase() {
		return mDatabase;
	}
	
	public class LocalBinder extends Binder{
		private DatabaseService mService;
		public LocalBinder(DatabaseService service) {
			mService = service;
		}
		
		public DatabaseService getService(){
			return mService;
		}
		
		void clearService(){
			mService = null;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(!mDatabase.inTransaction()){
			mDatabase.close();
		}
		//KPKLog.d("DESTROY SERVICE");
		mBinder.clearService();
		//KPKLog.d("DATABSE CLOSED: " + mDatabase.isOpen());
		mBinder = null;
	}

}
