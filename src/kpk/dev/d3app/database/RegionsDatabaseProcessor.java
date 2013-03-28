package kpk.dev.d3app.database;

import java.util.ArrayList;
import java.util.List;

import kpk.dev.d3app.models.bnetmodels.BaseBattleNetModel;
import kpk.dev.d3app.models.bnetmodels.Region;
import kpk.dev.d3app.models.bnetmodels.Server;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RegionsDatabaseProcessor<T extends BaseBattleNetModel> {
	private List<Server> mRegionsServers = new ArrayList<Server>();
	public RegionsDatabaseProcessor() {}
	
	public synchronized void insertData(List<Region> items, SQLiteDatabase database) {
		database.beginTransaction();
		try{
			for(Region item : items) {
				database.insert(item.getTableName(), "null", item.getContentValues());
				mRegionsServers.addAll(item.getServers());
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
		new ServersDatabaseProcessor().insertData(mRegionsServers, database);
	}
	
	public synchronized void updateData(List<Region> items, SQLiteDatabase database) {
		database.beginTransaction();
		try{
			for(Region item : items) {
				String[] args = {item.getName()};
				database.update(item.getTableName(), item.getContentValues(), Region.REGION_COLUMN + "=?", args);
				mRegionsServers.addAll(item.getServers());
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
		new ServersDatabaseProcessor().updateData(mRegionsServers, database);
	}
	
	public synchronized List<Region> getRegions(SQLiteDatabase database){
		Cursor regionsCursor = database.rawQuery("SELECT * FROM " + Region.TABLE_NAME, null);
		final List<Region> regions = new ArrayList<Region>();
		while(regionsCursor.moveToNext()){
			Region region = new Region();
			int regionNameColumnIndex = regionsCursor.getColumnIndexOrThrow(Region.REGION_COLUMN);
			region.setName(regionsCursor.getString(regionNameColumnIndex));
			List<Server> serversForRegion = new ServersDatabaseProcessor().getServersForRegion(region.getName(), database);
			region.setServers(serversForRegion);
			regions.add(region);
		}
		return regions;
	}
}
