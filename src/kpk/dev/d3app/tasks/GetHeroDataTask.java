package kpk.dev.d3app.tasks;

import java.util.ArrayList;
import java.util.List;

import kpk.dev.d3app.database.HeroDatabaseProcessor;
import kpk.dev.d3app.database.HeroDatabaseProcessor.HeroDataType;
import kpk.dev.d3app.listeners.DataReadyListener;
import kpk.dev.d3app.models.accountmodels.IProfileModel;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

public class GetHeroDataTask extends AsyncTask<Bundle, String, List<IProfileModel>> {
	public static final String HERO_ID_BUNDLE_KEY = "hero_id_key";
	public static final String HERO_SERVER_BUNDLE_KEY = "hero_server_key";
	public static final String HERO_PROFILE_TAG = "hero_profile_tag_key";
	public static final String FOLLOWER_SLUG_BUNDLE_KEY = "follower_slug_key";
	private HeroDatabaseProcessor mDatabaseProcessor;
	private DataReadyListener mListener;
	private SQLiteDatabase mDatabase;
	private HeroDataType mDataType;
	public GetHeroDataTask(DataReadyListener listener, SQLiteDatabase database, HeroDataType type) {
		mListener = listener;
		mDatabase = database;
		mDataType = type;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDatabaseProcessor = new HeroDatabaseProcessor();
	}
	@Override
	protected List<IProfileModel> doInBackground(Bundle... params) {
		Bundle bundle = params[0];
		
		if(bundle.containsKey(HERO_ID_BUNDLE_KEY)){
			final List<IProfileModel> profileModels = new ArrayList<IProfileModel>();
			profileModels.add(mDatabaseProcessor.getHeroData(mDataType, params[0].getLong(HERO_ID_BUNDLE_KEY), 
					params[0].containsKey(HERO_ID_BUNDLE_KEY)?params[0].getString(FOLLOWER_SLUG_BUNDLE_KEY):null, mDatabase));
			return profileModels;
		}else{
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(List<IProfileModel> result) {
		super.onPostExecute(result);
		mListener.dataReadyListener(result);
	}
}
