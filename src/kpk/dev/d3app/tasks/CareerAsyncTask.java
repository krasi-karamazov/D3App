package kpk.dev.d3app.tasks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kpk.dev.d3app.database.DatabaseProcessorBase;
import kpk.dev.d3app.database.ProfilesDatabaseProcessor;
import kpk.dev.d3app.listeners.BaseDataListener;
import kpk.dev.d3app.models.accountmodels.D3Item;
import kpk.dev.d3app.models.accountmodels.HeroModel;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.models.accountmodels.IProfileModel;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CareerAsyncTask extends BaseJSONAsyncTask {
	
	private String mServer;
	
	public CareerAsyncTask(BaseDataListener aListener, SQLiteDatabase aDatabase){
		super(aListener, aDatabase);
	}
	
	@Override
	protected Void doInBackground(Bundle... params) {
		long time = System.currentTimeMillis();
		
		String battleTag = params[0].getString(BATTLE_TAG_BUNDLE_KEY);
		battleTag = battleTag.replace('#', '-');
		String region = params[0].getString(BaseJSONAsyncTask.REGION_BUNDLE_KEY);
		String serverURL = "http://" + region + ".battle.net" + "/api/d3/profile/" + battleTag + "/";
		mServer = params[0].getString(BaseJSONAsyncTask.REGION_BUNDLE_KEY);
		DatabaseProcessorBase dbProccessor = new ProfilesDatabaseProcessor();
		
		IProfileModel insertedProfile = parseAndWriteObjectstToDB(readJSON(serverURL), dbProccessor, ProfileModel.class, mServer);
		readAndParseHeroJSON(region, battleTag, ((ProfileModel)insertedProfile).getHeroes());
		KPKLog.d("Time: " + (System.currentTimeMillis() - time));
		return null;
	}
	
	private synchronized void readAndParseHeroJSON(String server, String battleTag, List<HeroModel> heroes) {
		final List<HeroModelDecorator> fullModels = new ArrayList<HeroModelDecorator>();
		for(HeroModel hero : heroes){
			HeroModelDecorator heroData = getFullHeroModel(hero, battleTag);
			Map<String, D3Item> items = heroData.getItems();
			Set<String> keys = items.keySet();
			for(String s : keys){
				D3Item item = items.get(s);
				item.setGemsString(getItemGems(item.getToolTipParams(), heroData.getHeroModel().getServer()));
				
			}
			fullModels.add(heroData);
		}
		saveFullHeroData(fullModels);
	}
	
	private synchronized String getItemGems(String itemString, String server) {
		String itemJSON = readJSON("http://" + server.toLowerCase(new Locale("en-US")) + D3Constants.ITEM_INFO_URL + itemString);
		ObjectMapper mapper = new ObjectMapper();
		try {
			StringBuilder gemsString = new StringBuilder();
			Map<?,?> rootAsMap = mapper.readValue(itemJSON, Map.class);
			ArrayList<?> gemsList = (ArrayList<?>)rootAsMap.get("gems");

			for(int i = 0; i < gemsList.size(); i++) {
				LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>)gemsList.get(i);
				LinkedHashMap<?, ?> itemMap = (LinkedHashMap<?, ?>)map.get("item");
				gemsString.append(D3Constants.LARGE_ITEM_ICON_URL + itemMap.get("icon").toString() + ".png,");
			}
			return gemsString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	private synchronized HeroModelDecorator getFullHeroModel(HeroModel hero, String battleTag){
		String heroJSONURL = "http://" + hero.getServer() + ".battle.net" 
				+ "/api/d3/profile/" + battleTag + "/hero/" + hero.getID();
		try{
			JsonFactory jsonFactory = new MappingJsonFactory();
			JsonParser jp = jsonFactory.createJsonParser(readJSON(heroJSONURL));
			HeroModelDecorator decorator = jp.readValueAs(HeroModelDecorator.class);
			decorator.setHeroModel(hero);
			jp.close();
			return decorator;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
