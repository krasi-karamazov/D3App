package kpk.dev.d3app.adapters;

import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.ui.fragments.HeroesListFragment;
import kpk.dev.d3app.ui.fragments.PlayedTimesFragment;
import kpk.dev.d3app.ui.fragments.ProfileDetailsFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProfileDataAdapter extends FragmentStatePagerAdapter {

	private ProfileModel mProfile;
	public ProfileDataAdapter(FragmentManager fm, ProfileModel profile) {
		super(fm);
		mProfile = profile;
	}
	
	@Override
	public Fragment getItem(int position) {
		if(mProfile == null) return new Fragment();
		Fragment fragment = null;
		if(position == 0){
			fragment = new ProfileDetailsFragment();
			((ProfileDetailsFragment)fragment).setData(mProfile);
		}else if(position == 1) {
			fragment = new PlayedTimesFragment();
			((PlayedTimesFragment)fragment).setData(mProfile.getTimePlayed());
		}else if(position == 2) {
			fragment = new HeroesListFragment();
			((HeroesListFragment)fragment).setData(mProfile.getHeroes());
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
	
	private String[] titles = {
		"Profile summary",
		"Time played per class",
		"Heroes"
	};
}
