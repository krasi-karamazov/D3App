package kpk.dev.d3app.models.accountmodels;
import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;


public class D3FollowerSkill implements IProfileModel {
	private long mHeroID;
	private String mSlug;
	private String mName;
	private String mIcon;
	private int mLevel;
	private String mCategorySlug;
	private String mToolTipURL;
	private String mDescription;
	private String mSimpleDescription;
	private String mSkillCalcId;
	private D3FollowerSkill mSkill;
	public D3FollowerSkill() {
	}
	
	@JsonSetter("skill")
	public void setSkill(D3FollowerSkill skill) {
		mSkill = skill;
	}
	@JsonGetter("skill")
	public D3FollowerSkill getSkill() {return mSkill;}
	
	public void setHeroID(long id) {
		mHeroID = id;
	}
	
	public long getHeroID(){
		return mHeroID;
	}
	
	public void setSlug(String slug) {
		mSlug = slug;
	}
	
	public String getSlug(){
		return mSlug;
	}
	
	public void setName(String name) {
		//KPKLog.d("NAME " + name);
		mName = name;
	}
	
	public String getName(){
		return mName;
	}
	
	public void setIcon(String icon) {
		mIcon = icon;
	}
	
	public String getIcon(){
		return mIcon;
	}
	
	public void setLevel(int level) {
		mLevel = level;
	}
	
	public int getLevel(){
		return mLevel;
	}
	
	@JsonSetter("tooltipUrl")
	public void setTooltipURL(String tooltipURL) {
		mToolTipURL = tooltipURL;
	}
	
	@JsonGetter("tooltipUrl")
	public String getTooltipURL(){
		return mToolTipURL;
	}
	
	public void setDescription(String description) {
		mDescription = description;
	}
	
	public String getDescription(){
		return mDescription;
	}
	
	public void setSimpleDescription(String description) {
		mSimpleDescription = description;
	}
	
	public String getSimpleDescription(){
		return mSimpleDescription;
	}
	
	@JsonSetter("skillCalcId")
	public void setScillCalcId(String id) {
		mSkillCalcId = id;
	}
	
	@JsonGetter("skillCalcId")
	public String getScillCalcId(){
		return mSkillCalcId;
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
	
	public ContentValues getSkillContentValues(String followerSlug, long heroID){
		ContentValues contentValues = getContentValues();
		contentValues.put(HeroModel.HERO_ID_COLUMN, heroID);
		if(followerSlug != null){
			contentValues.put(D3Skill.FOLLOWER_SLUG_COLUMN, followerSlug);
		}
		return contentValues;
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(D3Skill.SLUG_COLUMN, mSlug);
		contentValues.put(D3Skill.NAME_COLUMN, mName);
		contentValues.put(D3Skill.ICON_COLUMN, mIcon);
		contentValues.put(D3Skill.LEVEL_COLUMN, mLevel);
		contentValues.put(D3Skill.CATEGORY_SLUG_COLUMN, mCategorySlug);
		contentValues.put(D3Skill.TOOLTIP_URL_COLUMN, mToolTipURL);
		contentValues.put(D3Skill.DESCRIPTION_COLUMN, mDescription);
		contentValues.put(D3Skill.SIMPLE_DESCRIPTION_COLUMN, mSimpleDescription);
		contentValues.put(D3Skill.SKILLCALC_ID_COLUMN, mSkillCalcId);
		return contentValues;
	}

}
