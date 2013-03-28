package kpk.dev.d3app.ui.fragments;

import java.util.Map;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.PlayTimeAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class PlayedTimesFragment extends AbstractFragment<Map<String, Double>> {
	
	private Map<String, Double> mPlayTimes;
	private GridView mTimesPlayedGrid;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View viewRoot = inflater.inflate(R.layout.play_times_layout, container, false);
		mTimesPlayedGrid = (GridView)viewRoot.findViewById(R.id.playtime_grid);
		return viewRoot;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		mTimesPlayedGrid.setAdapter(new PlayTimeAdapter(getActivity(), mPlayTimes));
	}
	
	@Override
	public void setData(Map<String, Double> data) {
		mPlayTimes = data;
	}
}
