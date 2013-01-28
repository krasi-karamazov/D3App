package kpk.dev.d3app.ui.activities;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.HeroDataAdapter;
import kpk.dev.d3app.database.HeroDatabaseProcessor.HeroDataType;
import kpk.dev.d3app.listeners.DataReadyListener;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.models.accountmodels.IProfileModel;
import kpk.dev.d3app.tasks.GetHeroDataTask;
import kpk.dev.d3app.util.KPKLog;
import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

public class HeroDetailsActivity extends AbstractActivity {
	private long mHeroID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHeroID = getIntent().getExtras().getLong(GetHeroDataTask.HERO_ID_BUNDLE_KEY);
		if(mHeroID == -1) {
			finish();
			return;
		}
		setContentView(R.layout.hero_details_activity_layout);
	}
	
	private DataReadyListener mDataReadFromDBListener = new DataReadyListener() {
		@Override
		public void dataReadyListener(List<IProfileModel> models) {
			if(models != null && models.size() > 0) {
				displayData((HeroModelDecorator)models.get(0));
			}
		}
	};
	
	private void displayData(HeroModelDecorator heroModel) {
		KPKLog.d("hero id display" + heroModel.getHeroID());
		final ViewPager mViewPager = (ViewPager)findViewById(R.id.hero_data_pager);
		final PagerTitleStrip titleStrip = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
		Utils.setFont(titleStrip);
		final HeroDataAdapter mHeroDataAdapter = new HeroDataAdapter(getSupportFragmentManager(), heroModel);
		mViewPager.setAdapter(mHeroDataAdapter);
	}
	
	@Override
	protected void initComponents() {
		Bundle params = new Bundle();
		params.putLong(GetHeroDataTask.HERO_ID_BUNDLE_KEY, mHeroID);
		new GetHeroDataTask(mDataReadFromDBListener, getDatabase(), HeroDataType.BasicHeroData).execute(params);
	}
}
