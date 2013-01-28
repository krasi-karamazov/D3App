package kpk.dev.d3app.ui.fragments;

import java.util.HashMap;
import java.util.Map;

import kpk.dev.d3app.util.KPKLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HeroStatsFragment extends AbstractFragment<Map<String, Number>> {
	private Map<String, Number> mStats;
	private Map<String, String> mMainAttributes;
	private Map<String, String> mOffensive;
	@Override
	public void setData(Map<String, Number> data) {
		mStats = data;
		organizeData();
	}
	
	private void organizeData() {
		setupMainAttributes();
		setupOffensive();
		KPKLog.d(mMainAttributes.toString());
		KPKLog.d(mOffensive.toString());
	}

	private void setupOffensive() {
		mOffensive = new HashMap<String, String>();
		mOffensive.put("Damage increase", Float.valueOf(mStats.get("damageIncrease").floatValue()).toString() + "%");
		mOffensive.put("Critical hit chance", Float.valueOf(mStats.get("critChance").floatValue()).toString() + "%");
		mOffensive.put("Critical hit damage", Float.valueOf(mStats.get("critDamage").floatValue()).toString() + "%");
	}

	private void setupMainAttributes() {
		mMainAttributes = new HashMap<String, String>();
		mMainAttributes.put("Strength", Integer.valueOf(mStats.get("strength").intValue()).toString());
		mMainAttributes.put("Dexterity", Integer.valueOf(mStats.get("dexterity").intValue()).toString());
		mMainAttributes.put("Intelligence", Integer.valueOf(mStats.get("intelligence").intValue()).toString());
		mMainAttributes.put("Vitality", Integer.valueOf(mStats.get("vitality").intValue()).toString());
		mMainAttributes.put("Armor", Integer.valueOf(mStats.get("armor").intValue()).toString());
		mMainAttributes.put("Damage", Integer.valueOf(mStats.get("damage").intValue()).toString());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
