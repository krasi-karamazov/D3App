package kpk.dev.d3app.models.accountmodels;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;


public class D3Rune implements IProfileModel {

	public static final String TABLE_NAME = "skill_runes";
	public static final String SKILL_SLUG_COLUMN = "skill_slug";
	public static final String SLUG_COLUMN = "rune_slug";
	public static final String TYPE_COLUMN = "rune_type";
	public static final String NAME_COLUMN = "rune_name";
	public static final String LEVEL_COLUMN = "rune_level";
	public static final String DESCRIPTION_COLUMN = "rune_description";
	public static final String SIMPLE_DESCRIPTION_COLUMN = "rune_simple_description";
	public static final String TOOLTIP_PARAMS_COLUMN = "rune_tooltip_params";
	public static final String SKILLCALC_ID_COLUMN = "rune_skillcalc_id";
	public static final String ORDER_COLUMN = "rune_order";
	
	private String mSlug;
	private String mSkillSlug;
	private String mType;
	private String mName;
	private String mIcon;
	private int mLevel;
	private String mCategorySlug;
	private String mToolTipParams;
	private String mDescription;
	private String mSimpleDescription;
	private String mSkillCalcId;
	private int mOrder;
	
	public D3Rune() {}
	
	public void setSkillSlug(String slug){
		mSkillSlug = slug;
	}
	
	public String getSkillSlug() {
		return mSkillSlug;
	}
	
	public void setOrder(int order) {
		mOrder = order;
	}
	
	public int getOrder(){
		return mOrder;
	}
	
	public String getParentSkill() {
		return mSlug.substring(0, mSlug.lastIndexOf((char)45));
	}
	
	public void setSlug(String slug) {
		mSlug = slug;
	}
	
	public String getSlug(){
		return mSlug;
	}
	
	public void setType(String type) {
		mType = type;
	}
	
	public String getType(){
		return mType;
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
	
	public void setTooltipParams(String tooltipParams) {
		mToolTipParams = tooltipParams;
	}
	
	public String getTooltipParams(){
		return mToolTipParams;
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
	
	public ContentValues getRuneContentValues(long heroID, String skillSlug){
		ContentValues contentValues = getContentValues();
		contentValues.put(HeroModel.HERO_ID_COLUMN, heroID);
		contentValues.put(D3Rune.SKILL_SLUG_COLUMN, skillSlug);
		return contentValues;
	}

	@Override
	public ContentValues getContentValues() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(D3Rune.SLUG_COLUMN, mSlug);
		contentValues.put(D3Rune.TYPE_COLUMN, mType);
		contentValues.put(D3Rune.NAME_COLUMN, mName);
		contentValues.put(D3Rune.LEVEL_COLUMN, mName);
		contentValues.put(D3Rune.DESCRIPTION_COLUMN, mDescription);
		contentValues.put(D3Rune.SIMPLE_DESCRIPTION_COLUMN, mSimpleDescription);
		contentValues.put(D3Rune.TOOLTIP_PARAMS_COLUMN, mToolTipParams);
		contentValues.put(D3Rune.SKILLCALC_ID_COLUMN, mSkillCalcId);
		contentValues.put(D3Rune.ORDER_COLUMN, mOrder);
		return contentValues;
	}

}
