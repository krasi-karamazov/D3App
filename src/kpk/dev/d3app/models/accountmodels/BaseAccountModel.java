package kpk.dev.d3app.models.accountmodels;

import android.content.ContentValues;

public abstract class BaseAccountModel {
	public static String ID_COLUMN = "_id";
	protected abstract void formContentValues(ContentValues contentValues);
}
