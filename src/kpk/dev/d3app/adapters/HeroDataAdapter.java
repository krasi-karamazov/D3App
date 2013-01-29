package kpk.dev.d3app.adapters;

import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.ui.fragments.BasicHeroDataFragment;
import kpk.dev.d3app.ui.fragments.HeroArmoryFragment;
import kpk.dev.d3app.ui.fragments.HeroSkillsFragment;
import kpk.dev.d3app.ui.fragments.HeroStatsFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HeroDataAdapter extends FragmentStatePagerAdapter {
	private HeroModelDecorator mHeroModel;
	
	
	public HeroDataAdapter(FragmentManager fm, HeroModelDecorator heroModel) {
		super(fm);
		mHeroModel = heroModel;
	}
	
	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		if(position == 0){
			fragment = new BasicHeroDataFragment();
			((BasicHeroDataFragment)fragment).setData(mHeroModel);
		}
		if(position == 1){
			fragment = new HeroArmoryFragment();
			((HeroArmoryFragment)fragment).setData(mHeroModel);
		}
		if(position == 2){
			fragment = new HeroSkillsFragment();
			((HeroSkillsFragment)fragment).setData(mHeroModel);
		}
		//Stats fragment
		if(position == 3){
			fragment = new HeroStatsFragment();
			((HeroStatsFragment)fragment).setData(mHeroModel.getStats());
		}
		return fragment;
	}
	
	@Override
	public int getCount() {
		return titles.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
	
	private String[] titles = {
		"Hero summary",
		"Armory",
		"Skills",
		"Statistics"
	};
}