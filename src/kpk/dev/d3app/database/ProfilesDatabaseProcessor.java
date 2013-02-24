package kpk.dev.d3app.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
import kpk.dev.d3app.util.Utils;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProfilesDatabaseProcessor extends DatabaseProcessorBase {
	private List<ProfileModel> mProfiles = new ArrayList<ProfileModel>();
	private enum ProfileModelType{
		heroes(),
		artisans(),
		hardcoreArtisans();
	}
	public synchronized boolean insertData(List<IProfileModel> items, SQLiteDatabase database) {
		database.beginTransaction();
		boolean result = false;
		try{
			for(IProfileModel item : items) {
				ProfileModel model = (ProfileModel)item;
				if(profileExists(model, database)){
					KPKLog.d("UPDATE INITIAL DATA");
					deleteProfile(database, model.getBattleTag(), model.getServer());
					result = false;
				}else{
					KPKLog.d("INSERT INITIAL DATA");
					mProfiles.add(model);
					result = true;
				}
				database.insert(ProfileModel.TABLE_NAME, "null", item.getContentValues());
				insertProfileModelSummary(model.getHeroes(), database, model.getBattleTag(), model.getServer(), ProfileModelType.heroes);
				insertProfileModelSummary(model.getArtisans(), database, model.getBattleTag(), model.getServer(), ProfileModelType.artisans);
				insertProfileModelSummary(model.getHardcoreArtisans(), database, model.getBattleTag(), model.getServer(), ProfileModelType.hardcoreArtisans);
				insertProgression(model.getProgression(), database, model.getBattleTag(), false, model.getServer());
				insertProgression(model.getHardcoreProgression(), database, model.getBattleTag(), true, model.getServer());
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
		return result;
	}
	
	private synchronized boolean profileExists(ProfileModel item, SQLiteDatabase database) {
		Cursor checkerCursor = database.rawQuery("SELECT * FROM " + ProfileModel.TABLE_NAME + " WHERE " + IProfileModel.PROFILE_TAG_COLUMN + "='" + item.getBattleTag()
				+ "' AND " + IProfileModel.SERVER_COLUMN + "='" + item.getServer() + "'", null);
		boolean result = checkerCursor.moveToFirst();
		checkerCursor.close();
		return result;
	}

	private synchronized void insertProgression(ProgressionModel progression, SQLiteDatabase database, String battleTag, 
			boolean hardcore, String server) {
		progression.setParentProfileTag(battleTag);
		final List<D3Mode> d3modes = new ArrayList<D3Mode>();
		d3modes.add(progression.getNormal());
		d3modes.add(progression.getNightmare());
		d3modes.add(progression.getHell());
		d3modes.add(progression.getInferno());
		database.beginTransaction();
		try{
			for(int i = 0; i < d3modes.size(); i++){
				d3modes.get(i).setName(D3Constants.mModes[i]);
				final ContentValues contentValues = d3modes.get(i).getContentValues();
				contentValues.put(IProfileModel.PROFILE_TAG_COLUMN, battleTag);
				contentValues.put(IProfileModel.SERVER_COLUMN, server);
				contentValues.put(D3Mode.MODE_NAME_COLUMN, D3Constants.mModes[i]);

				database.insert((hardcore)?ProgressionModel.HARDCORE_TABLE_NAME:ProgressionModel.TABLE_NAME, "null", contentValues);
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	

	private  synchronized <T extends IProfileModel> void insertProfileModelSummary(List<T> list, SQLiteDatabase database, String battleTag, 
			String server, ProfileModelType type) {
		String tableName = determineTableName(type);
		database.beginTransaction();
		try{
			for(IProfileModel profileModel : list){
				if(profileModel instanceof HeroModel){
					HeroModel hero = (HeroModel)profileModel;
					((HeroModel)profileModel).setPortrait(ProfileModel.HERO_PORTRAIT_URL + Utils.removeHiphen(hero.getclass()) + "_" 
							+ hero.getGender().name() + ".png");
				}
				profileModel.setParentProfileTag(battleTag);
				profileModel.setServer(server);
				database.insert(tableName, "null", profileModel.getContentValues());
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	private String determineTableName(ProfileModelType type) {
		String tableName = "";
		if(type.name().equalsIgnoreCase(ProfileModelType.artisans.name())){
			tableName = ArtisanModel.TABLE_NAME;
		}else if(type.name().equalsIgnoreCase(ProfileModelType.hardcoreArtisans.name())){
			tableName = ArtisanModel.TABLE_NAME_HARDCORE;
		}else if(type.name().equalsIgnoreCase(ProfileModelType.heroes.name())){
			tableName = HeroModel.TABLE_NAME;
		}
		return tableName;
	}

	public synchronized ProfileModel getProfileByServerAndBattleTag(SQLiteDatabase database, String server, String battleTag) {
		Cursor profileCursor = database.rawQuery("SELECT * FROM " + ProfileModel.TABLE_NAME + " WHERE " + IProfileModel.PROFILE_TAG_COLUMN
				+ "='" + battleTag + "' AND " + IProfileModel.SERVER_COLUMN + "='" + server + "'", null);
		ProfileModel profile =  null;
		if(profileCursor.getCount() > 0) {
			if(profileCursor.moveToFirst()) {
				profile = getInitialProfileData(profileCursor);
				profile.setHeroes(getHeroes(database, profile.getBattleTag(), profile.getServer()));
				profile.setLastPlayedHeroPortrait(ProfileModel.getHeroPortrait(profile.getLastHeroPlayed(), profile));
				profile.setArtisans(getArtisans(database, profile.getBattleTag(), false, profile.getServer()));
				profile.setHardcoreArtisans(getArtisans(database, profile.getBattleTag(), true, profile.getServer()));
				profile.setProgression(getProgression(database, profile.getBattleTag(), false, profile.getServer()));
				profile.setHardcoreProgression(getProgression(database, profile.getBattleTag(), true, profile.getServer()));
			}
		}
		profileCursor.close();
		return profile;
	}
	
	public synchronized List<IProfileModel> getProfiles(SQLiteDatabase database){
		Cursor profilesCursor = database.rawQuery("SELECT * FROM " + ProfileModel.TABLE_NAME, null);
		final List<IProfileModel> profiles = new ArrayList<IProfileModel>();
		while(profilesCursor.moveToNext()){
			ProfileModel profile = getInitialProfileData(profilesCursor);
			
			profile.setHeroes(getHeroes(database, profile.getBattleTag(), profile.getServer()));
			profile.setLastPlayedHeroPortrait(ProfileModel.getHeroPortrait(profile.getLastHeroPlayed(), profile));
			profile.setArtisans(getArtisans(database, profile.getBattleTag(), false, profile.getServer()));
			profile.setHardcoreArtisans(getArtisans(database, profile.getBattleTag(), true, profile.getServer()));
			profile.setProgression(getProgression(database, profile.getBattleTag(), false, profile.getServer()));
			profile.setHardcoreProgression(getProgression(database, profile.getBattleTag(), true, profile.getServer()));
			profiles.add(profile);
		}
		profilesCursor.close();
		return profiles;
	}
	
	private synchronized ProgressionModel getProgression(SQLiteDatabase database, String battleTag, boolean isHardcore, String server) {
		final String tableName = (!isHardcore)?ProgressionModel.TABLE_NAME:ProgressionModel.HARDCORE_TABLE_NAME;
		Cursor progressionCursor = database.rawQuery("SELECT * FROM " 
				+ tableName + " WHERE " 
				+ IProfileModel.PROFILE_TAG_COLUMN + "='" + battleTag
				+ "' AND " + IProfileModel.SERVER_COLUMN + "='" + server +"'", null);
		return getProgression(progressionCursor);
	}
	
	private synchronized ProfileModel getInitialProfileData(Cursor profilesCursor) {
		final ProfileModel profile = new ProfileModel();
		int regionNameColumnIndex = profilesCursor.getColumnIndexOrThrow(IProfileModel.SERVER_COLUMN);
		int battleTagColumnIndex = profilesCursor.getColumnIndexOrThrow(IProfileModel.PROFILE_TAG_COLUMN);
		int lastHeroColumnIndex = profilesCursor.getColumnIndexOrThrow(ProfileModel.LAST_HERO_PLAYED);
		int lastUpdatedColumnIndex = profilesCursor.getColumnIndexOrThrow(ProfileModel.LAST_UPDATED);
		int lifetimeKillsColumnIndex = profilesCursor.getColumnIndexOrThrow(ProfileModel.LIFETIME_KILLS_COLUMN);
		int elitesKillsColumnIndex = profilesCursor.getColumnIndexOrThrow(ProfileModel.ELITES_KILLS_COLUMN);
		int hardcoreKillsColumnIndex = profilesCursor.getColumnIndexOrThrow(ProfileModel.HARDCORE_KILLS_COLUMN);
		int barbarianPlaytimeColumn = profilesCursor.getColumnIndexOrThrow(ProfileModel.BARBARIAN_PLAYTIME_COLUMN);
		int demonHunterPlaytimeColumn = profilesCursor.getColumnIndexOrThrow(ProfileModel.DEMON_HUNTER_PLAYTIME_COLUMN);
		int monkPlaytimeColumn = profilesCursor.getColumnIndexOrThrow(ProfileModel.MONK_PLAYTIME_COLUMN);
		int witchDoctorPlayTimeColumn = profilesCursor.getColumnIndexOrThrow(ProfileModel.WITCH_DOCTOR_PLAYTIME_COLUMN);
		int wizardPlayTimeColumn = profilesCursor.getColumnIndexOrThrow(ProfileModel.WIZARD_PLAYTIME_COLUMN);
		profile.setServer(profilesCursor.getString(regionNameColumnIndex));
		profile.setBattleTag(profilesCursor.getString(battleTagColumnIndex));
		profile.setLastHeroPlayed(profilesCursor.getLong(lastHeroColumnIndex));
		profile.setLastUpdated(profilesCursor.getLong(lastUpdatedColumnIndex));
		
		final Map<String, Integer> killsMap = new HashMap<String, Integer>();
		killsMap.put(profile.mLifeTimeKillsKey, profilesCursor.getInt(lifetimeKillsColumnIndex));
		killsMap.put(profile.mElitesKillsKey, profilesCursor.getInt(elitesKillsColumnIndex));
		killsMap.put(profile.mHardcoreKillsKey, profilesCursor.getInt(hardcoreKillsColumnIndex));
		profile.setKills(killsMap);
		
		final Map<String, Double> playTimesMap = new LinkedHashMap<String, Double>();
		playTimesMap.put(profile.mBarbarianKey, profilesCursor.getDouble(barbarianPlaytimeColumn));
		playTimesMap.put(profile.mDemonHunterKey, profilesCursor.getDouble(demonHunterPlaytimeColumn));
		playTimesMap.put(profile.mMonkKey, profilesCursor.getDouble(monkPlaytimeColumn));
		playTimesMap.put(profile.mWitchDoctorKey, profilesCursor.getDouble(witchDoctorPlayTimeColumn));
		playTimesMap.put(profile.mWizardKey, profilesCursor.getDouble(wizardPlayTimeColumn));
		profile.setTimePlayed(playTimesMap);
		return profile;
	}
	
	private synchronized List<ArtisanModel> getArtisans(SQLiteDatabase database, String battleTag, boolean hardcore, String server) {
		final String tableName = (!hardcore)?ArtisanModel.TABLE_NAME:ArtisanModel.TABLE_NAME_HARDCORE;
		Cursor artisanCursor = database.rawQuery("SELECT * FROM " 
				+ tableName + " WHERE " 
				+ IProfileModel.PROFILE_TAG_COLUMN + "='" + battleTag
				+ "' AND " + IProfileModel.SERVER_COLUMN + "='" + server +"'", null);
		
		final List<ArtisanModel> artisans = new ArrayList<ArtisanModel>();
		
		int artisanSlugColumnIndex = artisanCursor.getColumnIndexOrThrow(ArtisanModel.SLUG_COLUMN);
		int artisanLevelColumnIndex = artisanCursor.getColumnIndexOrThrow(ArtisanModel.LEVEL_COLUMN);
		int artisanCurrentStepColumnIndex = artisanCursor.getColumnIndexOrThrow(ArtisanModel.STEP_CURRENT_COLUMN);
		int artisanMaxStepColumnIndex = artisanCursor.getColumnIndexOrThrow(ArtisanModel.STEP_MAX_COLUMN);
		int artisanParentTagColumnIndex = artisanCursor.getColumnIndexOrThrow(IProfileModel.PROFILE_TAG_COLUMN);
		int artisanServerColumnIndex = artisanCursor.getColumnIndexOrThrow(IProfileModel.SERVER_COLUMN);
		while(artisanCursor.moveToNext()) {
			ArtisanModel artisanModel = new ArtisanModel();
			artisanModel.setSlug(artisanCursor.getString(artisanSlugColumnIndex));
			artisanModel.setLevel(artisanCursor.getInt(artisanLevelColumnIndex));
			artisanModel.setStepCurrent(artisanCursor.getInt(artisanCurrentStepColumnIndex));
			artisanModel.setStepMax(artisanCursor.getInt(artisanMaxStepColumnIndex));
			artisanModel.setParentProfileTag(artisanCursor.getString(artisanParentTagColumnIndex));
			artisanModel.setServer(artisanCursor.getString(artisanServerColumnIndex));
			artisans.add(artisanModel);
		}
		artisanCursor.close();
		return artisans;
		
	}

	private synchronized List<HeroModel> getHeroes(SQLiteDatabase database, String battleTag, String server) {
		Cursor cursor = database.rawQuery("SELECT * FROM " + HeroModel.TABLE_NAME + " WHERE " 
				+ IProfileModel.PROFILE_TAG_COLUMN + "='" + battleTag 
				+ "' AND " + IProfileModel.SERVER_COLUMN + "='" + server +"'", null);
		final List<HeroModel> heroes = new ArrayList<HeroModel>();
		
		int heroIDColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_ID_COLUMN);
		int heroNameColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_NAME_COLUMN);
		int heroClassColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_CLASS_COLUMN);
		int heroGenderColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_GENDER_COLUMN);
		int heroHardcoreColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_HARDCORE_COLUMN);
		int heroLevelColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_LEVEL_COLUMN);
		int heroParagonColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_PARAGON_LEVEL_COLUMN);
		int heroLastUpdatedColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_LAST_UPDATED_COLUMN);
		int heroIsDeadColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_DEAD_COLUMN);
		int heroParentProfileTag = cursor.getColumnIndexOrThrow(IProfileModel.PROFILE_TAG_COLUMN);
		int heroServerColumnIndex = cursor.getColumnIndexOrThrow(IProfileModel.SERVER_COLUMN);
		
		while(cursor.moveToNext()) {
			HeroModel heroModel = new HeroModel();
			heroModel.setID(cursor.getLong(heroIDColumnIndex));
			heroModel.setName(cursor.getString(heroNameColumnIndex));
			heroModel.setclass(cursor.getString(heroClassColumnIndex));
			HeroModel.Gender gender =  (cursor.getString(heroGenderColumnIndex).equalsIgnoreCase("female"))?HeroModel.Gender.female:HeroModel.Gender.male;
			heroModel.setGender(gender);
			heroModel.setHardcore((cursor.getInt(heroHardcoreColumnIndex) == 0)?false:true);
			heroModel.setLevel(cursor.getInt(heroLevelColumnIndex));
			heroModel.setParagonLevel(cursor.getInt(heroParagonColumnIndex));
			heroModel.setLastUpdated(cursor.getLong(heroLastUpdatedColumnIndex));
			heroModel.setDead((cursor.getInt(heroIsDeadColumnIndex) == 0)?false:true);
			heroModel.setParentProfileTag(cursor.getString(heroParentProfileTag));
			heroModel.setPortrait(ProfileModel.HERO_PORTRAIT_URL + Utils.removeHiphen(heroModel.getclass()) + "_" 
					+ heroModel.getGender().name() + ".png");
			heroModel.setServer(cursor.getString(heroServerColumnIndex));
			heroes.add(heroModel);
		}
		cursor.close();
		return heroes;
	}
	
	public synchronized HeroModel getHeroById(SQLiteDatabase database, String battleTag, String server, long heroId) {
		Cursor cursor = database.rawQuery("SELECT * FROM " + HeroModel.TABLE_NAME + " WHERE " 
				+ IProfileModel.PROFILE_TAG_COLUMN + "='" + battleTag 
				+ "' AND " + HeroModel.HERO_ID_COLUMN + "='" + heroId + "' AND " + IProfileModel.SERVER_COLUMN + "='" + server +"'", null);
		int heroIDColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_ID_COLUMN);
		int heroNameColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_NAME_COLUMN);
		int heroClassColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_CLASS_COLUMN);
		int heroGenderColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_GENDER_COLUMN);
		int heroHardcoreColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_HARDCORE_COLUMN);
		int heroLevelColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_LEVEL_COLUMN);
		int heroParagonColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_PARAGON_LEVEL_COLUMN);
		int heroLastUpdatedColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_LAST_UPDATED_COLUMN);
		int heroIsDeadColumnIndex = cursor.getColumnIndexOrThrow(HeroModel.HERO_DEAD_COLUMN);
		int heroParentProfileTag = cursor.getColumnIndexOrThrow(IProfileModel.PROFILE_TAG_COLUMN);
		int heroServerColumnIndex = cursor.getColumnIndexOrThrow(IProfileModel.SERVER_COLUMN);
		
		if(cursor.moveToFirst()){
			HeroModel heroModel = new HeroModel();
			heroModel.setID(cursor.getLong(heroIDColumnIndex));
			heroModel.setName(cursor.getString(heroNameColumnIndex));
			heroModel.setclass(cursor.getString(heroClassColumnIndex));
			HeroModel.Gender gender =  (cursor.getString(heroGenderColumnIndex).equalsIgnoreCase("female"))?HeroModel.Gender.female:HeroModel.Gender.male;
			heroModel.setGender(gender);
			heroModel.setHardcore((cursor.getInt(heroHardcoreColumnIndex) == 0)?false:true);
			heroModel.setLevel(cursor.getInt(heroLevelColumnIndex));
			heroModel.setParagonLevel(cursor.getInt(heroParagonColumnIndex));
			heroModel.setLastUpdated(cursor.getLong(heroLastUpdatedColumnIndex));
			heroModel.setDead((cursor.getInt(heroIsDeadColumnIndex) == 0)?false:true);
			heroModel.setParentProfileTag(cursor.getString(heroParentProfileTag));
			heroModel.setPortrait(ProfileModel.HERO_PORTRAIT_URL + Utils.removeHiphen(heroModel.getclass()) + "_" 
					+ heroModel.getGender().name() + ".png");
			heroModel.setServer(cursor.getString(heroServerColumnIndex));
			return heroModel;
		}
			
		return null;
	}
	
	public synchronized String[] deleteProfile(SQLiteDatabase database, String battleTag, String server){
		String[] whereArgs = {battleTag, server};
		database.beginTransaction();
		try{
			database.delete(ProfileModel.TABLE_NAME, IProfileModel.PROFILE_TAG_COLUMN + "=? AND " + IProfileModel.SERVER_COLUMN + "=?", whereArgs);
			database.delete(ArtisanModel.TABLE_NAME, IProfileModel.PROFILE_TAG_COLUMN + "=? AND " + IProfileModel.SERVER_COLUMN + "=?", whereArgs);
			database.delete(ArtisanModel.TABLE_NAME_HARDCORE, IProfileModel.PROFILE_TAG_COLUMN + "=? AND " + IProfileModel.SERVER_COLUMN + "=?", whereArgs);
			database.delete(ProgressionModel.TABLE_NAME, IProfileModel.PROFILE_TAG_COLUMN + "=? AND " + IProfileModel.SERVER_COLUMN + "=?", whereArgs);
			database.delete(ProgressionModel.HARDCORE_TABLE_NAME, IProfileModel.PROFILE_TAG_COLUMN + "=? AND " + IProfileModel.SERVER_COLUMN + "=?", whereArgs);
			final List<Long> heroIds = getHeroesId(database, battleTag, server);
			for(long id : heroIds){
				deleteHero(database, id);
			}
			
			database.delete(HeroModel.TABLE_NAME, IProfileModel.PROFILE_TAG_COLUMN + "=? AND " + IProfileModel.SERVER_COLUMN + "=?", whereArgs);
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
		String[] returns = {battleTag, server};
		return returns;
	}
	
	public synchronized void deleteHero(SQLiteDatabase database, long id) {
		database.delete(HeroModelDecorator.HERO_STATS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Skill.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3PassiveSkill.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Rune.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(HeroModelDecorator.HERO_PROGRESSION_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Item.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(HeroModelDecorator.HERO_KILLS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Follower.TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Skill.FOLLOWER_SKILLS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Follower.FOLLOWER_STATS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
		database.delete(D3Item.FOLLOWER_ITEMS_TABLE_NAME, HeroModel.HERO_ID_COLUMN + "=" + id, null);
	}
	
	private List<Long> getHeroesId(SQLiteDatabase database, String battletag, String server){
		final Cursor heroIDCursor = database.rawQuery("SELECT " + HeroModel.HERO_ID_COLUMN + " FROM " + HeroModel.TABLE_NAME + " WHERE " 
				+ IProfileModel.PROFILE_TAG_COLUMN + "='" + battletag + "' AND " + IProfileModel.SERVER_COLUMN + "='" + server + "'", null);
		List<Long> idList = new ArrayList<Long>();
		while(heroIDCursor.moveToNext()){
			int idIndex = heroIDCursor.getColumnIndex(HeroModel.HERO_ID_COLUMN);
			if(idIndex != -1){
				long id = heroIDCursor.getLong(idIndex);
				idList.add(id);
			}else{
				continue;
			}
			
		}
		return idList;
	}
}
