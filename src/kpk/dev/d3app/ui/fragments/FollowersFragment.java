package kpk.dev.d3app.ui.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import kpk.dev.d3app.R;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.models.accountmodels.D3Follower;
import kpk.dev.d3app.models.accountmodels.D3FollowerSkill;
import kpk.dev.d3app.models.accountmodels.D3Item;
import kpk.dev.d3app.tasks.FileDownloader;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
import kpk.dev.d3app.util.Utils;
import kpk.dev.d3app.widgets.FollowerView;

public class FollowersFragment extends AbstractFragment<Map<String, D3Follower>> {
	private Map<String, D3Follower> mFollowers;
	private boolean mHasData;
	private List<String> mapKeys= new ArrayList<String>();
	private List<Integer> viewIds = new ArrayList<Integer>();
	private View mRootView;
	@Override
	public void setData(Map<String, D3Follower> data) {
		if(data != null){
			mHasData = true;
			mFollowers = data;
			for(int i = 0; i < D3Constants.FOLLOWERS_KEYS_ARRAY.length; i++) {
				mapKeys.add(D3Constants.FOLLOWERS_KEYS_ARRAY[i]);
				viewIds.add(D3Constants.FOLLOWER_VIEWS_IDS[i]);
			}
		}else{
			mHasData = false;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.followers_fragment_layout, container, false);
		checkDataAndOrganizeViews();
		return mRootView;
	}
	
	private void checkDataAndOrganizeViews() {
		if(mHasData) {
			for(int i = 0; i < D3Constants.FOLLOWERS_KEYS_ARRAY.length; i++) {
				if(mFollowers.get(D3Constants.FOLLOWERS_KEYS_ARRAY[i]) == null){
					mRootView.findViewById(D3Constants.FOLLOWER_VIEWS_IDS[i]).setVisibility(View.GONE);
					viewIds.set(i, -1);
					mapKeys.set(i, "");
				}
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(mHasData){
			for(int i = 0; i < viewIds.size(); i++) {
				setupImageView(viewIds.get(i), i);
			}
		}
	}

	private void setupImageView(int viewId, int index) {
		if(viewId >= 0){
			
			FollowerView followerView = (FollowerView)mRootView.findViewById(viewId);
			setupSkillsImages(followerView, mapKeys.get(index));
			Map<String, D3Item> followerItems = mFollowers.get(mapKeys.get(index)).getItems();
			
			for(int i = 0; i < D3Constants.FOLLOWERS_ITEMS_HOLDERS_IDS.length; i++) {
				D3Item item = followerItems.get(D3Constants.FOLLOWERS_ITEMS_TYPES[i]);
				if(item == null) continue;
				final ImageView view = (ImageView)followerView.findViewById(D3Constants.FOLLOWERS_ITEMS_HOLDERS_IDS[i]);
				view.setTag(item);
				String itemIcon = D3Constants.LARGE_ITEM_ICON_URL + item.getIcon() + ".png";
				final File itemIconFile = new File(D3Constants.getExternalImageDirectory() + Utils.getFileNameFromURL(itemIcon));
				Bitmap bmp = getMemoryCache().getBitmapFromMemoryCache(itemIconFile.getName());
				view.setBackgroundResource(Utils.getDrawableForItem(item.getDisplayColor()));
				if(checkIfBitmapExistsOnDiskAndCache(itemIconFile, bmp == null)){
					bmp = null;
				}
				if(bmp != null){
					view.setImageBitmap(bmp);
				}else{
					new FileDownloader(getActivity().getApplicationContext()).downloadFile(getScreenDensity(), itemIcon, itemIconFile, new FileDownloadListener() {
						public void fileDownloaded() {
							view.setImageBitmap(getMemoryCache().getBitmapFromMemoryCache(itemIconFile.getName()));
						};
					});
				}
			}
		}
	}
	
	private void setupSkillsImages(FollowerView followerView, String followerKey) {
		List<D3FollowerSkill> skills = mFollowers.get(followerKey).getD3FollowerSkills();
		fillFollowerSkills(skills);
		for(int i = 0; i < D3Constants.FOLLOWERS_SKILLS_HOLDERS_IDS.length; i++) {
			TextView txtV = (TextView)followerView.findViewById(D3Constants.FOLLOWERS_SKILLS_HOLDERS_IDS[i]);
			if(skills.get(i) == null){
				txtV.setText(D3Constants.FOLLOWERS_SKILLS_ENABLED_LEVELS[i]);
			}else{
				setImage(D3Constants.SKILL_ICON_URL + skills.get(i).getIcon() + ".png", txtV);
			}
		}
	}
	
	private void fillFollowerSkills(List<D3FollowerSkill> skills) {
		while(skills.size() < 4) {
			skills.add(null);
		}
	}

	private int getScreenDensity() {
		DisplayMetrics metrics = new DisplayMetrics();    
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);    
		return metrics.densityDpi;    
	}
}
