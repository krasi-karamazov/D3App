package kpk.dev.d3app.ui.activities;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import kpk.dev.d3app.R;
import kpk.dev.d3app.application.StartUpComposite;
import kpk.dev.d3app.application.StartUpComposite.StartUpMode;
import kpk.dev.d3app.database.ProfilesDatabaseProcessor;
import kpk.dev.d3app.database.RegionsDatabaseProcessor;
import kpk.dev.d3app.listeners.BaseDataListener;
import kpk.dev.d3app.listeners.ICleanUpCompletedListener;
import kpk.dev.d3app.listeners.ServerStatusParsedListener;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.models.accountmodels.interfaces.IProfileModel;
import kpk.dev.d3app.models.bnetmodels.BaseBattleNetModel;
import kpk.dev.d3app.models.bnetmodels.Region;
import kpk.dev.d3app.tasks.BaseJSONAsyncTask;
import kpk.dev.d3app.tasks.CleanupTask;
import kpk.dev.d3app.tasks.GetServerStatusTask;
import kpk.dev.d3app.tasks.BaseJSONAsyncTask.TaskType;
import kpk.dev.d3app.ui.fragments.BaseDialog;
import kpk.dev.d3app.ui.fragments.BaseDialog.DialogType;
import kpk.dev.d3app.ui.fragments.WarningDialogFragment;
import kpk.dev.d3app.ui.interfaces.IDialogWatcher;
import kpk.dev.d3app.util.D3Constants;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import kpk.dev.d3app.util.KPKLog;

public class SplashScreenActivity extends AbstractActivity implements ServerStatusParsedListener, IDialogWatcher, ICleanUpCompletedListener {
	private ProgressBar mProgressBar;
	private static final String WARNING_DIALOG_TAG = "warning_dialog";
	private static final String PROGRESS_KEY = "progress";
	private int mProgress;
	private Bundle mSavedState;
    private Handler mHandler;
	private StartUpComposite mStartUpComposite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_layout);
		mStartUpComposite = new StartUpComposite(this);
        mHandler = new Handler();
	}
	
	private void startProfilesUpdate() {
		ProfilesDatabaseProcessor dbProcessor = new ProfilesDatabaseProcessor();
		List<IProfileModel> profiles = dbProcessor.getProfiles(getDatabase());
		D3Constants.PROGRESS_STEPS_MAX = D3Constants.PROGRESS_STEPS_MAX + profiles.size();
		D3Constants.PROGRESS_INCREMENT = 100 / D3Constants.PROGRESS_STEPS_MAX;
		updateProfile(profiles, 0);
	}

	private void updateProfile(final List<IProfileModel> profiles, final int i) {
		//KPKLog.d("Loading profile");
		Bundle bundle = new Bundle();
		if(profiles == null || profiles.size() < 1){
			new GetServerStatusTask(SplashScreenActivity.this).start();
			return;
		}
		ProfileModel model = (ProfileModel)profiles.get(i);
		bundle.putString(BaseJSONAsyncTask.REGION_BUNDLE_KEY, model.getServer());
		bundle.putString(BaseJSONAsyncTask.BATTLE_TAG_BUNDLE_KEY, model.getBattleTag());
		BaseJSONAsyncTask careerTask = BaseJSONAsyncTask.getTask(TaskType.CAREER, new BaseDataListener() {
			@Override
			public void dataReady(IProfileModel model, boolean newObject,
					String[] returnedArgs) {
				if(i < profiles.size() - 1){
					int index = i + 1;
					updateProfile(profiles, index);
				}else{
					new GetServerStatusTask(SplashScreenActivity.this).start();
				}
			}
		}, getDatabase());
		careerTask.execute(bundle);
	}

	public void startSplashScreenTimer() {
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

	public void showInternetConnectivityWarningDialog() {
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
            performCleanup();
		}else{
			try{
				dbProcessor.updateData(regions, getDatabase());
                performCleanup();
			}catch(IllegalStateException e) {
				
			}
		}
	}

    private void performCleanup() {
        final int cleanUpTimeIndex = getSharedPreferences(D3Constants.SHARED_PREFERENCES_FILE, MODE_APPEND).getInt(D3Constants.SHARED_PREFS_CACHE_OPTIONS_KEY, 3);
        if(cleanUpTimeIndex != -1){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    long time = Long.valueOf(getResources().getStringArray(R.array.delete_images_values)[cleanUpTimeIndex]);
                    new CleanupTask(SplashScreenActivity.this).execute(time);
                }
            });
        }
    }
	
	private void startTransition() {
		final Animation progressbarAnimation = AnimationUtils.loadAnimation(this, R.anim.progress_bar_animation);
		progressbarAnimation.setAnimationListener(new Animation.AnimationListener() {
			
			public void onAnimationStart(Animation animation) {}
			
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
		//KPKLog.d("Loading stuff");
	}

	@Override
	protected void initComponents() {
		mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
		checkConnectivity(mSavedState);
	}
	
	private void checkConnectivity(Bundle savedState) {
		StartUpMode startUpMode = mStartUpComposite.init();
		if(startUpMode.name().equalsIgnoreCase(StartUpMode.FirstTimeWithConnection.name())){
			mProgressBar.setVisibility(View.VISIBLE);
			new GetServerStatusTask(this).start();
		}else if(startUpMode.name().equalsIgnoreCase(StartUpMode.FirstTimeWithoutConnection.name())){
			showInternetConnectivityWarningDialog();
		}else if(startUpMode.name().equalsIgnoreCase(StartUpMode.NotFirstTimeWithoutConnection.name())){
			startSplashScreenTimer();
		}else if(startUpMode.name().equalsIgnoreCase(StartUpMode.NotFirstTimeWithConnection.name())){
			mProgressBar.setVisibility(View.VISIBLE);
			startServerAndProfilesUpdate();
		}
	}

	private void startServerAndProfilesUpdate() {
		long lastUpdateTime = mStartUpComposite.getLastUpdateTime();
		long updatePeriod = mStartUpComposite.getUpdatePeriod();
		new GetServerStatusTask(SplashScreenActivity.this).start();
		if(System.currentTimeMillis() - lastUpdateTime >= updatePeriod){
			startProfilesUpdate();
		}
	}

	@Override
	public void closeDialogs(String tag) {
		DialogFragment openDialogFragment = (DialogFragment)getSupportFragmentManager().findFragmentByTag(tag);
		if(openDialogFragment != null){
			openDialogFragment.dismiss();
		}
		finish();
	}

	@Override
	public void closeDialogsWithData(DialogType type, String tag, Bundle dialogData) {}

    @Override
    public void cleanUpCompleted() {
        startTransition();
    }
}
