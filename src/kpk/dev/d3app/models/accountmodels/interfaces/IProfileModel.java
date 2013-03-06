package kpk.dev.d3app.models.accountmodels.interfaces;

import android.content.ContentValues;

public interface IProfileModel {
	public static final String ID_COLUMN = "_id";
	public static final String SERVER_COLUMN = "server";
	public static final String PROFILE_TAG_COLUMN = "profile_tag";
	void setServer(String server);
	void setParentProfileTag(String profileTag);
	String getParentProfileTag();
	String getTableName();
	ContentValues getContentValues();
}