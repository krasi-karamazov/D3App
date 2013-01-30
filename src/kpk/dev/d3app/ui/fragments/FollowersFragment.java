package kpk.dev.d3app.ui.fragments;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kpk.dev.d3app.R;
import kpk.dev.d3app.models.accountmodels.D3Follower;
import kpk.dev.d3app.util.KPKLog;

public class FollowersFragment extends AbstractFragment<Map<String, D3Follower>> {
	private Map<String, D3Follower> mFollowers;
	@Override
	public void setData(Map<String, D3Follower> data) {
		mFollowers = data;
		KPKLog.d("Followers size " + mFollowers.size());
		KPKLog.d("Followers slug " + mFollowers.get("templar").getSlug());
		KPKLog.d("Followers slug " + mFollowers.get("templar").getItems());
		//KPKLog.d("Followers slug " + mFollowers.get("templar").getSkills());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.followers_fragment_layout, container, false);
		return rootView;
	}
}
