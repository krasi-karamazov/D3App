package kpk.dev.d3app.models.bnetmodels;

import android.content.ContentValues;

public abstract class BaseBattleNetModel {
	protected String mName;
	public static String ID_COLUMN = "_id";
	protected ContentValues mContentValues;
	public BaseBattleNetModel(String name) {
		mName = name;
	}
	
	public BaseBattleNetModel(){}
	
	public void setName(String name){
		this.mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	public abstract boolean isRegion();
	
	public ContentValues getContentValues() {
		mContentValues = new ContentValues();
		formContentValues(mContentValues);
		return mContentValues;
	}
	
	public abstract String getTableName();
	
	protected abstract void formContentValues(ContentValues contentValues);
}
