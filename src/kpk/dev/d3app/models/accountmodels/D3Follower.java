package kpk.dev.d3app.models.accountmodels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import kpk.dev.d3app.util.KPKLog;

import android.content.ContentValues;

public class D3Follower implements IProfileModel {
	public static final String TABLE_NAME = "d3followers";
	public static final String FOLLOWER_STATS_TABLE_NAME = "d3followers_stats";
	public static final String FOLLOWER_SLUG_COLUMN = "follower_slug";
	public static final String FOLLOWER_LEVEL_COLUMN = "follower_level";
	private long mHeroID;
	private String mSlug;
	private int mLevel;
	private List<D3FollowerSkill> mSkills;
	private Map<String, D3Item> mItems;
	private Map<String, Double> mStats;
	public D3Follower(){}
	
	public void setStats(Map<String, Double> stats) {
		this.mStats = stats;
	}
	
	public Map<String, Double> getStats() {
		return this.mStats;
	}
	
	@JsonAnySetter
	public void handleUnknown(String key, Object value) {
	    if(key.equalsIgnoreCase("skills")) {
	    	mSkills = new ArrayList<D3FollowerSkill>();
	    	ArrayList<?> val = (ArrayList<?>)value;
	    	for(int i = 0; i < val.size(); i++) {
	    		LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>)val.get(i);
	    		Set<String> keySet = (Set<String>)map.keySet();
	    		for(String mapKey : keySet) {
	    			LinkedHashMap<?, ?> skillMap = (LinkedHashMap<?, ?>)map.get(mapKey);
	    			D3FollowerSkill skill = new D3FollowerSkill();
	    			skill.setSlug(skillMap.get("slug").toString());
	    			skill.setName(skillMap.get("name").toString());
	    			skill.setIcon(skillMap.get("icon").toString());
	    			skill.setLevel(Integer.valueOf(skillMap.get("level").toString()));
	    			skill.setTooltipURL(skillMap.get("tooltipUrl").toString());
	    			skill.setDescription(skillMap.get("description").toString());
	    			skill.setSimpleDescription(skillMap.get("simpleDescription").toString());
	    			skill.setScillCalcId(skillMap.get("skillCalcId").toString());
	    			mSkills.add(skill);
	    		}
	    	}
	    }
	}
	
	public List<D3FollowerSkill> getD3FollowerSkills(){
		return mSkills;
	}
	
	public void setD3FollowerSkills(List<D3FollowerSkill> skills){
		mSkills = skills;
	}
	
	public void setItems(Map<String, D3Item> items) {
		mItems = items;
	}
	
	public Map<String, D3Item> getItems() {
		return mItems ;
	}
	
	
	public void setHeroID(long id) {
		mHeroID = id;
	}
	
	public long getHeroID() {
		return mHeroID;
	}
	
	public void setSlug(String slug) {
		mSlug = slug;
	}
	
	public String getSlug(){
		return mSlug;
	}
	
	public void setLevel(int level) {
		mLevel = level;
	}
	
	public int getLevel(){
		return mLevel;
	}
	
	@Override
	public void setServer(String server) {}

	@Override
	public void setParentProfileTag(String profileTag) {}

	@Override
	public String getParentProfileTag() {return null;}

	@Override
	public String getTableName() {return TABLE_NAME;}
	
	@Override
	public ContentValues getContentValues() {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(HeroModel.HERO_ID_COLUMN, mHeroID);
		contentValues.put(FOLLOWER_SLUG_COLUMN, mSlug);
		contentValues.put(FOLLOWER_LEVEL_COLUMN, mLevel);
		return contentValues;
	}
	
	public ContentValues getStatsContentValues() {
		final ContentValues contentValues = getContentValues();
		contentValues.remove(FOLLOWER_LEVEL_COLUMN);
		List<String> statsStrings = getStatsStrings();
		contentValues.put(HeroModelDecorator.STATS_KEYS_COLUMN, statsStrings.get(0));
		contentValues.put(HeroModelDecorator.STATS_VALUES_COLUMN, statsStrings.get(1));
		return contentValues;
	}
	
	public List<ContentValues> getSkillsContentValues() {
		final List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
		contentValuesList.addAll(new SkillsModel().getFollowerSkillsContentValues(mHeroID, mSlug, mSkills));
		return contentValuesList;
	}
	
	
	
	public List<ContentValues> getItemsContentValues(){
		final List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
		final Set<Entry<String, D3Item>> itemEntrySet = mItems.entrySet();
		final Iterator<Entry<String, D3Item>> iterator = itemEntrySet.iterator();
		while(iterator.hasNext()){
			Entry<String, D3Item> entry = iterator.next();
			ContentValues values = getContentValues();
			values.remove(FOLLOWER_LEVEL_COLUMN);
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

	private List<String> getStatsStrings() {
		final List<String> statsStrings = new ArrayList<String>();
		final Set<Entry<String, Double>> statsEntries = mStats.entrySet();
		final Iterator<Entry<String, Double>> entryIterator = statsEntries.iterator();
		final StringBuilder keys = new StringBuilder();
		final StringBuilder values = new StringBuilder();
		while(entryIterator.hasNext()) {
			Entry<String, Double> entry = entryIterator.next();
			keys.append(entry.getKey() + ",");
			values.append(entry.getValue() + ",");
		}
		statsStrings.add(keys.toString());
		statsStrings.add(values.toString());
		return statsStrings;
	}
}
