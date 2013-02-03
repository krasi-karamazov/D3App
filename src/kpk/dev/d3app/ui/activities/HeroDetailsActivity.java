package kpk.dev.d3app.ui.activities;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.HeroDataAdapter;
import kpk.dev.d3app.database.HeroDatabaseProcessor.HeroDataType;
import kpk.dev.d3app.listeners.DataReadyListener;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.models.accountmodels.IProfileModel;
import kpk.dev.d3app.tasks.BaseJSONAsyncTask;
import kpk.dev.d3app.tasks.GetHeroDataTask;
import kpk.dev.d3app.tasks.HeroAsyncTask;
import kpk.dev.d3app.ui.fragments.BaseDialog;
import kpk.dev.d3app.ui.fragments.BaseDialog.DialogType;
import kpk.dev.d3app.ui.fragments.ProgressDialogFragment;
import kpk.dev.d3app.ui.interfaces.IDialogWatcher;

import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;

public class HeroDetailsActivity extends AbstractActivity implements IDialogWatcher{
	private long mHeroID;
	private String mServer;
	private String mProfileTag;
	private static final String PROGRESS_DIALOG_TAG = "progress_dialog";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHeroID = getIntent().getExtras().getLong(GetHeroDataTask.HERO_ID_BUNDLE_KEY);
		mServer = getIntent().getExtras().getString(GetHeroDataTask.HERO_SERVER_BUNDLE_KEY);
		mProfileTag = getIntent().getExtras().getString(GetHeroDataTask.HERO_PROFILE_TAG);
		if(mHeroID == -1) {
			finish();
			return;
		}
		setContentView(R.layout.hero_details_activity_layout);
	}
	
	private DataReadyListener mDataReadFromDBListener = new DataReadyListener() {
		@Override
		public void dataReadyListener(List<IProfileModel> models) {
			if(models != null && models.size() > 0 && models.get(0) != null) {
				displayData((HeroModelDecorator)models.get(0));
				DialogFragment openDialogFragment = (DialogFragment)getSupportFragmentManager().findFragmentByTag(PROGRESS_DIALOG_TAG);
				if(openDialogFragment != null){
					openDialogFragment.dismiss();
				}
			}else{
				Bundle params = new Bundle();
				params.putLong(BaseJSONAsyncTask.HERO_ID_BUNDLE_KEY, mHeroID);
				params.putString(BaseJSONAsyncTask.REGION_BUNDLE_KEY, mServer);
				params.putString(BaseJSONAsyncTask.BATTLE_TAG_BUNDLE_KEY, mProfileTag);
				new HeroAsyncTask(this, getDatabase()).execute(params);
			}
		}

		@Override
		public void dataReady(IProfileModel model, boolean newObject,
				String[] returnedArgs) {
			Bundle params = new Bundle();
			params.putLong(GetHeroDataTask.HERO_ID_BUNDLE_KEY, mHeroID);
			new GetHeroDataTask(mDataReadFromDBListener, getDatabase(), HeroDataType.BasicHeroData).execute(params);
			
		}
	};
	
	private void displayData(HeroModelDecorator heroModel) {
		final ViewPager mViewPager = (ViewPager)findViewById(R.id.hero_data_pager);
		final PagerTitleStrip titleStrip = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
		Utils.setFont(titleStrip);
		final HeroDataAdapter mHeroDataAdapter = new HeroDataAdapter(getSupportFragmentManager(), heroModel);
		mViewPager.setAdapter(mHeroDataAdapter);
	}
	
	@Override
	protected void initComponents() {
		showProgressDialog();
		Bundle params = new Bundle();
		params.putLong(GetHeroDataTask.HERO_ID_BUNDLE_KEY, mHeroID);
		new GetHeroDataTask(mDataReadFromDBListener, getDatabase(), HeroDataType.BasicHeroData).execute(params);
	}
	private void showProgressDialog() {
		final Bundle dialogData = new Bundle();
		dialogData.putInt(BaseDialog.DIALOG_LAYOUT_KEY, R.layout.progress_dialog_layout);
		DialogFragment progressDialog = ProgressDialogFragment.getInstance(dialogData);
		((BaseDialog)progressDialog).setDialogWatcher(this);
		progressDialog.setCancelable(false);
		progressDialog.show(getSupportFragmentManager(), PROGRESS_DIALOG_TAG);
	}

	@Override
	public void closeDialogs(String tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeDialogsWithData(DialogType type, String tag,
			Bundle dialogData) {
		// TODO Auto-generated method stub
		
	}
}
