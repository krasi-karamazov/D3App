package kpk.dev.d3app.util;

import java.util.Map;

import kpk.dev.d3app.R;

import android.os.Environment;

public class D3Constants {
	public static Map<String, Integer> CLASS_RESOURCE_IMAGES;
	public static final String[] regions = {"United States", "Europe", "Asia"};
	public static final String SERVERS_PAGE_URL = "http://us.battle.net/d3/en/status";
	public static final String DATABASE_NAME = "D3AppDatabase";
	public static final String SHARED_PREFERENCES_FILE = "d3_shared_prefs";
	public static final int DATABASE_VERSION = 1;
	public static final int PROGRESS_INCREMENT = 10;
	public static final String FIRST_LOGIN_COMPLETE_KEY = "first_login";
	public static final String[] mModes = {"normal", "nightmare", "hell", "inferno"};
	private static String APP_FOLDER = ".THEd3app";
	public static final String BLACKSMITH_PORTRAIT_URL = "http://media.blizzard.com/d3/icons/portraits/64/blacksmith.png";
	public static final String JEWELER_PORTRAIT_URL = "http://media.blizzard.com/d3/icons/portraits/64/jeweler.png";
	public static final String LARGE_ITEM_ICON_URL = "http://media.blizzard.com/d3/icons/items/large/";
	public static final String ITEM_PARAM_URL = ".battle.net/d3/en/tooltip/";
	public static final String SKILL_TOOL_TIP_URL = ".battle.net/d3/en/tooltip/skill/";
	public static final String SKILL_ICON_URL = "http://media.blizzard.com/d3/icons/skills/64/";
	public static String getExternalImageDirectory(){
		return Environment.getExternalStorageDirectory().getPath() + "/" + APP_FOLDER + "/images/";
	}
	public static final String[] ITEMS_TYPES = {"head", "shoulders", "torso", "neck", "waist", "legs", "feet", "hands", "bracers", "rightFinger", "leftFinger", "offHand", "mainHand"};
	public static final int[] ITEMS_HOLDERS_IDS = {R.id.head_image, R.id.shoulders_image, R.id.torso_image, R.id.neck_image, R.id.waist, R.id.legs_image, R.id.feet_image, 
		R.id.hands_image, R.id.bracers_image, R.id.rightFinger_image, R.id.leftFinger_image, R.id.offHand_image, R.id.mainHand_image};
	public static final int[][] ITEMS_GEMS = {{R.id.gem1_head, R.id.gem2_head}, {R.id.gem1_shoulders, R.id.gem2_shoulders}, {R.id.gem1_torso, R.id.gem2_torso, R.id.gem3_torso},
		{R.id.gem1_neck}, {}, {R.id.gem1_legs, R.id.gem2_legs}, {R.id.gem1_feet, R.id.gem2_feet}, {R.id.gem1_hands, R.id.gem2_hands}, 
		{R.id.gem1_bracers, R.id.gem2_bracers}, {R.id.gem1_rightFinger}, {R.id.gem1_leftFinger}, {R.id.gem1_offHand, R.id.gem2_offHand, R.id.gem3_offHand}, 
		{R.id.gem1_mainHand, R.id.gem2_mainHand, R.id.gem3_mainHand}};
	
	
	public static final int[][] ITEMS_SOCKETS = {{R.id.gem1_head_socket, R.id.gem2_head_socket}, {R.id.gem1_shoulders_socket, R.id.gem2_shoulders_socket}, 
		{R.id.gem1_torso_socket, R.id.gem2_torso_socket, R.id.gem3_torso_socket},
		{R.id.gem1_neck_socket}, {}, {R.id.gem1_legs_socket, R.id.gem2_legs_socket}, {R.id.gem1_feet_socket, R.id.gem2_feet_socket}, 
		{R.id.gem1_hands_socket, R.id.gem2_hands_socket}, 
		{R.id.gem1_bracers_socket, R.id.gem2_bracers_socket}, {R.id.gem1_rightFinger_socket}, {R.id.gem1_leftFinger_socket}, 
		{R.id.gem1_offHand_socket, R.id.gem2_offHand_socket, R.id.gem3_offHand_socket}, 
		{R.id.gem1_mainHand_socket, R.id.gem2_mainHand_socket, R.id.gem3_mainHand_socket}};
	
	
	
	public static String ITEM_INFO_URL = ".battle.net/api/d3/data/";
}
