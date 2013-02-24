package kpk.dev.d3app.tasks;

import java.util.ArrayList;
import java.util.List;

import kpk.dev.d3app.database.ProfilesDatabaseProcessor;
import kpk.dev.d3app.listeners.DataReadyListener;
import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class ReadRecordsTask extends AsyncTask<ModelType, Integer, List<IProfileModel>> {
	private SQLiteDatabase mDatabase;
	private DataReadyListener mListener;
	public ReadRecordsTask(SQLiteDatabase database, DataReadyListener listener) {
		mDatabase = database;
		mListener = listener;
	}
	
	@Override
	protected List<IProfileModel> doInBackground(ModelType... type) {
		List<IProfileModel> models = new ArrayList<IProfileModel>();
		if(type[0].name().equalsIgnoreCase(ModelType.profiles.name())){
			models.addAll(new ProfilesDatabaseProcessor().getProfiles(mDatabase));
		}else if(type[0].name().equalsIgnoreCase(ModelType.profile.name())){
			IProfileModel profile = new ProfilesDatabaseProcessor().getProfileByServerAndBattleTag(mDatabase, 
					type[0].mServer, type[0].mBattleTag);
			models.add(profile);
		}
		return models;
	}
	
	@Override
	protected void onPostExecute(List<IProfileModel> result) {
		super.onPostExecute(result);
		mListener.dataReadyListener(result);
	}

}
