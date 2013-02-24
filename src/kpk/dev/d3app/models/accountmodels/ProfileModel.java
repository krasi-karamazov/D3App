package kpk.dev.d3app.models.accountmodels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import kpk.dev.d3app.util.Utils;

import android.content.ContentValues;

public class ProfileModel implements IProfileModel {
	public static final String LAST_HERO_PLAYED = "last_hero_played";
	public static final String BARBARIAN_PLAYTIME_COLUMN = "barbarian_playtime";
	public static final String DEMON_HUNTER_PLAYTIME_COLUMN = "demon_hunter_playtime";
	public static final String MONK_PLAYTIME_COLUMN = "monk_playtime";
	public static final String WITCH_DOCTOR_PLAYTIME_COLUMN = "witch_doctor_playtime";
	public static final String WIZARD_PLAYTIME_COLUMN = "wizard_playtime";
	public static final String LAST_UPDATED = "last_updated_time";
	public static final String TABLE_NAME = "D3ProfilesTable";
	public static final String LIFETIME_KILLS_COLUMN = "life_time_kills";
	public static final String ELITES_KILLS_COLUMN = "elites_kills";
	public static final String HARDCORE_KILLS_COLUMN = "hardcore_kills";
	public static final String HERO_PORTRAIT_URL = "http://media.blizzard.com/d3/icons/portraits/64/";
	/*
	 * Kills map keys
	 */
	public final String mLifeTimeKillsKey = "monsters";
	public final String mElitesKillsKey = "elites";
	public final String mHardcoreKillsKey = "hardcoreMonsters";
	/**/
	
	/*
	 * Time played map keys
	 */
	public final String mBarbarianKey = "barbarian";
	public final String mDemonHunterKey = "demon-hunter";
	public final String mMonkKey = "monk";
	public final String mWitchDoctorKey = "witch-doctor";
	public final String mWizardKey = "wizard";
	/**/
	private String mServer;
	private String mBattleTag;
	private long mLastHeroPlayer;
	private long mLastUpdated;
	private Map<String, Double> mTimePlayed;
	private List<HeroModel> mHeroes;
	private List<HeroModel> mFallenHeroes;
	private List<ArtisanModel> mArtisans;
	private List<ArtisanModel> mHardcoreArtisans;
	private Map<String, Integer> mKills;
	private ProgressionModel mProgression;
	private ProgressionModel mHardcoreProgression;
	private String mLastPlayedHeroPortraitURL;
	public ProfileModel(String server, String battleTAG, long lastHeroPlayerID, long lastUpdated, Map<String, Integer> kills, Map<String, Double> timePlayedPerClass){
		this.mServer = server;
		this.mBattleTag = battleTAG;
		this.mLastHeroPlayer = lastHeroPlayerID;
		this.mLastUpdated = lastUpdated;
		this.mTimePlayed = timePlayedPerClass;
		this.mKills = kills;
	}
	
	public enum ProgressionType{
		hardcore, 
		normal;
	};
	
	public enum ArtisanType{
		blacksmith, 
		jeweler;
	};
	
	public static String getHeroPortrait(long heroID, ProfileModel profile) {
		StringBuilder result = new StringBuilder(ProfileModel.HERO_PORTRAIT_URL);
		List<HeroModel> heroes = profile.getHeroes();
		for(HeroModel hero : heroes) {
			if(hero.getID() == heroID){
				result.append(Utils.removeHiphen(hero.getclass()) + "_" + hero.getGender().name() + ".png");
				break;
			}
		}
		
		return result.toString();
	}
	
	public ProfileModel() {
		
	}
	
	public HeroModel getHeroByID(long heroID){
		HeroModel heroResult = null;
		for(HeroModel hero : mHeroes) {
			if(hero.getID() == heroID) {
				heroResult = hero;
				break;
			}
		}
		return heroResult;
	}
	
	public String getLastPlayedHeroPortrait(){
		return mLastPlayedHeroPortraitURL;
	}
	
	public void setLastPlayedHeroPortrait(String url){
		mLastPlayedHeroPortraitURL = url;
	}
	
	public void setHeroes(List<HeroModel> heroes){
		mHeroes = heroes;
	}
	
	public List<HeroModel> getHeroes(){
		return mHeroes;
	}
	
	@JsonSetter("fallenHeroes")
	public void setFallenHeroes(List<HeroModel> heroes){
		mFallenHeroes = heroes;
	}
	
	@JsonGetter("fallenHeroes")
	public List<HeroModel> getFallenHeroes(){
		return mFallenHeroes;
	}
	
	public void setServer(String server) {
		this.mServer = server;
	}
	
	public String getServer() {
		return this.mServer;
	}
	
	public void setBattleTag(String battleTag) {
		this.mBattleTag = battleTag;
	}
	
	public String getBattleTag() {
		return this.mBattleTag;
	}
	
	public void setLastHeroPlayed(long id) {
		this.mLastHeroPlayer = id;
	}
	
	public long getLastHeroPlayed() {
		return this.mLastHeroPlayer;
	}
	
	public void setLastUpdated(long lastUpdated) {
		this.mLastUpdated = lastUpdated;
	}
	
	public long getLastUpdated() {
		return this.mLastUpdated;
	}
	
	public void setTimePlayed(Map<String, Double> timePerClass) {
		this.mTimePlayed = timePerClass;
	}
	
	public Map<String, Double> getTimePlayed() {
		return this.mTimePlayed;
	}
	
	public void setArtisans(List<ArtisanModel> artisans) {
		this.mArtisans = artisans;
	}
	
	public List<ArtisanModel> getArtisans() {
		return this.mArtisans;
	}
	
	public void setHardcoreArtisans(List<ArtisanModel> artisans) {
		this.mHardcoreArtisans = artisans;
	}
	
	public List<ArtisanModel> getHardcoreArtisans() {
		return this.mHardcoreArtisans;
	}
	
	public void setKills(Map<String, Integer> kills) {
		this.mKills = kills;
	}
	
	public int getLifeTimeKills() {
		return this.mKills.get(mLifeTimeKillsKey);
	}
	
	public int getEliteKills() {
		return this.mKills.get(mElitesKillsKey);
	}
	
	public int getHardcoreKills() {
		return this.mKills.get(mHardcoreKillsKey);
	}
	
	public void setProgression(ProgressionModel progression) {
		this.mProgression = progression;
	}
	
	public ProgressionModel getProgression() {
		return this.mProgression;
	}
	
	public void setHardcoreProgression(ProgressionModel progression) {
		this.mHardcoreProgression = progression;
	}
	
	public ProgressionModel getHardcoreProgression() {
		return this.mHardcoreProgression;
	}
	
	public List<Boolean> getProgressionBooleanList(ProgressionType type) {
		final List<Boolean> completionList = new ArrayList<Boolean>();
		if(type == ProgressionType.normal){
			completionList.addAll(getProgression().getNormal().getActsCompletion());
			completionList.addAll(getProgression().getNightmare().getActsCompletion());
			completionList.addAll(getProgression().getHell().getActsCompletion());
			completionList.addAll(getProgression().getInferno().getActsCompletion());
		}else{
			completionList.addAll(getHardcoreProgression().getNormal().getActsCompletion());
			completionList.addAll(getHardcoreProgression().getNightmare().getActsCompletion());
			completionList.addAll(getHardcoreProgression().getHell().getActsCompletion());
			completionList.addAll(getHardcoreProgression().getInferno().getActsCompletion());
		}
		
		return completionList;
	}
	
	public ArtisanModel getArtisan(ArtisanType type, boolean hardcore) {
		String slug = null;
		ArtisanModel artisanResult = null;
		if(type == ArtisanType.blacksmith){
			slug = ArtisanType.blacksmith.name();
		}else{
			slug = ArtisanType.jeweler.name();
		}
		List<ArtisanModel> artisans = null;
		if(hardcore){
			artisans = getHardcoreArtisans();
		}else{
			artisans = getArtisans();
		}
		
		for(ArtisanModel artisan : artisans) {
			if(artisan.getSlug().equalsIgnoreCase(slug)) {
				artisanResult = artisan;
				break;
			}
		}
		return artisanResult;
	}
	
	@Override
	public ContentValues getContentValues() {
		final ContentValues profileContentValues = new ContentValues();
		profileContentValues.put(IProfileModel.PROFILE_TAG_COLUMN, mBattleTag);
		profileContentValues.put(IProfileModel.SERVER_COLUMN, mServer);
		profileContentValues.put(LAST_HERO_PLAYED, mLastHeroPlayer);
		profileContentValues.put(LAST_UPDATED, mLastUpdated);
		profileContentValues.put(BARBARIAN_PLAYTIME_COLUMN, mTimePlayed.get(mBarbarianKey));
		profileContentValues.put(DEMON_HUNTER_PLAYTIME_COLUMN, mTimePlayed.get(mDemonHunterKey));
		profileContentValues.put(MONK_PLAYTIME_COLUMN, mTimePlayed.get(mMonkKey));
		profileContentValues.put(WITCH_DOCTOR_PLAYTIME_COLUMN, mTimePlayed.get(mWitchDoctorKey));
		profileContentValues.put(WIZARD_PLAYTIME_COLUMN, mTimePlayed.get(mWizardKey));
		profileContentValues.put(LIFETIME_KILLS_COLUMN, mKills.get(mLifeTimeKillsKey));
		profileContentValues.put(ELITES_KILLS_COLUMN, mKills.get(mElitesKillsKey));
		profileContentValues.put(HARDCORE_KILLS_COLUMN, mKills.get(mHardcoreKillsKey));
		return profileContentValues;
		
	}

	@Override
	public void setParentProfileTag(String profileTag) {}

	@Override
	public String getParentProfileTag() {
		return null;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
}
