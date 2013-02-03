package kpk.dev.d3app.ui.activities;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kpk.dev.d3app.R;
import kpk.dev.d3app.database.ProfilesDatabaseProcessor;
import kpk.dev.d3app.database.RegionsDatabaseProcessor;
import kpk.dev.d3app.listeners.BaseDataListener;
import kpk.dev.d3app.listeners.ServerStatusParsedListener;
import kpk.dev.d3app.models.accountmodels.IProfileModel;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.models.bnetmodels.BaseBattleNetModel;
import kpk.dev.d3app.models.bnetmodels.Region;
import kpk.dev.d3app.tasks.BaseJSONAsyncTask;
import kpk.dev.d3app.tasks.GetServerStatusTask;
import kpk.dev.d3app.tasks.BaseJSONAsyncTask.TaskType;
import kpk.dev.d3app.ui.fragments.BaseDialog;
import kpk.dev.d3app.ui.fragments.BaseDialog.DialogType;
import kpk.dev.d3app.ui.fragments.WarningDialogFragment;
import kpk.dev.d3app.ui.interfaces.IDialogWatcher;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
import kpk.dev.d3app.util.Utils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.ContactsContract.Profile;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AbstractActivity implements ServerStatusParsedListener, IDialogWatcher  {
	private ProgressBar mProgressBar;
	private static final String WARNING_DIALOG_TAG = "warning_dialog";
	private static final String PROGRESS_KEY = "progress";
	private int mProgress;
	private Bundle savedState;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedState = savedInstanceState;
		setContentView(R.layout.splash_screen_layout);
	}
	
	private void checkIfShouldUpdate() {
		ProfilesDatabaseProcessor dbProcessor = new ProfilesDatabaseProcessor();
		List<IProfileModel> profiles = dbProcessor.getProfiles(getDatabase());
		D3Constants.PROGRESS_STEPS_MAX = D3Constants.PROGRESS_STEPS_MAX + profiles.size();
		D3Constants.PROGRESS_INCREMENT = 100 / D3Constants.PROGRESS_STEPS_MAX;
		updateProfile(profiles, 0);
	}

	private void updateProfile(final List<IProfileModel> profiles, final int i) {
		Bundle bundle = new Bundle();
		ProfileModel model = (ProfileModel)profiles.get(i);
		bundle.putString(BaseJSONAsyncTask.REGION_BUNDLE_KEY, model.getServer());
		bundle.putString(BaseJSONAsyncTask.BATTLE_TAG_BUNDLE_KEY, model.getBattleTag());
		BaseJSONAsyncTask careerTask = BaseJSONAsyncTask.getTask(TaskType.CAREER, new BaseDataListener() {
			@Override
			public void dataReady(IProfileModel model, boolean newObject,
					String[] returnedArgs) {
				if(i < profiles.size() - 1){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mProgressBar.setProgress(mProgressBar.getProgress() + D3Constants.PROGRESS_INCREMENT);
						}
					});
					int index = i + 1;
					updateProfile(profiles, index);
				}else{
					KPKLog.d("FINISH");
					startTransition();
				}
			}
		}, getDatabase());
		careerTask.execute(bundle);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(PROGRESS_KEY, mProgressBar.getProgress());
	}
	
	private void checkConnectivity(Bundle savedState) {
		if(Utils.isConnectedToInternet(getApplicationContext())) {
			if(savedState != null){
				mProgress = savedState.getInt(PROGRESS_KEY, -1);
				if(mProgress != -1){
					mProgressBar.setProgress(mProgress);
				}
			}
			mProgressBar.setVisibility(View.VISIBLE);
			new GetServerStatusTask(this).start();
		}else{
			if(!Utils.getAppSharedPreferences(getApplicationContext()).getBoolean(D3Constants.FIRST_LOGIN_COMPLETE_KEY, false)){
				showInternetConnectivityWarningDialog();
			}else{
				startSplashScreenTimer();
			}
		}
	}

	private void startSplashScreenTimer() {
		Timer mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						mProgressBar.setVisibility(View.INVISIBLE);
						startActivity(new Intent(SplashScreenActivity.this, HomeScreenActivity.class));
						finish();
					}
				});
				
			}
		}, 3000);	
	}

	private void showInternetConnectivityWarningDialog() {
		final Bundle dialogData = new Bundle();
		dialogData.putString(BaseDialog.TITLE_KEY, getString(R.string.connectivity_alert_dialog_title));
		dialogData.putString(WarningDialogFragment.MESSAGE_KEY, getString(R.string.connectivity_alert_dialog_message));
		dialogData.putInt(BaseDialog.DIALOG_LAYOUT_KEY, R.layout.warning_dialog_background);
		DialogFragment warningDialogFragment = WarningDialogFragment.getInstance(dialogData);
		((BaseDialog)warningDialogFragment).setDialogWatcher(this);
		warningDialogFragment.show(getSupportFragmentManager(), WARNING_DIALOG_TAG);
	}

	@Override
	public void serverStatusParsed(List<Region> regions) {
		final SharedPreferences prefs = getSharedPreferences(D3Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
		RegionsDatabaseProcessor<BaseBattleNetModel> dbProcessor = new RegionsDatabaseProcessor<BaseBattleNetModel>();
		if(!prefs.getBoolean(D3Constants.FIRST_LOGIN_COMPLETE_KEY, false)){
			dbProcessor.insertData(regions, getDatabase());
			Editor editor = prefs.edit();
			editor.putBoolean(D3Constants.FIRST_LOGIN_COMPLETE_KEY, true);
			editor.commit();
		}else{
			try{
				dbProcessor.updateData(regions, getDatabase());
			}catch(IllegalStateException e) {
				
			}
		}
		checkIfShouldUpdate();
	}
	
	private void startTransition(){
		final Animation progressbarAnimation = AnimationUtils.loadAnimation(this, R.anim.progress_bar_animation);
		progressbarAnimation.setAnimationListener(new Animation.AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				
				mProgressBar.setProgress(D3Constants.PROGRESS_STEPS_MAX * D3Constants.PROGRESS_INCREMENT);
			}
			
			public void onAnimationRepeat(Animation animation) {}
			
			public void onAnimationEnd(Animation animation) {
				mProgressBar.setVisibility(View.GONE);
				startActivity(new Intent(SplashScreenActivity.this, HomeScreenActivity.class));
				finish();
			}
		});
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mProgressBar.startAnimation(progressbarAnimation);	
			}
		});
	}

	@Override
	public void serverStatusParseProgress() {
		mProgressBar.setProgress(mProgressBar.getProgress() + D3Constants.PROGRESS_INCREMENT);
	}

	@Override
	protected void initComponents() {
		mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
		mProgressBar.setProgress(0);
		mProgressBar.setMax(D3Constants.PROGRESS_STEPS_MAX * D3Constants.PROGRESS_INCREMENT);
		checkConnectivity(savedState);
	}

	@Override
	public void closeDialogs(String tag) {
		DialogFragment openDialogFragment = (DialogFragment)getSupportFragmentManager().findFragmentByTag(tag);
		openDialogFragment.dismiss();
		finish();
	}

	@Override
	public void closeDialogsWithData(DialogType type, String tag, Bundle dialogData) {}
}
