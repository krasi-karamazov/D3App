package kpk.dev.d3app.adapters;

import java.util.List;

import kpk.dev.d3app.models.bnetmodels.Region;
import kpk.dev.d3app.ui.fragments.AbstractFragment;
import kpk.dev.d3app.ui.fragments.RegionFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class RegionsPagerAdapter extends FragmentStatePagerAdapter {

	private List<Region> mRegions;
	public RegionsPagerAdapter(FragmentManager fm, List<Region> regions) {
		super(fm);
		mRegions = regions;
	}
	
	@Override
	public Fragment getItem(int position) {
		AbstractFragment<Region> regionFragment = new RegionFragment();
		regionFragment.setData(mRegions.get(position));
		return (Fragment)regionFragment;
	}

	@Override
	public int getCount() {
		return mRegions.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mRegions.get(position).getName();
	}

}
