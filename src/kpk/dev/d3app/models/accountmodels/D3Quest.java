package kpk.dev.d3app.models.accountmodels;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import android.content.ContentValues;

public class D3Quest implements IProfileModel {
	private String mSlug;
	private String mName;
	
	public D3Quest(){
		
	}
	
	public void setName(String name){
		this.mName = name;
	}
	public String getName(){
		return this.mName;
	}
	
	public void setSlug(String slug){
		this.mSlug = slug;
	}
	public String getSlug(){
		return this.mSlug;
	}

	@Override
	public void setServer(String server) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public ContentValues getContentValues() {
		return null;
	}

	@Override
	public void setParentProfileTag(String profileTag) {}

	@Override
	public String getParentProfileTag() {
		return null;
	}
	
	@Override
	public String getTableName() {
		return null;
	}
}
