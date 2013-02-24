package kpk.dev.d3app.models.accountmodels;

import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import android.content.ContentValues;

public class ProgressionModel implements IProfileModel {

	public static final String TABLE_NAME = "d3progressiontable";
	public static final String HARDCORE_TABLE_NAME = "d3hardcoreprogressiontable";
	private D3Mode normal;
	private D3Mode nightmare;
	private D3Mode hell;
	private D3Mode inferno;
	private String mParentProfileTag;
	private String mServer;
	public ProgressionModel(){
		
	}
	
	public void setNormal(D3Mode normal){
		this.normal = normal;
	}
	
	public D3Mode getNormal(){
		return this.normal;
	}
	
	public void setNightmare(D3Mode nightmare){
		this.nightmare = nightmare;
	}
	
	public D3Mode getNightmare(){
		return this.nightmare;
	}
	
	public void setHell(D3Mode hell){
		this.hell = hell;
	}
	
	public D3Mode getHell(){
		return this.hell;
	}
	
	public void setInferno(D3Mode inferno){
		this.inferno = inferno;
	}
	
	public D3Mode getInferno(){
		return this.inferno;
	}

	@Override
	public void setServer(String server) {
		mServer = server;
	}
	
	@Override
	public ContentValues getContentValues() {
		return null;
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

	public String getServer() {
		return mServer;
	}

}
