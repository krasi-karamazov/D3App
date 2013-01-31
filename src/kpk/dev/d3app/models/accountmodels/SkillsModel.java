package kpk.dev.d3app.models.accountmodels;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

//import android.content.ContentValues;

public class SkillsModel {
	private List<D3Skill> mActive;
	private List<D3PassiveSkill> mPassive;
	public SkillsModel() {
		
	}
	@JsonSetter("active")
	public void setActive(List<D3Skill> active) {
		mActive = active;
	}
	@JsonGetter("active")
	public List<D3Skill> getActive() {
		return mActive;
	}
	
	@JsonSetter("passive")
	public void setPassive(List<D3PassiveSkill> passive) {
		mPassive = passive;
	}
	
	@JsonGetter("passive")
	public List<D3PassiveSkill> getPassive() {
		return mPassive;
	}
	
	public List<ContentValues> getActiveSkillsContentValues(long heroID, String followerSlug, List<D3Skill> skills) {
		final List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
		for(D3Skill skill : skills) {
			if(skill == null) continue;
			ContentValues contentValues = skill.getSkillContentValues(followerSlug, heroID);
			contentValuesList.add(contentValues);
		}
		return contentValuesList;
	}
	
	public List<ContentValues> getFollowerSkillsContentValues(long heroID, String followerSlug, List<D3FollowerSkill> skills) {
		final List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
		for(D3FollowerSkill skill : skills) {
			if(skill == null) continue;
			//KPKLog.d(skill.getName());
			ContentValues contentValues = skill.getSkillContentValues(followerSlug, heroID);
			contentValuesList.add(contentValues);
		}
		return contentValuesList;
	}
	
	public List<ContentValues> getPassiveSkillsValues(long heroID) {
		final List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
		for(D3PassiveSkill skill : mPassive) {
			if(skill == null) continue;
			ContentValues contentValues = skill.getPassiveSkillContentValues(heroID);
			contentValuesList.add(contentValues);
		}
		return contentValuesList;
	}
	
	public List<ContentValues> getRunesContentValues(long heroID) {
		final List<ContentValues> contentValuesList = new ArrayList<ContentValues>();
		for(D3Skill skill : mActive) {
			if(skill == null) continue;
			D3Rune rune = skill.getRune();
			if(rune == null) continue;
			ContentValues contentValues = rune.getRuneContentValues(heroID, skill.getSkill().getSlug());
			contentValuesList.add(contentValues);
		}
		return contentValuesList;
	}
}
