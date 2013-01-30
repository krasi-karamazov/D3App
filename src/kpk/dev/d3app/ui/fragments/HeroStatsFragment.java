package kpk.dev.d3app.ui.fragments;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kpk.dev.d3app.R;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HeroStatsFragment extends AbstractFragment<Map<String, Number>> {
	private Map<String, Number> mStats;
	private Map<String, Map<String, String>> organizedStats;
	private TextView statsTitle1;
	private TextView statsText1;
	private TextView statsTitle2;
	private TextView statsText2;
	private TextView statsTitle3;
	private TextView statsText3;
	private TextView statsTitle4;
	private TextView statsText4;
	private TextView statsTitle5;
	private TextView statsText5;
	@Override
	public void setData(Map<String, Number> data) {
		mStats = data;
		organizedStats = Utils.getOrganizedStatMaps(mStats);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.hero_stats_fragment_layout, container, false);
		initComps(rootView);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		Set<Entry<String, Map<String, String>>>  statsSet = organizedStats.entrySet();
		for(Entry<String, Map<String, String>> entry : statsSet){
			setupTexts(entry);
		}
	}

	private void setupTexts(Entry<String, Map<String, String>> entry) {
		TextView titleView = null;
		TextView statsView = null;
		if(entry.getKey().equalsIgnoreCase(D3Constants.MAIN_ATTRIBUTES_KEY)){
			titleView = statsTitle1;
			statsView = statsText1;
		}else if(entry.getKey().equalsIgnoreCase(D3Constants.OFFENSIVE_KEY)){
			titleView = statsTitle2;
			statsView = statsText2;
		}else if(entry.getKey().equalsIgnoreCase(D3Constants.LIFE_KEY)){
			titleView = statsTitle3;
			statsView = statsText3;
		}else if(entry.getKey().equalsIgnoreCase(D3Constants.DEFENSIVE_KEY)){
			titleView = statsTitle4;
			statsView = statsText4;
		}else if(entry.getKey().equalsIgnoreCase(D3Constants.ADVENTURE_KEY)){
			titleView = statsTitle5;
			statsView = statsText5;
		}
		Utils.writeStatsStrings(entry, titleView, statsView);
	}

	private void initComps(View root) {
		statsTitle1 = (TextView)root.findViewById(R.id.stats_category_title_1);
		statsText1 = (TextView)root.findViewById(R.id.stats_category_1);
		statsTitle2 = (TextView)root.findViewById(R.id.stats_category_title_2);
		statsText2 = (TextView)root.findViewById(R.id.stats_category_2);
		statsTitle3 = (TextView)root.findViewById(R.id.stats_category_title_3);;
		statsText3 = (TextView)root.findViewById(R.id.stats_category_3);
		statsTitle4 = (TextView)root.findViewById(R.id.stats_category_title_4);;
		statsText4 = (TextView)root.findViewById(R.id.stats_category_4);
		statsTitle5 = (TextView)root.findViewById(R.id.stats_category_title_5);;
		statsText5 = (TextView)root.findViewById(R.id.stats_category_5);
	}
}
