package kpk.dev.d3app.models.accountmodels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import android.content.ContentValues;

public class HeroModelDecorator implements IProfileModel {
	public static final String TABLE_NAME = "D3HeroFullTable";
	public static final String HERO_ID_COLUMN = "hero_id";
	public static final String HERO_NAME_COLUMN = "hero_name";
	public static final String HERO_LEVEL_COLUMN = "hero_level";
	public static final String HERO_HARDCORE_COLUMN = "hero_is_hardcore";
	public static final String HERO_PARAGON_LEVEL_COLUMN = "hero_paragon_level";
	public static final String HERO_GENDER_COLUMN = "hero_gender";
	public static final String HERO_DEAD_COLUMN = "hero_is_dead";
	public static final String HERO_CLASS_COLUMN = "hero_class";
	public static final String HERO_LAST_UPDATED_COLUMN = "hero_last_updated";
	
	public static final String HERO_STATS_TABLE_NAME = "hero_stats";
	public static final String STATS_KEYS_COLUMN = "stats_keys";
	public static final String STATS_VALUES_COLUMN = "stats_values";
	
	public static final String HERO_KILLS_TABLE_NAME = "hero_kills";
	public static final String HERO_KILLS_VALUES_COLUMN = "kills_values";
	public static final String HERO_KILLS_KEYS_COLUMN = "kills_keys";
	
	public static final String HERO_PROGRESSION_TABLE_NAME = "hero_progress";
	
	private Map<String, Number> mStats;
	private SkillsModel mSkills;
	private Map<String, D3Follower> mFollowers;
	private Map<String, Number> mKills;
	private HeroProgressionModel mProgress;
	private HeroModel mHeroModel;
	private Map<String, D3Item> mItems;
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public HeroModelDecorator(HeroModel heroModel){
		mHeroModel = heroModel;
	}
	
	@JsonAnySetter
	public void handleUnknown(String key, Object value) {
	    //nothing to see here, just ignoring some props
	}
	
	public HeroModelDecorator(){
		
	}
	
	public void setItems(Map<String, D3Item> items){
		mItems = items;
	}
	
	public Map<String, D3Item> getItems() {
		return mItems;
	}
	
	public long getHeroID() {
		if(mHeroModel != null) {
			return mHeroModel.getID();
		}else{
			return -1;
		}
	}
	
	public void setHeroModel(HeroModel heroModel) {
		mHeroModel = heroModel;
	}
	
	public HeroModel getHeroModel() {
		return mHeroModel;
	}
	
	@JsonSetter("progress")
	public void setHeroProgress(HeroProgressionModel progress) {
		mProgress = progress;
		System.out.println(mProgress.getHell().getActsCompletion());
	}
	
	@JsonGetter("progress")
	public HeroProgressionModel getHeroProgress() {
		System.out.println(mProgress);
		return mProgress;
	}
	
	public void setKills(Map<String, Number> kills) {
		mKills = kills;
	}
	
	public Map<String, Number> getKills() {
		return mKills;
	}
	
	public void setStats(Map<String, Number> stats) {
		this.mStats = stats;
	}
	
	public Map<String, Number> getStats() {
		return this.mStats;
	}
	
	public void setSkills(SkillsModel skills) {
		mSkills = skills;
	}
	
	public SkillsModel getSkills() {
		return mSkills;
	}
	
	@JsonSetter("followers")
	public void setFollowersModels(Map<String, D3Follower> followers) {	
		mFollowers = followers;
	}
	@JsonGetter("followers")
	public Map<String, D3Follower> getFollowersModels() {
		return mFollowers;
	}
	
	@Override
	public void setServer(String server) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setParentProfileTag(String profileTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getParentProfileTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentValues getContentValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<ContentValues> getActiveSkillsContentValues() {
		return mSkills.getActiveSkillsContentValues(mHeroModel.getID(), null, mSkills.getActive());
	}
	
	public List<ContentValues> getRunesContentValues() {
		return mSkills.getRunesContentValues(mHeroModel.getID());
	}
	
	public List<ContentValues> getPassiveSkillsContentValues() {
		return mSkills.getPassiveSkillsValues(mHeroModel.getID());
	}
	
	public ContentValues getKillsContentValues() {
		final ContentValues contentValues = new ContentValues();
		
		final StringBuilder keysString = new StringBuilder();
		final StringBuilder valuesString = new StringBuilder();
		final Set<Entry<String, Number>> entrySet = mKills.entrySet();
		Iterator<Entry<String, Number>> entryIterator = entrySet.iterator();
		
		while(entryIterator.hasNext()){
			Entry<String, Number> entry = entryIterator.next();
			keysString.append(entry.getKey() + ",");
			valuesString.append(entry.getValue() + ",");
		}
		contentValues.put(HeroModel.HERO_ID_COLUMN, mHeroModel.getID());
		contentValues.put(HERO_KILLS_KEYS_COLUMN, keysString.toString());
		contentValues.put(HERO_KILLS_VALUES_COLUMN, valuesString.toString());
		
		return contentValues;
	}
	
	public List<ContentValues> getHeroItemsContentValues(){
		final List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
		final Set<Entry<String, D3Item>> itemEntrySet = mItems.entrySet();
		final Iterator<Entry<String, D3Item>> iterator = itemEntrySet.iterator();
		while(iterator.hasNext()){
			Entry<String, D3Item> entry = iterator.next();
			ContentValues values = new ContentValues();
			values.put(HeroModel.HERO_ID_COLUMN, getHeroID());
			values.put(D3Item.TYPE_COLUMN, entry.getKey());
			values.put(D3Item.ITEM_ID_COLUMN, entry.getValue().getItemID());
			values.put(D3Item.ITEM_NAME_COLUMN, entry.getValue().getName());
			values.put(D3Item.ITEM_ICON_COLUMN, entry.getValue().getIcon());
			values.put(D3Item.ITEM_DISPLAY_COLOR_COLUMN, entry.getValue().getDisplayColor());
			values.put(D3Item.ITEM_TOOLTIP_PARAMS_COLUMN, entry.getValue().getToolTipParams());
			values.put(D3Item.ITEM_GEMS_COLUMN, entry.getValue().getGemsString());
			contentValuesList.add(values);
		}
		return contentValuesList;
	}
	
	public ContentValues getStatsContentValues(){
		final ContentValues contentValues = new ContentValues();
		
		final StringBuilder keysString = new StringBuilder();
		final StringBuilder valuesString = new StringBuilder();
		final Set<Entry<String, Number>> entrySet = mStats.entrySet();
		Iterator<Entry<String, Number>> entryIterator = entrySet.iterator();
		
		while(entryIterator.hasNext()){
			Entry<String, Number> entry = entryIterator.next();
			keysString.append(entry.getKey() + ",");
			valuesString.append(entry.getValue() + ",");
		}
		contentValues.put(HeroModel.HERO_ID_COLUMN, mHeroModel.getID());
		contentValues.put(STATS_KEYS_COLUMN, keysString.toString());
		contentValues.put(STATS_VALUES_COLUMN, valuesString.toString());
		
		return contentValues;
	}
	
	public List<Boolean> getProgressionBooleanList() {
		final List<Boolean> completionList = new ArrayList<Boolean>();
		
		completionList.addAll(getHeroProgress().getNormal().getActsCompletion());
		completionList.addAll(getHeroProgress().getNightmare().getActsCompletion());
		completionList.addAll(getHeroProgress().getHell().getActsCompletion());
		completionList.addAll(getHeroProgress().getInferno().getActsCompletion());
		
		return completionList;
	}
}
