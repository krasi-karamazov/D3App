package kpk.dev.d3app.models.accountmodels;

import java.util.List;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;

import android.content.ContentValues;

public class D3Act implements IProfileModel {
	private boolean mCompleted;
	private String mParentProfileTag;
	private List<D3Quest> completedQuests;
	public D3Act(){
		
	}
	
	public void setCompleted(boolean isCompleted){
		this.mCompleted = isCompleted;
	}
	
	public boolean getCompleted(){
		return this.mCompleted;
	}
	
	public void setCompletedQuests(List<D3Quest> completedQuests){
		this.completedQuests = completedQuests;
	}
	
	public List<D3Quest> getCompletedQuests(){
		return this.completedQuests;
	}
	
	@Override
	public ContentValues getContentValues() {
		return null;
	}

	@Override
	public void setServer(String server) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParentProfileTag(String profileTag) {
		mParentProfileTag = profileTag;
	}

	@Override
	public String getParentProfileTag() {
		return mParentProfileTag;
	}
	
	@Override
	public String getTableName() {
		return null;
	}
}
