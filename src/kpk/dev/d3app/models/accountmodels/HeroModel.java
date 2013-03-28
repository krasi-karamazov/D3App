package kpk.dev.d3app.models.accountmodels;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import android.content.ContentValues;

public class HeroModel implements IProfileModel {
	public static final String TABLE_NAME = "D3HeroSummaryTable";
	public static final String HERO_ID_COLUMN = "hero_id";
	public static final String HERO_NAME_COLUMN = "hero_name";
	public static final String HERO_LEVEL_COLUMN = "hero_level";
	public static final String HERO_HARDCORE_COLUMN = "hero_is_hardcore";
	public static final String HERO_PARAGON_LEVEL_COLUMN = "hero_paragon_level";
	public static final String HERO_GENDER_COLUMN = "hero_gender";
	public static final String HERO_DEAD_COLUMN = "hero_is_dead";
	public static final String HERO_CLASS_COLUMN = "hero_class";
	public static final String HERO_LAST_UPDATED_COLUMN = "hero_last_updated";
	public enum Gender{
		male, 
		female;
	};
	private String mName;
	private long mID;
	private int mLevel;
	private boolean mHardcore;
	private int mParagonLevel;
	private Gender mGender;
	private boolean mDead;
	private String mClass;
	private long lastUpdated;
	private String mParentProfileTag;
	private String mServer;
	private String mPortraitImage;
	
	@JsonAnySetter
	public void handleUnknown(String key, Object value) {
	    // do something: put to a Map; log a warning, whatever
	}
	
	public void setPortrait(String name) {
		this.mPortraitImage = name;
	}
	
	public String getPortrait() {
		return this.mPortraitImage;
	}
	
	public void setName(String name) {
		this.mName = name;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public void setParentProfileTag(String profileTag) {
		this.mParentProfileTag = profileTag;
	}
	
	public String getParentProfileTag() {
		return this.mParentProfileTag;
	}
	
	public void setclass(String heroClass) {
		this.mClass = heroClass;
	}
	
	public String getclass() {
		return this.mClass;
	}
	
	public void setID(long _id){
		this.mID = _id;
	}
	
	public long getID() {
		return this.mID;
	}
	
	public void setLevel(int level){
		this.mLevel = level;
	}
	
	public int getLevel() {
		return this.mLevel;
	}
	
	public void setHardcore(boolean hardcore){
		this.mHardcore = hardcore;
	}
	
	public boolean getHandrdcore() {
		return this.mHardcore;
	}
	
	
	public void setParagonLevel(int level){
		this.mParagonLevel = level;
	}
	
	public int getParagonLevel() {
		return this.mParagonLevel;
	}
	
	public void setGender(Gender gender){
		this.mGender = gender;
	}
	
	public Gender getGender() {
		return this.mGender;
	}
	
	public void setDead(boolean isdead){
		this.mDead = isdead;
	}
	
	public boolean getDead() {
		return this.mDead;
	}
	
	@JsonSetter("last-updated")
	public void setLastUpdated(long lastUpdated){
		this.lastUpdated = lastUpdated;
	}
	
	@JsonGetter("last-updated")
	public long getLastUpdated() {
		return this.lastUpdated;
	}

	@Override
	public void setServer(String server) {
		mServer = server;
	}
	
	public String getServer() {
		return mServer;
	}
	
	@Override
	public ContentValues getContentValues() {
		final ContentValues heroContentValues = new ContentValues();
		heroContentValues.put(HERO_NAME_COLUMN, mName);
		heroContentValues.put(HERO_ID_COLUMN, mID);
		heroContentValues.put(HERO_CLASS_COLUMN, mClass);
		heroContentValues.put(HERO_GENDER_COLUMN, mGender.name());
		heroContentValues.put(HERO_HARDCORE_COLUMN, mHardcore);
		heroContentValues.put(HERO_LEVEL_COLUMN, mLevel);
		heroContentValues.put(HERO_PARAGON_LEVEL_COLUMN, mParagonLevel);
		heroContentValues.put(HERO_LAST_UPDATED_COLUMN, lastUpdated);
		heroContentValues.put(HERO_DEAD_COLUMN, mDead);
		heroContentValues.put(IProfileModel.PROFILE_TAG_COLUMN, mParentProfileTag);
		heroContentValues.put(IProfileModel.SERVER_COLUMN, mServer);
		return heroContentValues;
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
}
