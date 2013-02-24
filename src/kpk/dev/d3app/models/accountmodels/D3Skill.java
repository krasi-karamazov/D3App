package kpk.dev.d3app.models.accountmodels;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import android.content.ContentValues;

public class D3Skill implements IProfileModel {

	public static final String TABLE_NAME = "active_skills";
	public static final String FOLLOWER_SKILLS_TABLE_NAME = "followers_skills";
	public static final String SLUG_COLUMN = "skill_slug";
	public static final String FOLLOWER_SLUG_COLUMN = "follower_slug";
	public static final String NAME_COLUMN = "skill_name";
	public static final String ICON_COLUMN = "skill_icon";
	public static final String LEVEL_COLUMN = "skill_level";
	public static final String CATEGORY_SLUG_COLUMN = "skill_category_slug";
	public static final String TOOLTIP_URL_COLUMN = "skill_tooltip_url";
	public static final String DESCRIPTION_COLUMN = "skill_description";
	public static final String SIMPLE_DESCRIPTION_COLUMN = "skill_simple_description";
	public static final String SKILLCALC_ID_COLUMN = "skill_skillcalc_id";
	
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
	private D3Rune mRune;
	private D3Skill mSkill;
	public D3Skill() {}
	@JsonSetter("skill")
	public void setSkill(D3Skill skill) {
		mSkill = skill;
	}
	@JsonGetter("skill")
	public D3Skill getSkill() {return mSkill;}
	
	public void setRune(D3Rune rune) {
		mRune = rune;
	}
	
	public D3Rune getRune() {
		return mRune;
	}
	
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
	
	public void setCategorySlug(String slug) {
		mCategorySlug = slug;
	}
	
	public String getCategorySlug(){
		return mCategorySlug;
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
	public void setServer(String server) {}

	@Override
	public void setParentProfileTag(String profileTag) {}

	@Override
	public String getParentProfileTag() {return null;}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ContentValues getSkillContentValues(String followerSlug, long heroID){
		ContentValues contentValues = getContentValues();
		
		if(followerSlug != null){
			contentValues.put(D3Skill.FOLLOWER_SLUG_COLUMN, followerSlug);
		}
		return contentValues;
	}
	
	@Override
	public ContentValues getContentValues() {
		ContentValues contentValues = new ContentValues();
		if(mSkill == null) return null;
		contentValues.put(D3Skill.SLUG_COLUMN, mSkill.getSlug());
		contentValues.put(D3Skill.NAME_COLUMN, mSkill.getName());
		contentValues.put(D3Skill.ICON_COLUMN, mSkill.getIcon());
		contentValues.put(D3Skill.LEVEL_COLUMN, mSkill.getLevel());
		contentValues.put(D3Skill.CATEGORY_SLUG_COLUMN, mSkill.getCategorySlug());
		contentValues.put(D3Skill.TOOLTIP_URL_COLUMN, mSkill.getTooltipURL());
		contentValues.put(D3Skill.DESCRIPTION_COLUMN, mSkill.getDescription());
		contentValues.put(D3Skill.SIMPLE_DESCRIPTION_COLUMN, mSkill.getSimpleDescription());
		contentValues.put(D3Skill.SKILLCALC_ID_COLUMN, mSkill.getScillCalcId());
		return contentValues;
	}
}
