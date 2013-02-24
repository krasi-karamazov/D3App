package kpk.dev.d3app.ui.activities;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.ProfileDataAdapter;
import kpk.dev.d3app.listeners.DataReadyListener;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import kpk.dev.d3app.tasks.ModelType;
import kpk.dev.d3app.tasks.ReadRecordsTask;
import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

public class ProfileDetailsActivity extends AbstractActivity {
	private int mRequiredProfileID;
	private ProfileModel mProfile;
	private ViewPager mViewPager;
	private ProfileDataAdapter mProfileDataAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRequiredProfileID = getIntent().getIntExtra(ProfilesListActivity.SELECTED_PROFILE_ID_KEY, -1); 
		if(mRequiredProfileID == -1) {
			finish();
			return;
		}
		setContentView(R.layout.profile_activity_layout);
	}
	
	private void getSelectedProfile() {
		ModelType modeltype = ModelType.profile;
		modeltype.profile(getIntent().getStringExtra(ProfilesListActivity.SELECTED_PROFILE_SERVER), 
				getIntent().getStringExtra(ProfilesListActivity.SELECTED_PROFILE_BATTLE_TAG));
		new ReadRecordsTask(getDatabase(), mDataReadFromDBListener).execute(modeltype);
	} 
	private DataReadyListener mDataReadFromDBListener = new DataReadyListener() {
		
		@Override
		public void dataReadyListener(List<IProfileModel> models) {
			if(models.size() > 0){
				mProfile = (ProfileModel)models.get(0);
				displayData();
			}
		}

		@Override
		public void dataReady(IProfileModel model, boolean newObject,
				String[] returnedArgs) {
		}
	};

	private void displayData() {
		mViewPager = (ViewPager)findViewById(R.id.profile_data_pager);
		final PagerTitleStrip titleStrip = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
		Utils.setFont(titleStrip);
		mProfileDataAdapter = new ProfileDataAdapter(getSupportFragmentManager(), mProfile);
		mViewPager.setAdapter(mProfileDataAdapter);
	}
	
	@Override
	protected void initComponents() {
		getSelectedProfile();
	}
}
