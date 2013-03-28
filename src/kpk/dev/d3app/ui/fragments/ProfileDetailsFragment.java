package kpk.dev.d3app.ui.fragments;

import kpk.dev.d3app.R;
import kpk.dev.d3app.models.accountmodels.ArtisanModel;
import kpk.dev.d3app.models.accountmodels.HeroModel;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.models.accountmodels.ProfileModel.ArtisanType;
import kpk.dev.d3app.models.accountmodels.ProfileModel.ProgressionType;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.widgets.ProgressionProgressBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileDetailsFragment extends AbstractFragment<ProfileModel> {
	private ProfileModel mProfile;
	private HeroModel mHero;
	private View mRootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.profile_details_layout, container, false);
		mHero = mProfile.getHeroByID(mProfile.getLastHeroPlayed());
		return mRootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupTexts();
		setupProgressbar();
		setupImages();
	}
	
	private void setupTexts() {
		final TextView mLifeTimeKills = (TextView)mRootView.findViewById(R.id.lifetime_kills_label);
		final TextView mEliteKills = (TextView)mRootView.findViewById(R.id.elite_kills_label);
		final TextView mLastPlayedHeroName = (TextView)mRootView.findViewById(R.id.last_played_hero_name);
		final TextView mBlackSmithLevels = (TextView)mRootView.findViewById(R.id.blacksmith_levels_field);
		final TextView mJewelerLevels = (TextView)mRootView.findViewById(R.id.jeweler_levels_field);
		
		mLifeTimeKills.setText(mLifeTimeKills.getText().toString() + "\n" + mProfile.getLifeTimeKills());
		mEliteKills.setText(mEliteKills.getText().toString() + "\n" + mProfile.getEliteKills());
		mLastPlayedHeroName.setText(mHero.getName());
		
		ArtisanModel blacksmithNormal = mProfile.getArtisan(ArtisanType.blacksmith, false);
		ArtisanModel blacksmithHardcore = mProfile.getArtisan(ArtisanType.blacksmith, true);
		ArtisanModel jewelerNormal = mProfile.getArtisan(ArtisanType.jeweler, false);
		ArtisanModel jewelerHardcore = mProfile.getArtisan(ArtisanType.jeweler, true);
		
		mBlackSmithLevels.setText("Level " + ((blacksmithNormal != null)?blacksmithNormal.getLevel():0) + " (Normal)" 
				+ "\n" + "Level " + ((blacksmithHardcore != null)?blacksmithHardcore.getLevel():0) + " (Hardcore)");
				
				mJewelerLevels.setText("Level " + ((jewelerNormal != null)?jewelerNormal.getLevel():0) + " (Normal)" 
						+ "\n" + "Level " + ((jewelerHardcore != null)?jewelerHardcore.getLevel():0) + " (Hardcore)");
	}
	
	private void setupProgressbar() {
		final ProgressionProgressBar mNormalProgressionBar = (ProgressionProgressBar)mRootView.findViewById(R.id.progression_bar_normal);
		mNormalProgressionBar.setDataArrayList(mProfile.getProgressionBooleanList(ProgressionType.normal));
		mNormalProgressionBar.init(Color.YELLOW);		
		final ProgressionProgressBar mHardcoreProgressionBar = (ProgressionProgressBar)mRootView.findViewById(R.id.progression_bar_hardcore);
		mHardcoreProgressionBar.setDataArrayList(mProfile.getProgressionBooleanList(ProgressionType.hardcore));
		mHardcoreProgressionBar.init(Color.RED);
	}
	
	private void setupImages() {
		final ImageView mLastPlayedHeroPortrait = (ImageView)mRootView.findViewById(R.id.last_played_hero_portrait);
		final ImageView mBlacksmithPortrait = (ImageView)mRootView.findViewById(R.id.blacksmith_portrait);
		final ImageView mJewelerPortrait = (ImageView)mRootView.findViewById(R.id.jeweler_portrait);
		setImage(mProfile.getLastPlayedHeroPortrait(), mLastPlayedHeroPortrait);
		setImage(D3Constants.BLACKSMITH_PORTRAIT_URL, mBlacksmithPortrait);
		setImage(D3Constants.JEWELER_PORTRAIT_URL, mJewelerPortrait);
	}
	
	@Override
	public void setData(ProfileModel data) {
		mProfile = data;
	}
}
