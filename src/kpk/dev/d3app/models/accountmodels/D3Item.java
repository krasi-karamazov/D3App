package kpk.dev.d3app.models.accountmodels;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;



//import android.content.ContentValues;

public class D3Item implements IProfileModel {

	public static final String TABLE_NAME = "hero_items";
	public static final String FOLLOWER_ITEMS_TABLE_NAME = "follower_items";
	public static final String TYPE_COLUMN = "item_type";
	public static final String ITEM_ID_COLUMN = "item_id";
	public static final String ITEM_NAME_COLUMN = "item_name";
	public static final String ITEM_ICON_COLUMN = "item_icon";
	public static final String ITEM_DISPLAY_COLOR_COLUMN = "item_display_color";
	public static final String ITEM_TOOLTIP_PARAMS_COLUMN = "item_tooltip_params";
	public static final String ITEM_GEMS_COLUMN = "item_gems_column";
	public static final String FOLLOWER_SLUG_COLUMN = "follower_slug";
	
	private long mHeroID;
	private String mID;
	private String mName;
	private String mIcon;
	private String mDisplayColor;
	private String mToolTipParams;
	private String mGemsString;
	private String mType;
	private int mImageViewId;
	private int[] mGems;
	private int[] mSockets;
	
	public D3Item(){}
	
	public void setSocketsArray(int[] socketsArray) {
		mSockets = socketsArray;
	}
	
	public int[] getSocketsArray() {
		return mSockets;
	}
	
	public void setGemsArray(int[] gemsArray) {
		mGems = gemsArray;
	}
	
	public int[] getGemsArray() {
		return mGems;
	}
	
	public void setImageViewID(int id) {
		mImageViewId = id;
	}
	
	public int getImageViewID() {
		return mImageViewId;
	}
	
	public void setHeroID(long id) {
		mHeroID = id;
	}
	
	public long getHeroID() {
		return mHeroID;
	}
	
	public void setType(String type) {
		mType = type;
	}
	
	public String getType() {
		return mType;
	}
	
	public void setGemsString(String gems) {
		mGemsString = gems;
	}
	
	public String getGemsString() {
		return mGemsString;
	} 
	
	public void setName(String name) {
		mName = name;
	}
	
	public String getName() {
		return mName;
	}
	
	@JsonSetter("id")
	public void setItemID(String id) {
		mID = id;
	}
	
	@JsonGetter("id")
	public String getItemID() {
		return mID;
	}
	
	public void setIcon(String icon) {
		mIcon = icon;
	}
	
	public String getIcon() {
		return mIcon;
	}
	
	public void setDisplayColor(String displayColor) {
		mDisplayColor = displayColor;
	}
	
	public String getDisplayColor() {
		return mDisplayColor;
	}
	
	@JsonSetter("tooltipParams")
	public void setToolTipParams(String toolTipParams) {
		mToolTipParams = toolTipParams;
	}
	
	@JsonGetter("tooltipParams")
	public String getToolTipParams() {
		return mToolTipParams;
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

	@Override
	public ContentValues getContentValues() {
		// TODO Auto-generated method stub
		return null;
	}

}
