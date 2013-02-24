package kpk.dev.d3app.database;

import kpk.dev.d3app.models.accountmodels.ArtisanModel;
import kpk.dev.d3app.models.accountmodels.D3Follower;
import kpk.dev.d3app.models.accountmodels.D3Item;
import kpk.dev.d3app.models.accountmodels.D3Mode;
import kpk.dev.d3app.models.accountmodels.D3PassiveSkill;
import kpk.dev.d3app.models.accountmodels.D3Rune;
import kpk.dev.d3app.models.accountmodels.D3Skill;
import kpk.dev.d3app.models.accountmodels.HeroModel;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.models.accountmodels.ProgressionModel;
import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import kpk.dev.d3app.models.bnetmodels.Region;
import kpk.dev.d3app.models.bnetmodels.Server;
import kpk.dev.d3app.util.D3Constants;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class D3AppSQLitehelper extends SQLiteOpenHelper {
	private static final String REGIONS_TABLE_CREATE_STRING = "create table "
		      + Region.TABLE_NAME + "(" + Region.ID_COLUMN
		      + " integer primary key autoincrement, " + Region.REGION_COLUMN
		      + " text not null);";
	private static final String SERVERS_TABLE_CREATE_STRING = "create table "
		      + Server.TABLE_NAME + "(" + Server.ID_COLUMN
		      + " integer primary key autoincrement, " + Server.NAME_COLUMN
		      + " text not null, " + Server.AVAILABLE_COLUMN + " integer, " + Server.REGION_COLUMN + " text not null" + ");";
	
	private static final String PROFILES_TABLE_CREATE_STRING = "create table "
		      + ProfileModel.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + IProfileModel.PROFILE_TAG_COLUMN + " text not null, " 
		      + ProfileModel.LAST_HERO_PLAYED + " integer, " 
		      + ProfileModel.LAST_UPDATED + " integer, "
		      + ProfileModel.BARBARIAN_PLAYTIME_COLUMN + " real, "
		      + ProfileModel.DEMON_HUNTER_PLAYTIME_COLUMN + " real, "
		      + ProfileModel.MONK_PLAYTIME_COLUMN + " real, "
		      + ProfileModel.WITCH_DOCTOR_PLAYTIME_COLUMN + " real, "
		      + ProfileModel.WIZARD_PLAYTIME_COLUMN + " real, "
		      + ProfileModel.LIFETIME_KILLS_COLUMN + " integer, " 
		      + ProfileModel.ELITES_KILLS_COLUMN + " integer, " 
		      + ProfileModel.HARDCORE_KILLS_COLUMN + " integer, " 
		      + IProfileModel.SERVER_COLUMN + " text not null" + ");";
	
	private static final String HEROES_TABLE_CREATE_STRING = "create table "
		      + HeroModel.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " + HeroModel.HERO_ID_COLUMN + " integer, "  
		      + HeroModel.HERO_NAME_COLUMN + " text not null, "
		      + HeroModel.HERO_CLASS_COLUMN + " text not null, "
		      + HeroModel.HERO_GENDER_COLUMN + " integer, " 
		      + HeroModel.HERO_HARDCORE_COLUMN + " integer, "
		      + HeroModel.HERO_LEVEL_COLUMN + " integer, "
		      + HeroModel.HERO_PARAGON_LEVEL_COLUMN + " integer, "
		      + HeroModel.HERO_LAST_UPDATED_COLUMN + " integer, "
		      + HeroModel.HERO_DEAD_COLUMN + " integer, "
		      + IProfileModel.PROFILE_TAG_COLUMN + " text not null, "
		      + IProfileModel.SERVER_COLUMN + " text not null" + ");";
	
	private static final String ARTISANS_TABLE_CREATE_STRING = "create table "
		      + ArtisanModel.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " + ArtisanModel.SLUG_COLUMN + " text not null, "  
		      + ArtisanModel.LEVEL_COLUMN + " integer, "
		      + ArtisanModel.STEP_CURRENT_COLUMN + " integer, "
		      + ArtisanModel.STEP_MAX_COLUMN + " integer, " 
		      + IProfileModel.PROFILE_TAG_COLUMN + " text not null, "
		      + IProfileModel.SERVER_COLUMN + " text not null" + ");";
	
	private static final String HARDCORE_ARTISANS_TABLE_CREATE_STRING = "create table "
		      + ArtisanModel.TABLE_NAME_HARDCORE + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " + ArtisanModel.SLUG_COLUMN + " text not null, "  
		      + ArtisanModel.LEVEL_COLUMN + " integer, "
		      + ArtisanModel.STEP_CURRENT_COLUMN + " integer, "
		      + ArtisanModel.STEP_MAX_COLUMN + " integer, " 
		      + IProfileModel.PROFILE_TAG_COLUMN + " text not null, "
		      + IProfileModel.SERVER_COLUMN + " text not null" + ");";
	
	private static final String PROGRESSION_TABLE = "create table "
		      + ProgressionModel.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " + D3Mode.MODE_NAME_COLUMN + " text not null, "  
		      + D3Mode.ACT1_COLUMN + " integer, "
		      + D3Mode.ACT2_COLUMN + " integer, "
		      + D3Mode.ACT3_COLUMN + " integer, "
		      + D3Mode.ACT4_COLUMN + " integer, "
		      + IProfileModel.PROFILE_TAG_COLUMN + " text not null, "
		      + IProfileModel.SERVER_COLUMN + " text not null" + ");";
	
	private static final String HARDCORE_PROGRESSION_TABLE = "create table "
		      + ProgressionModel.HARDCORE_TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " + D3Mode.MODE_NAME_COLUMN + " text not null, "  
		      + D3Mode.ACT1_COLUMN + " integer, "
		      + D3Mode.ACT2_COLUMN + " integer, "
		      + D3Mode.ACT3_COLUMN + " integer, "
		      + D3Mode.ACT4_COLUMN + " integer, "
		      + IProfileModel.PROFILE_TAG_COLUMN + " text not null, "
		      + IProfileModel.SERVER_COLUMN + " text not null" + ");";
	
	private static final String RUNES_TABLE = "create table "
		      + D3Rune.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Rune.SKILL_SLUG_COLUMN + " text not null, "
		      + D3Rune.SLUG_COLUMN + " text not null, "
		      + D3Rune.TYPE_COLUMN + " text not null, "
		      + D3Rune.NAME_COLUMN + " text not null, "
		      + D3Rune.LEVEL_COLUMN + " integer, "
		      + D3Rune.DESCRIPTION_COLUMN + " text not null, "
		      + D3Rune.SIMPLE_DESCRIPTION_COLUMN + " text not null, "
		      + D3Rune.TOOLTIP_PARAMS_COLUMN + " text not null, "
		      + D3Rune.SKILLCALC_ID_COLUMN + " text not null, "
		      + D3Rune.ORDER_COLUMN + " integer"
		      + ");";
	
	private static final String HERO_SKILLS_TABLE = "create table "
		      + D3Skill.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Skill.SLUG_COLUMN + " text not null, "
		      + D3Skill.NAME_COLUMN + " text not null, "
		      + D3Skill.ICON_COLUMN + " text not null, "
		      + D3Skill.LEVEL_COLUMN + " integer, "
		      + D3Skill.CATEGORY_SLUG_COLUMN + " text not null, "
		      + D3Skill.TOOLTIP_URL_COLUMN + " text not null, "
		      + D3Skill.DESCRIPTION_COLUMN + " text not null, "
		      + D3Skill.SIMPLE_DESCRIPTION_COLUMN + " text not null, "
		      + D3Skill.SKILLCALC_ID_COLUMN + " text not null"
		      + ");";
	
	private static final String HERO_PASSIVE_SKILLS_TABLE = "create table "
		      + D3PassiveSkill.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3PassiveSkill.SLUG_COLUMN + " text not null, "
		      + D3PassiveSkill.NAME_COLUMN + " text not null, "
		      + D3PassiveSkill.ICON_COLUMN + " text not null, "
		      + D3PassiveSkill.TOOLTIP_URL_COLUMN + " text not null, "
		      + D3PassiveSkill.DESCRIPTION_COLUMN + " text not null, "
		      + D3PassiveSkill.FLAVOR_COLUMN + " text not null, "
		      + D3PassiveSkill.SKILLCALC_ID_COLUMN + " text not null, "
		      + D3PassiveSkill.LEVEL_COLUMN + " integer"    
		      + ");";
	
	private static final String HERO_ITEMS_TABLE = "create table "
		      + D3Item.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Item.TYPE_COLUMN + " text not null, "
		      + D3Item.ITEM_ID_COLUMN + " text not null, "
		      + D3Item.ITEM_NAME_COLUMN + " text not null, "
		      + D3Item.ITEM_ICON_COLUMN + " text not null, "
		      + D3Item.ITEM_DISPLAY_COLOR_COLUMN + " text not null, "
		      + D3Item.ITEM_TOOLTIP_PARAMS_COLUMN + " text not null, "
		      + D3Item.ITEM_GEMS_COLUMN + " text"
		      + ");";
	
	private static final String HERO_STATS_TABLE = "create table "
		      + HeroModelDecorator.HERO_STATS_TABLE_NAME + "(" 
		      + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + HeroModelDecorator.STATS_KEYS_COLUMN + " text not null, "
		      + HeroModelDecorator.STATS_VALUES_COLUMN + " text not null"   
		      + ");";
	
	private static final String HERO_PROGRESSION_TABLE = "create table "
		      + HeroModelDecorator.HERO_PROGRESSION_TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, "
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Mode.MODE_NAME_COLUMN + " text not null, "  
		      + D3Mode.ACT1_COLUMN + " integer, "
		      + D3Mode.ACT2_COLUMN + " integer, "
		      + D3Mode.ACT3_COLUMN + " integer, "
		      + D3Mode.ACT4_COLUMN + " integer"
		      + ");";
	
	private static final String FOLLOWERS_TABLE = "create table "
		      + D3Follower.TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Follower.FOLLOWER_SLUG_COLUMN + " text not null, "
		      + D3Follower.FOLLOWER_LEVEL_COLUMN + " text not null"   
		      + ");";
	
	private static final String FOLLOWER_SKILLS_TABLE = "create table "
		      + D3Skill.FOLLOWER_SKILLS_TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Skill.FOLLOWER_SLUG_COLUMN + " text not null, "
		      + D3Skill.SLUG_COLUMN + " text not null, "
		      + D3Skill.NAME_COLUMN + " text not null, "
		      + D3Skill.ICON_COLUMN + " text not null, "
		      + D3Skill.LEVEL_COLUMN + " integer, "
		      + D3Skill.CATEGORY_SLUG_COLUMN + " text, "
		      + D3Skill.TOOLTIP_URL_COLUMN + " text not null, "
		      + D3Skill.DESCRIPTION_COLUMN + " text not null, "
		      + D3Skill.SIMPLE_DESCRIPTION_COLUMN + " text not null, "
		      + D3Skill.SKILLCALC_ID_COLUMN + " text not null"
		      + ");";
	
	private static final String FOLLOWERS_ITEMS_TABLE = "create table "
		      + D3Item.FOLLOWER_ITEMS_TABLE_NAME + "(" + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Item.FOLLOWER_SLUG_COLUMN + " text not null, "
		      + D3Item.TYPE_COLUMN + " text not null, "
		      + D3Item.ITEM_ID_COLUMN + " text not null, "
		      + D3Item.ITEM_NAME_COLUMN + " text not null, "
		      + D3Item.ITEM_ICON_COLUMN + " text not null, "
		      + D3Item.ITEM_DISPLAY_COLOR_COLUMN + " text not null, "
		      + D3Item.ITEM_TOOLTIP_PARAMS_COLUMN + " text not null, "
		      + D3Item.ITEM_GEMS_COLUMN + " text"
		      + ");";
	
	private static final String FOLLOWER_STATS_TABLE = "create table "
		      + D3Follower.FOLLOWER_STATS_TABLE_NAME + "(" 
		      + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + D3Follower.FOLLOWER_SLUG_COLUMN + " text not null, "
		      + HeroModelDecorator.STATS_KEYS_COLUMN + " text not null, "
		      + HeroModelDecorator.STATS_VALUES_COLUMN + " text not null"   
		      + ");";
	
	private static final String HERO_KILLS_TABLE = "create table "
		      + HeroModelDecorator.HERO_KILLS_TABLE_NAME + "(" 
		      + IProfileModel.ID_COLUMN
		      + " integer primary key autoincrement, " 
		      + HeroModel.HERO_ID_COLUMN + " integer, "
		      + HeroModelDecorator.HERO_KILLS_KEYS_COLUMN + " text not null, "
		      + HeroModelDecorator.HERO_KILLS_VALUES_COLUMN + " text not null"
		      + ");";
	
	private static D3AppSQLitehelper mInstance;
	
	private D3AppSQLitehelper(Context context) {
		super(context, D3Constants.DATABASE_NAME, null, D3Constants.DATABASE_VERSION);
	}
	
	public static D3AppSQLitehelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new D3AppSQLitehelper(context);
		}
		
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(REGIONS_TABLE_CREATE_STRING);
		database.execSQL(SERVERS_TABLE_CREATE_STRING);
		database.execSQL(PROFILES_TABLE_CREATE_STRING);
		database.execSQL(HEROES_TABLE_CREATE_STRING);
		database.execSQL(ARTISANS_TABLE_CREATE_STRING);
		database.execSQL(HARDCORE_ARTISANS_TABLE_CREATE_STRING);
		database.execSQL(PROGRESSION_TABLE);
		database.execSQL(HARDCORE_PROGRESSION_TABLE);
		database.execSQL(HERO_SKILLS_TABLE);
		database.execSQL(HERO_KILLS_TABLE);
		database.execSQL(HERO_PROGRESSION_TABLE);
		database.execSQL(RUNES_TABLE);
		database.execSQL(HERO_PASSIVE_SKILLS_TABLE);
		database.execSQL(HERO_ITEMS_TABLE);
		database.execSQL(HERO_STATS_TABLE);
		database.execSQL(FOLLOWER_SKILLS_TABLE);
		database.execSQL(FOLLOWERS_ITEMS_TABLE);
		database.execSQL(FOLLOWERS_TABLE);
		database.execSQL(FOLLOWER_STATS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		//database.execSQL("DROP TABLE " + Region.TABLE_NAME);
		//database.execSQL("DROP TABLE " + Server.TABLE_NAME);
		onCreate(database);
	}

}
