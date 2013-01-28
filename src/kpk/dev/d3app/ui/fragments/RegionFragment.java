package kpk.dev.d3app.ui.fragments;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.ServersAdapter;
import kpk.dev.d3app.models.bnetmodels.Region;
import kpk.dev.d3app.models.bnetmodels.Server;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RegionFragment extends AbstractFragment<Region> {
	private ListView mListView;
	private ServersAdapter serversAdapter;
	private List<Server> mServers;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.region_fragment_layout, container, false);
		mListView = (ListView)rootView.findViewById(R.id.servers_list);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		serversAdapter = new ServersAdapter(getActivity(), R.layout.server_item_layout, mServers);
		mListView.setAdapter(serversAdapter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	public void setData(Region data) {
		mServers = data.getServers();
	}
}
