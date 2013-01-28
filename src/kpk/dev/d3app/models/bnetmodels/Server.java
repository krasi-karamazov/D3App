package kpk.dev.d3app.models.bnetmodels;

import android.content.ContentValues;

public class Server extends BaseBattleNetModel {
	private String mIsAvailable;
	private String mRegion;
	public static String NAME_COLUMN = "server_name";
	public static String AVAILABLE_COLUMN = "server_available";
	public static String REGION_COLUMN = "region_name";
	public static String TABLE_NAME = "servers_table";
	public Server(String name, String isAvailable, String region) {
		super(name);
		this.mIsAvailable = isAvailable;
		this.mRegion = region;
	}
	
	public Server() {}
	
	public void setIsAvailable(String isAvailable) {
		this.mIsAvailable = isAvailable;
	}
	
	public String getIsAvailable() {
		return this.mIsAvailable;
	}
	
	public void setRegion(String region) {
		this.mRegion = region;
	}
	
	public String getRegion() {
		return this.mRegion;
	}
	
	@Override
	protected void formContentValues(ContentValues contentValues) {
		contentValues.put(NAME_COLUMN, mName);
		contentValues.put(AVAILABLE_COLUMN, (mIsAvailable.equalsIgnoreCase("available")?1:0));
		contentValues.put(REGION_COLUMN, mRegion);
	}

	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public boolean isRegion() {
		return false;
	}
}
