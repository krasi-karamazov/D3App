package kpk.dev.d3app.models.accountmodels;


import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;


public class D3PassiveSkill implements IProfileModel {

	public static final String TABLE_NAME = "passive_skills";
	public static final String SLUG_COLUMN = "passive_skill_slug";
	public static final String NAME_COLUMN = "passive_skill_name";
	public static final String ICON_COLUMN = "passive_skill_icon";
	public static final String TOOLTIP_URL_COLUMN = "passive_skill_tooltip_url";
	public static final String DESCRIPTION_COLUMN = "passive_skill_description";
	public static final String FLAVOR_COLUMN = "passive_skill_flavor";
	public static final String SKILLCALC_ID_COLUMN = "passive_skillcalc_id";
	public static final String LEVEL_COLUMN = "passive_skill_level";
	
	private long mHeroID;
	private String mSlug;
	private String mName;
	private String mIcon;
	private int mLevel;
	private String mCategorySlug;
	private String mToolTipURL;
	private String mDescription;
	private String mSkillCalcId;
	private String mFlavor;
	private D3PassiveSkill mSkill;
	public D3PassiveSkill() {}
	
	@JsonSetter("skill")
	public void setskill(D3PassiveSkill skill) {mSkill = skill;}
	@JsonGetter("skill")
	public D3PassiveSkill getskill() {return mSkill;}
	
	public void setHeroID(long id) {
		mHeroID = id;
	}
	
	public long getHeroID(){
		return mHeroID;
	}
	
	public void setFlavor(String flavor) {
		mFlavor = flavor;
	}
	
	public String getFlavor(){
		return mFlavor;
	}
	
	public void setSlug(String slug) {
		mSlug = slug;
	}
	
	public String getSlug(){
		return mSlug;
	}
	
	public void setName(String name) {
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
	
	public ContentValues getPassiveSkillContentValues(long heroID){
		ContentValues contentValues = getContentValues();
		//contentValues.put(HeroModel.HERO_ID_COLUMN, heroID);
		
		return contentValues;
	}

	@Override
	public ContentValues getContentValues() {
		ContentValues contentValues = new ContentValues();
		if(mSkill == null) return null;
		contentValues.put(D3PassiveSkill.SLUG_COLUMN, mSkill.getSlug());
		contentValues.put(D3PassiveSkill.NAME_COLUMN, mSkill.getName());
		contentValues.put(D3PassiveSkill.ICON_COLUMN, mSkill.getIcon());
		contentValues.put(D3PassiveSkill.TOOLTIP_URL_COLUMN, mSkill.getTooltipURL());
		contentValues.put(D3PassiveSkill.DESCRIPTION_COLUMN, mSkill.getDescription());
		contentValues.put(D3PassiveSkill.FLAVOR_COLUMN, mSkill.getFlavor());
		contentValues.put(D3PassiveSkill.SKILLCALC_ID_COLUMN, mSkill.getScillCalcId());
		contentValues.put(D3PassiveSkill.LEVEL_COLUMN, mSkill.getLevel());
		return contentValues;
	}

}
