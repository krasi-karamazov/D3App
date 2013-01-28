package kpk.dev.d3app.data;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import kpk.dev.d3app.listeners.BaseDataListener;
import kpk.dev.d3app.tasks.BaseJSONAsyncTask;
import kpk.dev.d3app.tasks.BaseJSONAsyncTask.TaskType;

public class D3DataManager {
	private static D3DataManager sInstance;
	private D3DataManager() {
		
	}
	
	public static D3DataManager getDataManager(){
		if(sInstance == null){
			sInstance = new D3DataManager();
		}
		return sInstance;
	}
	
	public synchronized void constructAndExecuteDataTask(TaskType type, Bundle args, BaseDataListener aListener, SQLiteDatabase aDatabase){
		BaseJSONAsyncTask task = BaseJSONAsyncTask.getTask(type, aListener, aDatabase);
		task.execute(args);
	}
}
