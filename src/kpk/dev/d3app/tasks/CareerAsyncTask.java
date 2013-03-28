package kpk.dev.d3app.tasks;

import kpk.dev.d3app.database.DatabaseProcessorBase;
import kpk.dev.d3app.database.ProfilesDatabaseProcessor;
import kpk.dev.d3app.listeners.BaseDataListener;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class CareerAsyncTask extends BaseJSONAsyncTask {
	private String mServer;
	public CareerAsyncTask(BaseDataListener aListener, SQLiteDatabase aDatabase){
		super(aListener, aDatabase);
	}
	
	@Override
	protected Void doInBackground(Bundle... params) {
		String battleTag = params[0].getString(BATTLE_TAG_BUNDLE_KEY);
		battleTag = battleTag.replace('#', '-');
		String region = params[0].getString(BaseJSONAsyncTask.REGION_BUNDLE_KEY);
		String serverURL = "http://" + region + ".battle.net" + "/api/d3/profile/" + battleTag + "/";
		mServer = params[0].getString(BaseJSONAsyncTask.REGION_BUNDLE_KEY);
		DatabaseProcessorBase dbProccessor = new ProfilesDatabaseProcessor();
		parseAndWriteObjectstToDB(readJSON(serverURL), dbProccessor, ProfileModel.class, mServer);
		return null;
	}
}
