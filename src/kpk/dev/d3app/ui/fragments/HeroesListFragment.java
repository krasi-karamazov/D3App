package kpk.dev.d3app.ui.fragments;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.HeroesListAdapter;
import kpk.dev.d3app.models.accountmodels.HeroModel;
import kpk.dev.d3app.tasks.GetHeroDataTask;
import kpk.dev.d3app.ui.activities.HeroDetailsActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class HeroesListFragment extends AbstractFragment<List<HeroModel>> {
	private List<HeroModel> mHeroes;
	private ListView mListView;
	private HeroesListAdapter mAdapter;
	@Override
	public void setData(List<HeroModel> data) {
		mHeroes = data;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.d3_list_layout, container, false);
		rootView.setBackgroundDrawable(null);
		mListView = (ListView)rootView.findViewById(R.id.d3list);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		mAdapter = new HeroesListAdapter(getActivity(), R.layout.profile_list_item, mHeroes);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(heroClickListener);
	}
	
	private AdapterView.OnItemClickListener heroClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> list, View item, int position, long id) {
			final Bundle bundle = new Bundle();
			bundle.putLong(GetHeroDataTask.HERO_ID_BUNDLE_KEY, mHeroes.get(position).getID());
			Intent intent = new Intent(getActivity(), HeroDetailsActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		};
	};

}
