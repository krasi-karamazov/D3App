package kpk.dev.d3app.models.bnetmodels;

import java.util.List;

import android.content.ContentValues;

public class Region extends BaseBattleNetModel {
	private List<Server> mServersList;
	public static String REGION_COLUMN = "region_name";
	public static String TABLE_NAME = "regions_table";
	public Region(String name, List<Server> servers) {
		super(name);
		mServersList = servers;
	}
	
	public Region(){
		super();
	}
	
	public void setServers(List<Server> servers) {
		mServersList = servers;
	}
	
	public List<Server> getServers() {
		return mServersList;
	}
	
	public Server getServerAt(int index){
		return mServersList.get(index);
	}
	
	public Server getServerByName(String name){
		Server server = null;
		for(Server serv : mServersList) {
			if(serv.getName().equalsIgnoreCase(name)){
				server = serv;
				break;
			}
		}
		return server;
	}
	
	@Override
	protected void formContentValues(ContentValues contentValues) {
		contentValues.put(REGION_COLUMN, mName);
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
	public boolean isRegion() {
		return true;
	}
}
