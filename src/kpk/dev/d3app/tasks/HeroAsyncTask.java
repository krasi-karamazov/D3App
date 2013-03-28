package kpk.dev.d3app.tasks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kpk.dev.d3app.database.ProfilesDatabaseProcessor;
import kpk.dev.d3app.listeners.BaseDataListener;
import kpk.dev.d3app.models.accountmodels.D3Item;
import kpk.dev.d3app.models.accountmodels.HeroModel;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.util.D3Constants;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HeroAsyncTask extends BaseJSONAsyncTask {
	private String mServer;
	public HeroAsyncTask(BaseDataListener aListener, SQLiteDatabase aDatabase) {
		super(aListener, aDatabase);
	}
	
	@Override
	protected Void doInBackground(Bundle... params) {
		String battleTag = params[0].getString(BATTLE_TAG_BUNDLE_KEY);
		battleTag = battleTag.replace('#', '-');
		String unModifiedBattleTag = params[0].getString(BATTLE_TAG_BUNDLE_KEY);
		mServer = params[0].getString(BaseJSONAsyncTask.REGION_BUNDLE_KEY);
		long heroId = params[0].getLong(BaseJSONAsyncTask.HERO_ID_BUNDLE_KEY);
		SQLiteDatabase database = getDatabase();
		if(database == null){
			return null;
		}
		HeroModel hero = new ProfilesDatabaseProcessor().getHeroById(getDatabase(), unModifiedBattleTag, mServer, heroId);
		//ProfileModel model = new ProfilesDatabaseProcessor().getProfileByServerAndBattleTag(database, mServer, battleTag);
		readAndParseHeroJSON(mServer, battleTag, hero);
		return null;
	}
	
	private synchronized void readAndParseHeroJSON(String server, String battleTag, HeroModel hero) {
		HeroModelDecorator decorator = getFullHeroModel(hero, battleTag);
		if(decorator == null){
			return;
		}
		Map<String, D3Item> items = decorator.getItems();
		Set<String> keys = items.keySet();
		for(String s : keys){
			D3Item item = items.get(s);
			item.setGemsString(getItemGems(item.getToolTipParams(), decorator.getHeroModel().getServer()));
		}
		saveFullHeroData(decorator);
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
            //KPKLog.d(heroJSONURL);
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
