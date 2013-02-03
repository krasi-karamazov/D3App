package kpk.dev.d3app.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import kpk.dev.d3app.models.accountmodels.D3Follower;
import kpk.dev.d3app.models.accountmodels.D3FollowerSkill;
import kpk.dev.d3app.models.accountmodels.D3Item;
import kpk.dev.d3app.models.accountmodels.D3Mode;
import kpk.dev.d3app.models.accountmodels.D3PassiveSkill;
import kpk.dev.d3app.models.accountmodels.D3Rune;
import kpk.dev.d3app.models.accountmodels.D3Skill;
import kpk.dev.d3app.models.accountmodels.HeroModel;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.models.accountmodels.HeroProgressionModel;
import kpk.dev.d3app.models.accountmodels.IProfileModel;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.models.accountmodels.SkillsModel;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HeroDatabaseProcessor extends DatabaseProcessorBase {
	
	public enum SkillDataType {
		active,
		passive,
		rune;
	}
	
	private enum MapDataType{
		Stats,
		Kills;
	}
	
	public enum HeroDataType{
		BasicHeroData,
		HeroActiveSkillsData,
		HeroItemsData,
		HeroPassiveSkillsData;
	}
	
	public synchronized void saveHeroData(HeroModelDecorator model, SQLiteDatabase database) {
		database.beginTransaction();
		try{
			saveHeroDatatoDB(model, database);
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	private void saveHeroDatatoDB(HeroModelDecorator model, SQLiteDatabase database) {
		clearData(database, model.getHeroID());
		saveHeroStats(model, database);
		saveHeroSkillsData(model.getHeroID(), D3Skill.TABLE_NAME, model.getActiveSkillsContentValues(), database, SkillDataType.active);
		saveHeroSkillsData(model.getHeroID(), D3PassiveSkill.TABLE_NAME, model.getPassiveSkillsContentValues(), database, SkillDataType.passive);
		saveHeroSkillsData(model.getHeroID(), D3Rune.TABLE_NAME, model.getRunesContentValues(), database, SkillDataType.rune);
		saveHeroProgress(model, database);
		saveHeroItems(model.getHeroID(), D3Item.TABLE_NAME, model.getHeroItemsContentValues(), database);
		saveHeroKills(model.getHeroID(), HeroModelDecorator.HERO_KILLS_TABLE_NAME, model.getKillsContentValues(), database);
		saveFollowers(model, D3Follower.TABLE_NAME, database);
	}
	
	private synchronized void saveFollowers(HeroModelDecorator model, String tableName, SQLiteDatabase database) {
		final Map<String, D3Follower> followers = model.getFollowersModels();
		Set<String> keysSet = followers.keySet();
		Iterator<String> iterator = keysSet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			D3Follower follower = followers.get(key);
			follower.setHeroID(model.getHeroID());
			database.insert(D3Follower.TABLE_NAME, null, follower.getContentValues());
			saveFollowerStats(follower, database);
			saveFollowerSkills(follower.getSkillsContentValues(), database);
			saveFollowerItems(follower.getItemsContentValues(), database);
		}
//		for(D3Follower follower : followers) {

//		}
	}
	
	private void saveFollowerItems(List<ContentValues> itemsContentValues, SQLiteDatabase database) {
		for(ContentValues values : itemsContentValues) {
			database.insert(D3Item.FOLLOWER_ITEMS_TABLE_NAME, null, values);
		}
	}

	private void saveFollowerSkills(List<ContentValues> skillsContentValues, SQLiteDatabase database) {
		for(ContentValues values : skillsContentValues) {
			database.insert(D3Skill.FOLLOWER_SKILLS_TABLE_NAME, null, values);
		}
	}

	private synchronized void saveFollowerStats(D3Follower follower, SQLiteDatabase database) {
		database.insert(D3Follower.FOLLOWER_STATS_TABLE_NAME, null, follower.getStatsContentValues());
	}
	
	private synchronized void saveHeroItems(long id, String tableName, List<ContentValues> values, SQLiteDatabase database) {
		for(ContentValues cv : values) {
			database.insert(tableName, null, cv);
		}
	}
	
	private synchronized void saveHeroProgress(HeroModelDecorator model, SQLiteDatabase database) {
		HeroProgressionModel progression = model.getHeroProgress();
		progression.setParentProfileTag(Long.valueOf(model.getHeroID()).toString());
		final List<D3Mode> d3modes = new ArrayList<D3Mode>();
		d3modes.add(progression.getNormal());
		d3modes.add(progression.getNightmare());
		d3modes.add(progression.getHell());
		d3modes.add(progression.getInferno());
		database.beginTransaction();
		try{
			for(int i = 0; i < d3modes.size(); i++){
				d3modes.get(i).setName(D3Constants.mModes[i]);
				final ContentValues contentValues = d3modes.get(i).getContentValues();
				contentValues.put(HeroModel.HERO_ID_COLUMN, model.getHeroID());
				contentValues.put(D3Mode.MODE_NAME_COLUMN, D3Constants.mModes[i]);
				if(contentValues.containsKey(IProfileModel.SERVER_COLUMN)) {
					contentValues.remove(IProfileModel.SERVER_COLUMN);
				}
				database.insert(HeroModelDecorator.HERO_PROGRESSION_TABLE_NAME, "null", contentValues);
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	private void clearData(SQLiteDatabase database, long id) {
		database.delete(D3Skill.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3PassiveSkill.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Rune.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(HeroModelDecorator.HERO_PROGRESSION_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Item.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(HeroModelDecorator.HERO_KILLS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Follower.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Skill.FOLLOWER_SKILLS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Follower.FOLLOWER_STATS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Item.FOLLOWER_ITEMS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
	}

	private synchronized void saveHeroSkillsData(long heroID, String tableName, List<ContentValues> contentValuesList, SQLiteDatabase database, SkillDataType type) {
		for(ContentValues cv : contentValuesList) {
			if(cv != null){
				cv.put(HeroModel.HERO_ID_COLUMN, heroID);
				database.insert(tableName, null, cv);
			}
		}
	}

	private void saveHeroStats(HeroModelDecorator model, SQLiteDatabase database) {
		database.insert(HeroModelDecorator.HERO_STATS_TABLE_NAME, null, model.getStatsContentValues());
	}
	
	private synchronized void saveHeroKills(long id, String tableName, ContentValues values, SQLiteDatabase database) {
		database.insert(tableName, null, values);
	}

	public synchronized IProfileModel getHeroData(HeroDataType dataType, long heroID, String followerSlug, SQLiteDatabase database) {
		if(dataType.name().equalsIgnoreCase(HeroDataType.BasicHeroData.name())){
			return getHeroData(heroID, database);
		}		
		return null;
	}

	private synchronized IProfileModel getHeroData(long heroID, SQLiteDatabase database) {
		HeroModelDecorator heroDecorator = null;
		String queryString = "select * from " + HeroModel.TABLE_NAME 
				+ " join " + HeroModelDecorator.HERO_STATS_TABLE_NAME 
				+ " join " + HeroModelDecorator.HERO_KILLS_TABLE_NAME
				+ " where " 
				+ HeroModel.TABLE_NAME + "." + HeroModel.HERO_ID_COLUMN + "=" + heroID + 
				" AND " + HeroModelDecorator.HERO_STATS_TABLE_NAME + "." + HeroModel.HERO_ID_COLUMN + "=" + heroID +  
				" AND " + HeroModelDecorator.HERO_KILLS_TABLE_NAME + "." + HeroModel.HERO_ID_COLUMN + "=" + heroID;
		
		Cursor cursor = database.rawQuery(queryString, null);
		
		
		if(cursor.moveToFirst()) {
			heroDecorator = new HeroModelDecorator(buildBasiHeroModel(cursor));
			heroDecorator.setStats(getHeroStats(cursor));
			heroDecorator.setKills(getHeroKills(cursor));
		}
		cursor.close();
		if(heroDecorator == null) {
			return null;
		}
		heroDecorator.setItems(getItems(heroID, database, null, D3Item.TABLE_NAME));
		heroDecorator.setSkills(getSkills(heroID, database));
		heroDecorator.setHeroProgress(getHeroProgression(database, heroID));
		heroDecorator.setFollowersModels(getFollowers(heroID, database));
		return heroDecorator;
	}
	
	private synchronized Map<String, D3Follower> getFollowers(long heroID, SQLiteDatabase database) {
		String queryString = "select * from " + D3Follower.TABLE_NAME + " where " + HeroModel.HERO_ID_COLUMN + "=" + heroID;
		Cursor cursor = database.rawQuery(queryString, null);
		final Map<String, D3Follower> followers = new HashMap<String, D3Follower>();
		while(cursor.moveToNext()) {
			int followerSlugColumn = cursor.getColumnIndexOrThrow(D3Follower.FOLLOWER_SLUG_COLUMN);
			int followerLevelColumn = cursor.getColumnIndexOrThrow(D3Follower.FOLLOWER_LEVEL_COLUMN);
			D3Follower follower = new D3Follower();
			follower.setSlug(cursor.getString(followerSlugColumn));
			follower.setLevel(cursor.getInt(followerLevelColumn));
			follower.setItems(getItems(heroID, database, follower.getSlug(), D3Item.FOLLOWER_ITEMS_TABLE_NAME));
			follower.setD3FollowerSkills(getFollowerSkills(heroID, follower.getSlug(), database));
			follower.setStats(getFollowerStats(heroID, follower.getSlug(), database, follower.getSlug()));
			followers.put(cursor.getString(followerSlugColumn), follower);
		}
		cursor.close();
		
		return followers;
	}

	private Map<String, Double> getFollowerStats(long heroID, String slug, SQLiteDatabase database, String followerSlug) {
		String queryString = "select * from " + D3Follower.FOLLOWER_STATS_TABLE_NAME + " where " + HeroModel.HERO_ID_COLUMN + "=" + heroID + " and " 
				+ D3Follower.FOLLOWER_SLUG_COLUMN + "='" + followerSlug + "'";
		Cursor cursor = database.rawQuery(queryString, null);
		Map<String, Double> statsMap = new LinkedHashMap<String, Double>();
		while(cursor.moveToNext()) {
			int keysColumnIndex = cursor.getColumnIndexOrThrow(HeroModelDecorator.STATS_KEYS_COLUMN);
			int valuesColumnIndex = cursor.getColumnIndexOrThrow(HeroModelDecorator.STATS_VALUES_COLUMN);
			String[] keys = cursor.getString(keysColumnIndex).split(",");
			String[] values = cursor.getString(valuesColumnIndex).split(",");
			for(int i = 0; i < keys.length; i++) {
				if(keys[i] != null && keys[i].length() > 0)
				statsMap.put(keys[i], Double.valueOf(values[i]));
			}
		}
		return statsMap;
	}

	private List<D3FollowerSkill> getFollowerSkills(long heroID, String slug, SQLiteDatabase database) {
		String query = "select * from " + D3Skill.FOLLOWER_SKILLS_TABLE_NAME
				+ " where " + D3Skill.FOLLOWER_SLUG_COLUMN + "='" + slug + "' and "
				+ HeroModel.HERO_ID_COLUMN + "=" + heroID;
		Cursor cursor = database.rawQuery(query, null);
		final List<D3FollowerSkill> skills = new ArrayList<D3FollowerSkill>();
		while(cursor.moveToNext()) {
			int skillSlugColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.SLUG_COLUMN);
			int skillNameColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.NAME_COLUMN);
			int skillIconColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.ICON_COLUMN);
			int skillLevelColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.LEVEL_COLUMN);
			int skillTooltipURLColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.TOOLTIP_URL_COLUMN);
			int skillDescriptionColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.DESCRIPTION_COLUMN);
			int skillSimpleDescriptionColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.SIMPLE_DESCRIPTION_COLUMN);
			D3FollowerSkill skill = new D3FollowerSkill();
			skill.setSlug(cursor.getString(skillSlugColumnIndex));
			skill.setName(cursor.getString(skillNameColumnIndex));
			skill.setIcon(cursor.getString(skillIconColumnIndex));
			skill.setLevel(cursor.getInt(skillLevelColumnIndex));
			skill.setTooltipURL(cursor.getString(skillTooltipURLColumnIndex));
			skill.setDescription(cursor.getString(skillDescriptionColumnIndex));
			skill.setSimpleDescription(cursor.getString(skillSimpleDescriptionColumnIndex));
			skills.add(skill);
		}
		cursor.close();
		return skills;
	}

	private SkillsModel getSkills(long heroID, SQLiteDatabase database) {
		final SkillsModel skillsModel = new SkillsModel();
		String activeSkillsQuery = "select * from " + D3Skill.TABLE_NAME + " where " + HeroModel.HERO_ID_COLUMN + "=" + heroID;
		String runesQuery = "select * from " + D3Rune.TABLE_NAME + " where " + HeroModel.HERO_ID_COLUMN + "=" + heroID;
		String passiveSkillsQuery = "select * from " + D3PassiveSkill.TABLE_NAME + " where " + HeroModel.HERO_ID_COLUMN + "=" + heroID;
		List<D3Skill> activeSkills = combineActiveSkillsWithRunes(getActiveSkills(database, activeSkillsQuery), getRunes(database, runesQuery));
		skillsModel.setActive(activeSkills);
		skillsModel.setPassive(getPassiveSkills(database, passiveSkillsQuery));
		return skillsModel;
	}

	private List<D3Skill> combineActiveSkillsWithRunes(List<D3Skill> activeSkills, List<D3Rune> runes) {
		for(int i = 0; i < activeSkills.size(); i++) {
			for(int j = 0; j < runes.size(); j++) {
				if(activeSkills.get(i).getSlug().equalsIgnoreCase(runes.get(j).getSkillSlug())){
					activeSkills.get(i).setRune(runes.get(j));
				}
			}
		}
		return activeSkills;
	}

	private List<D3PassiveSkill> getPassiveSkills(SQLiteDatabase database, String passiveSkillsQuery) {
		Cursor cursor = database.rawQuery(passiveSkillsQuery, null);
		final List<D3PassiveSkill> skills = new ArrayList<D3PassiveSkill>();
		while(cursor.moveToNext()) {
			int skillSlugColumnIndex = cursor.getColumnIndexOrThrow(D3PassiveSkill.SLUG_COLUMN);
			int skillNameColumnIndex = cursor.getColumnIndexOrThrow(D3PassiveSkill.NAME_COLUMN);
			int skillIconColumnIndex = cursor.getColumnIndexOrThrow(D3PassiveSkill.ICON_COLUMN);
			int skillTooltipColumnIndex = cursor.getColumnIndexOrThrow(D3PassiveSkill.TOOLTIP_URL_COLUMN);
			int skillDescriptionColumnIndex = cursor.getColumnIndexOrThrow(D3PassiveSkill.DESCRIPTION_COLUMN);
			int skillFlavorColumnIndex = cursor.getColumnIndexOrThrow(D3PassiveSkill.FLAVOR_COLUMN);
			
			D3PassiveSkill skill = new D3PassiveSkill();
			skill.setSlug(cursor.getString(skillSlugColumnIndex));
			skill.setName(cursor.getString(skillNameColumnIndex));
			skill.setIcon(cursor.getString(skillIconColumnIndex));
			skill.setTooltipURL(cursor.getString(skillTooltipColumnIndex));
			skill.setDescription(cursor.getString(skillDescriptionColumnIndex));
			skill.setFlavor(cursor.getString(skillFlavorColumnIndex));
			skills.add(skill);
		}
		return skills;
	}

	private List<D3Rune> getRunes(SQLiteDatabase database, String runesQuery) {
		Cursor cursor = database.rawQuery(runesQuery, null);
		final List<D3Rune> runes = new ArrayList<D3Rune>();
		while(cursor.moveToNext()){
			int skillSlugColumnIndex = cursor.getColumnIndexOrThrow(D3Rune.SKILL_SLUG_COLUMN);
			int runeSlugColumnIndex = cursor.getColumnIndexOrThrow(D3Rune.SLUG_COLUMN);
			int runeTypeColumnIndex = cursor.getColumnIndexOrThrow(D3Rune.TYPE_COLUMN);
			int runeNameColumnIndex = cursor.getColumnIndexOrThrow(D3Rune.NAME_COLUMN);
			int runeLevelColumnIndex = cursor.getColumnIndexOrThrow(D3Rune.LEVEL_COLUMN);
			int runeDescriptionColumnIndex = cursor.getColumnIndexOrThrow(D3Rune.DESCRIPTION_COLUMN);
			int runeSimpleDescriptionColumnIndex = cursor.getColumnIndexOrThrow(D3Rune.SIMPLE_DESCRIPTION_COLUMN);
			int runeTooltipParamsColumn = cursor.getColumnIndexOrThrow(D3Rune.TOOLTIP_PARAMS_COLUMN);

			D3Rune rune = new D3Rune();
			rune.setSkillSlug(cursor.getString(skillSlugColumnIndex));
			rune.setSlug(cursor.getString(runeSlugColumnIndex));
			rune.setType(cursor.getString(runeTypeColumnIndex));
			rune.setName(cursor.getString(runeNameColumnIndex));
			rune.setLevel(cursor.getInt(runeLevelColumnIndex));
			rune.setDescription(cursor.getString(runeDescriptionColumnIndex));
			rune.setSimpleDescription(cursor.getString(runeSimpleDescriptionColumnIndex));
			rune.setTooltipParams(cursor.getString(runeTooltipParamsColumn));
			runes.add(rune);
		}
		return runes;
	}

	private List<D3Skill> getActiveSkills(SQLiteDatabase database, String query) {
		Cursor cursor = database.rawQuery(query, null);
		final List<D3Skill> skills = new ArrayList<D3Skill>();
		while(cursor.moveToNext()) {
			int skillSlugColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.SLUG_COLUMN);
			int skillNameColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.NAME_COLUMN);
			int skillIconColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.ICON_COLUMN);
			int skillLevelColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.LEVEL_COLUMN);
			int skillCategoryColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.CATEGORY_SLUG_COLUMN);
			int skillTooltipURLColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.TOOLTIP_URL_COLUMN);
			int skillDescriptionColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.DESCRIPTION_COLUMN);
			int skillSimpleDescriptionColumnIndex = cursor.getColumnIndexOrThrow(D3Skill.SIMPLE_DESCRIPTION_COLUMN);
			D3Skill skill = new D3Skill();
			skill.setSlug(cursor.getString(skillSlugColumnIndex));
			skill.setName(cursor.getString(skillNameColumnIndex));
			skill.setIcon(cursor.getString(skillIconColumnIndex));
			skill.setLevel(cursor.getInt(skillLevelColumnIndex));
			skill.setCategorySlug(cursor.getString(skillCategoryColumnIndex));
			skill.setTooltipURL(cursor.getString(skillTooltipURLColumnIndex));
			skill.setDescription(cursor.getString(skillDescriptionColumnIndex));
			skill.setSimpleDescription(cursor.getString(skillSimpleDescriptionColumnIndex));
			skills.add(skill);
		}
		cursor.close();
		return skills;
	}

	private Map<String, D3Item> getItems(long heroID, SQLiteDatabase database, String followerSlug, String tableName) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select * from " + tableName + " where " + HeroModel.HERO_ID_COLUMN + "=" + heroID);
		if(followerSlug != null) {
			queryBuilder.append(" and " + D3Item.FOLLOWER_SLUG_COLUMN + "='" + followerSlug + "'");
		}
		Cursor cursor = database.rawQuery(queryBuilder.toString(), null);
		final Map<String, D3Item> itemsMap = new LinkedHashMap<String, D3Item>();
		while(cursor.moveToNext()) {
			int itemTypeColumnIndex = cursor.getColumnIndexOrThrow(D3Item.TYPE_COLUMN);
			int itemIDColumnIndex = cursor.getColumnIndexOrThrow(D3Item.ITEM_ID_COLUMN);
			int itemNameColumnIndex = cursor.getColumnIndexOrThrow(D3Item.ITEM_NAME_COLUMN);
			int itemIconColumnIndex = cursor.getColumnIndexOrThrow(D3Item.ITEM_ICON_COLUMN);
			int itemColorColumnIndex = cursor.getColumnIndexOrThrow(D3Item.ITEM_DISPLAY_COLOR_COLUMN);
			int itemTooltipParamsColumnIndex = cursor.getColumnIndexOrThrow(D3Item.ITEM_TOOLTIP_PARAMS_COLUMN);
			int itemGemsParamsColumnIndex = cursor.getColumnIndexOrThrow(D3Item.ITEM_GEMS_COLUMN);
			D3Item item = new D3Item();
			item.setItemID(cursor.getString(itemIDColumnIndex));
			item.setName(cursor.getString(itemNameColumnIndex));
			item.setIcon(cursor.getString(itemIconColumnIndex));
			item.setDisplayColor(cursor.getString(itemColorColumnIndex));
			item.setToolTipParams(cursor.getString(itemTooltipParamsColumnIndex));
			item.setGemsString(cursor.getString(itemGemsParamsColumnIndex));
			itemsMap.put(cursor.getString(itemTypeColumnIndex), item);
		}
		cursor.close();
		return itemsMap;
	}

	private synchronized HeroProgressionModel getHeroProgression(SQLiteDatabase database, long heroID) {
		final String tableName = HeroModelDecorator.HERO_PROGRESSION_TABLE_NAME;
		Cursor progressionCursor = database.rawQuery("SELECT * FROM " 
				+ tableName + " WHERE " 
				+ HeroModel.HERO_ID_COLUMN + "=" + heroID, null);
		return getHeroProgression(progressionCursor);
	}

	private Map<String, Number> getHeroKills(Cursor cursor) {
		return buildMap(cursor, MapDataType.Kills);
	}
	
	private synchronized Map<String, Number> buildMap(Cursor cursor, MapDataType type){
		final Map<String, Number> map = new LinkedHashMap<String, Number>();
		int keysColumnIndex = -1;
		int valuesColumnIndex = -1;
		String heroKeysString;
		String heroValuesString;
		String[] keysArray;
		String[] valuesArray;
		if(type.name().equalsIgnoreCase(MapDataType.Stats.name())) {
			keysColumnIndex = cursor.getColumnIndexOrThrow(HeroModelDecorator.STATS_KEYS_COLUMN);
			valuesColumnIndex = cursor.getColumnIndexOrThrow(HeroModelDecorator.STATS_VALUES_COLUMN);
		}else if(type.name().equalsIgnoreCase(MapDataType.Kills.name())) {
			
			keysColumnIndex = cursor.getColumnIndexOrThrow(HeroModelDecorator.HERO_KILLS_KEYS_COLUMN);
			valuesColumnIndex = cursor.getColumnIndexOrThrow(HeroModelDecorator.HERO_KILLS_VALUES_COLUMN);
		}
		
		heroKeysString = cursor.getString(keysColumnIndex);
		heroValuesString = cursor.getString(valuesColumnIndex);
		keysArray = heroKeysString.split(",");
		valuesArray = heroValuesString.split(",");
		
		for(int i = 0; i < keysArray.length; i++) {
			if(keysArray[i]!= null && keysArray[i].length() > 0){
				if(type.name().equalsIgnoreCase(MapDataType.Kills.name())){
					map.put(keysArray[i], Integer.valueOf(valuesArray[i]));	
				}else if(type.name().equalsIgnoreCase(MapDataType.Stats.name())){
					map.put(keysArray[i], Double.valueOf(valuesArray[i]));
				}
			}
		}
		return map;
	}

	private Map<String, Number> getHeroStats(Cursor cursor) {
		return buildMap(cursor, MapDataType.Stats);
	}

	private HeroModel buildBasiHeroModel(Cursor cursor) {
		final HeroModel heroModel = new HeroModel();
		
		int heroIDColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_ID_COLUMN);
		int heroNameColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_NAME_COLUMN);
		int heroClassColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_CLASS_COLUMN);
		int heroGenderColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_GENDER_COLUMN);
		int heroHardcoreColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_HARDCORE_COLUMN);
		int heroLevelColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_LEVEL_COLUMN);
		int heroParagonColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_PARAGON_LEVEL_COLUMN);
		int heroLastUpdatedColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_LAST_UPDATED_COLUMN);
		int heroIsDeadColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_DEAD_COLUMN);
		int serverColumn = cursor.getColumnIndexOrThrow(HeroModel.SERVER_COLUMN);
		
		heroModel.setID(cursor.getLong(heroIDColumnIndex));
		heroModel.setName(cursor.getString(heroNameColumnIndex));
		heroModel.setclass(cursor.getString(heroClassColumnIndex));
		heroModel.setServer(cursor.getString(serverColumn).toLowerCase(new Locale("en-US")));
		HeroModel.Gender gender =  (cursor.getString(heroGenderColumnIndex).equalsIgnoreCase("female"))?HeroModel.Gender.female:HeroModel.Gender.male;
		heroModel.setGender(gender);
		heroModel.setHardcore((cursor.getInt(heroHardcoreColumnIndex) == 0)?false:true);
		heroModel.setLevel(cursor.getInt(heroLevelColumnIndex));
		heroModel.setParagonLevel(cursor.getInt(heroParagonColumnIndex));
		heroModel.setLastUpdated(cursor.getLong(heroLastUpdatedColumnIndex));
		heroModel.setDead((cursor.getInt(heroIsDeadColumnIndex) == 0)?false:true);
		heroModel.setPortrait(ProfileModel.HERO_PORTRAIT_URL + Utils.removeHiphen(heroModel.getclass()) + "_" 
				+ heroModel.getGender().name() + ".png");
		return heroModel;
	}

	@Override
	public boolean insertData(List<IProfileModel> items, SQLiteDatabase database) {
		// TODO Auto-generated method stub
		return false;
	}
}
