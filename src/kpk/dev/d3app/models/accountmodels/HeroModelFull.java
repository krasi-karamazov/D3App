package kpk.dev.d3app.models.accountmodels;

import java.util.List;
import java.util.Map;

import android.content.ContentValues;

public class HeroModelFull extends HeroModel implements IProfileModel {
	public static final String TABLE_NAME = "D3HeroFullTable";
	public static final String HERO_ID_COLUMN = "hero_id";
	public static final String HERO_NAME_COLUMN = "hero_name";
	public static final String HERO_LEVEL_COLUMN = "hero_level";
	public static final String HERO_HARDCORE_COLUMN = "hero_is_hardcore";
	public static final String HERO_PARAGON_LEVEL_COLUMN = "hero_paragon_level";
	public static final String HERO_GENDER_COLUMN = "hero_gender";
	public static final String HERO_DEAD_COLUMN = "hero_is_dead";
	public static final String HERO_CLASS_COLUMN = "hero_class";
	public static final String HERO_LAST_UPDATED_COLUMN = "hero_last_updated";
	 
	private Map<String, Double> mStats;
	private SkillsModel mSkills;
	private List<D3Follower> mFollowers;
	private int mKills;
	private ProgressionModel mProgress;
	
	public void setProgress(ProgressionModel progress) {
		mProgress = progress;
	}
	
	public ProgressionModel getProgress() {
		return mProgress;
	}
	
	public void setKills(int kills) {
		mKills = kills;
	}
	
	public int getKills() {
		return mKills;
	}
	
	public void setStats(Map<String, Double> stats) {
		this.mStats = stats;
	}
	
	public Map<String, Double> getStats() {
		return this.mStats;
	}
	
	public void setSkills(SkillsModel skills) {
		mSkills = skills;
	}
	
	public SkillsModel getSkills() {
		return mSkills;
	}
	
	public void setFollowers(List<D3Follower> followers) {
		mFollowers = followers;
	}
	
	public List<D3Follower> getFollowers() {
		return mFollowers;
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
