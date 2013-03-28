package kpk.dev.d3app.models.accountmodels;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import android.content.ContentValues;

public class ArtisanModel implements IProfileModel {
	public static final String TABLE_NAME = "d3artisantable";
	public static final String TABLE_NAME_HARDCORE = "d3hardcoreartisantable";
	public static final String SLUG_COLUMN = "artisan_slug";
	public static final String LEVEL_COLUMN = "artisan_level";
	public static final String STEP_CURRENT_COLUMN = "artisan_step_current";
	public static final String STEP_MAX_COLUMN = "artisan_step_max";
	private String mSlug;
	private int mLevel;
	private int mStepCurrent;
	private int mStepMax;
	private String mParentProfileTag;
	private String mServer;
	public ArtisanModel(){
		
	}
	
	public void setParentProfileTag(String profileTag) {
		this.mParentProfileTag = profileTag;
	}
	
	public String getParentProfileTag() {
		return this.mParentProfileTag;
	}
	
	public void setSlug(String slug) {
		this.mSlug = slug;
	}
	
	public String getSlug(){
		return this.mSlug;
	}
	
	public void setLevel(int level) {
		this.mLevel = level;
	}
	
	public int getLevel() {
		return this.mLevel;
	}
	
	public void setStepCurrent(int stepCurrent) {
		this.mStepCurrent = stepCurrent;
	}
	
	public int getStepCurrent() {
		return this.mStepCurrent;
	}
	
	public void setStepMax(int stepMax) {
		this.mStepMax = stepMax;
	}
	
	public int getStepMax() {
		return this.mStepMax;
	}

	@Override
	public void setServer(String server) {
		mServer = server;
	}
	
	@Override
	public ContentValues getContentValues() {
		final ContentValues artisanContentvalues = new ContentValues();
		artisanContentvalues.put(SLUG_COLUMN, mSlug);
		artisanContentvalues.put(LEVEL_COLUMN, mLevel);
		artisanContentvalues.put(STEP_CURRENT_COLUMN, mStepCurrent);
		artisanContentvalues.put(STEP_MAX_COLUMN, mStepMax);
		artisanContentvalues.put(IProfileModel.PROFILE_TAG_COLUMN, mParentProfileTag);
		artisanContentvalues.put(IProfileModel.SERVER_COLUMN, mServer);
		return artisanContentvalues;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
}
