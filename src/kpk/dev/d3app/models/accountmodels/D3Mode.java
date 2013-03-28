package kpk.dev.d3app.models.accountmodels;

import java.util.ArrayList;
import java.util.List;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;

import android.content.ContentValues;

public class D3Mode implements IProfileModel {
	public static final String MODE_NAME_COLUMN = "mode_name";
	public static final String ACT1_COLUMN = "act1_completed";
	public static final String ACT2_COLUMN = "act2_completed";
	public static final String ACT3_COLUMN = "act3_completed";
	public static final String ACT4_COLUMN = "act4_completed";
	private D3Act act1;
	private D3Act act2;
	private D3Act act3;
	private D3Act act4;
	private String mName;
	private String mServer;
	
	public void setAct1(D3Act act){
		this.act1 = act;
	}
	
	public D3Act getAct1(){
		return this.act1;
	}
	
	public void setAct2(D3Act act){
		this.act2 = act;
	}
	
	public D3Act getAct2(){
		return this.act2;
	}
	
	public void setAct3(D3Act act){
		this.act3 = act;
	}
	
	public D3Act getAct3(){
		return this.act3;
	}
	
	public void setAct4(D3Act act){
		this.act4 = act;
	}
	
	public D3Act getAct4(){
		return this.act4;
	}
	
	public void setName(String name){
		mName = name;
	}
	
	public String getName(){
		return mName;
	}
	
	public List<Boolean> getActsCompletion() {
		final List<Boolean> actsCompletion = new ArrayList<Boolean>();
		actsCompletion.add(act1.getCompleted());
		actsCompletion.add(act2.getCompleted());
		actsCompletion.add(act3.getCompleted());
		actsCompletion.add(act4.getCompleted());
		return actsCompletion;
	}

	@Override
	public void setServer(String server) {
		mServer = server;
	}
	
	@Override
	public ContentValues getContentValues() {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(ACT1_COLUMN, (act1.getCompleted())?1:0);
		contentValues.put(ACT2_COLUMN, (act2.getCompleted())?1:0);
		contentValues.put(ACT3_COLUMN, (act3.getCompleted())?1:0);
		contentValues.put(ACT4_COLUMN, (act4.getCompleted())?1:0);
		contentValues.put(IProfileModel.SERVER_COLUMN, mServer);
		return contentValues;
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
