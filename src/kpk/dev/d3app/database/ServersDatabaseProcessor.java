package kpk.dev.d3app.database;

import java.util.ArrayList;
import java.util.List;

import kpk.dev.d3app.models.bnetmodels.Server;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ServersDatabaseProcessor {
	
	public ServersDatabaseProcessor() {
	}
	
	public void insertData(List<Server> servers, SQLiteDatabase database) {
		database.beginTransaction();
		try{
			for(Server server : servers){
				database.insert(server.getTableName(), "null", server.getContentValues());
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	public void updateData(List<Server> servers, SQLiteDatabase database) {
		database.beginTransaction();
		try{
			for(Server server : servers){
				String[] args = {server.getName(), server.getRegion()};
				database.update(server.getTableName(), server.getContentValues(), Server.NAME_COLUMN + "=? AND " + Server.REGION_COLUMN + "=?", args);
			}
			database.setTransactionSuccessful();
		}finally{
			database.endTransaction();
		}
	}
	
	public List<Server> getServersForRegion(String regionName, SQLiteDatabase database) {
		String[] columns = {Server.NAME_COLUMN, Server.AVAILABLE_COLUMN, Server.REGION_COLUMN};
		String selection = Server.REGION_COLUMN + "=?";
		String[] selectionArgs = {regionName};
		final List<Server> servers = new ArrayList<Server>();
		Cursor serverCursor = database.query(Server.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
		while(serverCursor.moveToNext()){
			int nameColumnIndex = serverCursor.getColumnIndexOrThrow(Server.NAME_COLUMN);
			int isAvailableColumnIndex = serverCursor.getColumnIndexOrThrow(Server.AVAILABLE_COLUMN);
			int regionColumnIndex = serverCursor.getColumnIndexOrThrow(Server.REGION_COLUMN);
			servers.add(new Server(serverCursor.getString(nameColumnIndex), (serverCursor.getInt(isAvailableColumnIndex) == 1)?"available":"not available", serverCursor.getString(regionColumnIndex)));
		}
		serverCursor.close();
		return servers;
	}
}
