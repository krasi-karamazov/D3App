package kpk.dev.d3app.database;

import java.util.List;

import kpk.dev.d3app.models.accountmodels.D3Act;
import kpk.dev.d3app.models.accountmodels.D3Mode;
import kpk.dev.d3app.models.accountmodels.HeroProgressionModel;
import kpk.dev.d3app.models.accountmodels.IProfileModel;
import kpk.dev.d3app.models.accountmodels.ProgressionModel;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DatabaseProcessorBase {
	
	public abstract boolean insertData(List<IProfileModel> items, SQLiteDatabase database);
	
	protected final synchronized ProgressionModel getProgression(Cursor progressionCursor){
		final ProgressionModel progressionModel = new ProgressionModel();
		int modeNameColumn = progressionCursor.getColumnIndexOrThrow(D3Mode.MODE_NAME_COLUMN);
		int act1Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT1_COLUMN);
		int act2Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT2_COLUMN);
		int act3Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT3_COLUMN);
		int act4Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT4_COLUMN);
		
		while(progressionCursor.moveToNext()) {
			D3Mode d3Mode = new D3Mode();
			d3Mode.setName(progressionCursor.getString(modeNameColumn));
			D3Act act1 = new D3Act();
			D3Act act2 = new D3Act();
			D3Act act3 = new D3Act();
			D3Act act4 = new D3Act();
			act1.setCompleted((progressionCursor.getInt(act1Column) == 1)?true:false);
			act2.setCompleted((progressionCursor.getInt(act2Column) == 1)?true:false);
			act3.setCompleted((progressionCursor.getInt(act3Column) == 1)?true:false);
			act4.setCompleted((progressionCursor.getInt(act4Column) == 1)?true:false);
			d3Mode.setAct1(act1);
			d3Mode.setAct2(act2);
			d3Mode.setAct3(act3);
			d3Mode.setAct4(act4);
			if(d3Mode.getName().equalsIgnoreCase("normal")){
				progressionModel.setNormal(d3Mode);
			}else if(d3Mode.getName().equalsIgnoreCase("nightmare")){
				progressionModel.setNightmare(d3Mode);
			}else if(d3Mode.getName().equalsIgnoreCase("hell")){
				progressionModel.setHell(d3Mode);
			}else if(d3Mode.getName().equalsIgnoreCase("inferno")){
				progressionModel.setInferno(d3Mode);
			}
		}
		progressionCursor.close();
		return progressionModel;
	}
	
	protected final synchronized HeroProgressionModel getHeroProgression(Cursor progressionCursor){
		final HeroProgressionModel progressionModel = new HeroProgressionModel();
		int modeNameColumn = progressionCursor.getColumnIndexOrThrow(D3Mode.MODE_NAME_COLUMN);
		int act1Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT1_COLUMN);
		int act2Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT2_COLUMN);
		int act3Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT3_COLUMN);
		int act4Column = progressionCursor.getColumnIndexOrThrow(D3Mode.ACT4_COLUMN);
		
		while(progressionCursor.moveToNext()) {
			D3Mode d3Mode = new D3Mode();
			d3Mode.setName(progressionCursor.getString(modeNameColumn));
			D3Act act1 = new D3Act();
			D3Act act2 = new D3Act();
			D3Act act3 = new D3Act();
			D3Act act4 = new D3Act();
			act1.setCompleted((progressionCursor.getInt(act1Column) == 1)?true:false);
			act2.setCompleted((progressionCursor.getInt(act2Column) == 1)?true:false);
			act3.setCompleted((progressionCursor.getInt(act3Column) == 1)?true:false);
			act4.setCompleted((progressionCursor.getInt(act4Column) == 1)?true:false);
			d3Mode.setAct1(act1);
			d3Mode.setAct2(act2);
			d3Mode.setAct3(act3);
			d3Mode.setAct4(act4);
			if(d3Mode.getName().equalsIgnoreCase("normal")){
				progressionModel.setNormal(d3Mode);
			}else if(d3Mode.getName().equalsIgnoreCase("nightmare")){
				progressionModel.setNightmare(d3Mode);
			}else if(d3Mode.getName().equalsIgnoreCase("hell")){
				progressionModel.setHell(d3Mode);
			}else if(d3Mode.getName().equalsIgnoreCase("inferno")){
				progressionModel.setInferno(d3Mode);
			}
		}
		progressionCursor.close();
		return progressionModel;
	}
}
