package kpk.dev.d3app.ui.activities;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.RegionsPagerAdapter;
import kpk.dev.d3app.database.RegionsDatabaseProcessor;
import kpk.dev.d3app.listeners.DatabaseReadyListener;
import kpk.dev.d3app.models.bnetmodels.BaseBattleNetModel;
import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

public class ServersViewerActivity extends AbstractActivity implements DatabaseReadyListener {
	
	private ViewPager mViewPager;
	private RegionsPagerAdapter mRegionsadapter;
	@Override
	protected void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.server_checker_layout);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void initComponents() {
		RegionsDatabaseProcessor<BaseBattleNetModel> dbProcessor = new RegionsDatabaseProcessor<BaseBattleNetModel>();
		mViewPager = (ViewPager)findViewById(R.id.server_pager);
		final PagerTabStrip titleStrip = (PagerTabStrip)findViewById(R.id.pager_title_strip);
		Utils.setFont(titleStrip);
		mRegionsadapter = new RegionsPagerAdapter(getSupportFragmentManager(), dbProcessor.getRegions(getDatabase()));
		mViewPager.setAdapter(mRegionsadapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mViewPager = null;
		mRegionsadapter = null;
		System.gc();
	}
}
