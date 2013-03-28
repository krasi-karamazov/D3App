package kpk.dev.d3app.tasks;

import kpk.dev.d3app.database.ProfilesDatabaseProcessor;
import kpk.dev.d3app.listeners.BaseDataListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class DeleteProfileTask extends BaseJSONAsyncTask {
	
	private BaseDataListener mListener;
	private SQLiteDatabase mDatabase;
	private String[] args;
	public DeleteProfileTask(BaseDataListener listener, SQLiteDatabase database) {
		super(listener, database);
		mListener = listener;
		mDatabase = database;
	}
	
	@Override
	protected Void doInBackground(Bundle... params) {
		final ProfilesDatabaseProcessor profileDBProc = new ProfilesDatabaseProcessor();
		args = profileDBProc.deleteProfile(mDatabase, params[0].getString(BATTLE_TAG_BUNDLE_KEY), params[0].getString(REGION_BUNDLE_KEY));
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		mListener.dataReady(null, false, args);
	}
}
