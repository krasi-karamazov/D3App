package kpk.dev.d3app.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import kpk.dev.d3app.database.DatabaseProcessorBase;
import kpk.dev.d3app.database.HeroDatabaseProcessor;
import kpk.dev.d3app.listeners.BaseDataListener;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

public abstract class BaseJSONAsyncTask extends AsyncTask<Bundle, String, Void> {
	public static final String BATTLE_TAG_BUNDLE_KEY = "battletagKey";
	public static final String REGION_BUNDLE_KEY = "regionKey";
	public static final String HERO_ID_BUNDLE_KEY = "hero_id";
	private SQLiteDatabase mDatabase;
	private boolean mResult;
	private BaseDataListener mListener;
	private IProfileModel mProfileModel;
	public enum TaskType{
		CAREER, HERO, DELETE;
	}
	
	protected BaseJSONAsyncTask(BaseDataListener listener, SQLiteDatabase database) {
		mListener = listener;
		mDatabase = database;
	}
	
	@Override
	protected Void doInBackground(Bundle... params) {
		return null;
	}
	
	protected synchronized final String readJSON(String url) {
		try{
			HttpClient client = new DefaultHttpClient();
			HttpGet getMethod = new HttpGet(url);
			HttpResponse httpResponse = client.execute(getMethod);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			if(responseCode == HttpStatus.SC_OK) {
				final StringBuilder builder = new StringBuilder(EntityUtils.toString(httpResponse.getEntity()));
				return builder.toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	protected <T extends IProfileModel> IProfileModel  parseAndWriteObjectstToDB(String jsonString, DatabaseProcessorBase dbProccessor, Class<T> clazz, String server){
		try {
			JsonFactory f = new MappingJsonFactory();
			JsonParser jp = f.createJsonParser(jsonString);
			IProfileModel profile = jp.readValueAs(ProfileModel.class);
			profile.setServer(server);
			profile.getContentValues();
			final List<IProfileModel> profiles = new ArrayList<IProfileModel>();
			profiles.add(profile);
			mResult = dbProccessor.insertData(profiles, mDatabase);
			mProfileModel = profile;
			return mProfileModel;
			
		} catch (Exception e) {
			return null;
		} 
	}
	
	public static BaseJSONAsyncTask getTask(TaskType type, BaseDataListener aListener, SQLiteDatabase aDatabase) {
		BaseJSONAsyncTask task = null;
		if(type.equals(TaskType.CAREER)){
			task = new CareerAsyncTask(aListener, aDatabase);
		}else if(type.equals(TaskType.DELETE)){
			return new DeleteProfileTask(aListener, aDatabase);
		}else if(type.equals(TaskType.HERO)) {
			task = new HeroAsyncTask(aListener, aDatabase);
		}
		return task;
	}
	
	protected final SQLiteDatabase getDatabase(){
		return mDatabase;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mListener.dataReady(mProfileModel, mResult, null);
	}
	
	protected final void saveFullHeroData(HeroModelDecorator fullHeroData) {
		HeroDatabaseProcessor dbProcessor = new HeroDatabaseProcessor();
		dbProcessor.saveHeroData(fullHeroData, mDatabase);
	}
}