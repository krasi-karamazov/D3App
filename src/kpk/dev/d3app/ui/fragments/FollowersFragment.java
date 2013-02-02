package kpk.dev.d3app.ui.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kpk.dev.d3app.R;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.models.accountmodels.D3Follower;
import kpk.dev.d3app.models.accountmodels.D3FollowerSkill;
import kpk.dev.d3app.models.accountmodels.D3Item;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.tasks.FileDownloader;
import kpk.dev.d3app.ui.activities.TooltipActivity;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;
import kpk.dev.d3app.widgets.FollowerView;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FollowersFragment extends AbstractFragment<HeroModelDecorator> {
	private Map<String, D3Follower> mFollowers;
	private boolean mHasData;
	private List<String> mapKeys= new ArrayList<String>();
	private List<Integer> viewIds = new ArrayList<Integer>();
	private View mRootView;
	private HeroModelDecorator mModel;
	@Override
	public void setData(HeroModelDecorator data) {
		if(data != null){
			mHasData = true;
			mModel = data;
			mFollowers = mModel.getFollowersModels();
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
				view.setOnClickListener(showItemDetailsListener);
			}
		}
	}
	
	private View.OnClickListener showItemDetailsListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			D3Item item = (D3Item)v.getTag();
			Intent intent = new Intent(getActivity(), TooltipActivity.class);
			intent.putExtra(TooltipActivity.TOOLTIP_URL_KEY, getToolTipUrl(item));
			startActivity(intent);
		}
	};
	
	private String getToolTipUrl(D3Item item) {
		return "http://" + mModel.getHeroModel().getServer() + D3Constants.ITEM_PARAM_URL + item.getToolTipParams();
	}
	
	private void setupSkillsImages(FollowerView followerView, String followerKey) {
		List<D3FollowerSkill> skills = mFollowers.get(followerKey).getD3FollowerSkills();
		fillFollowerSkills(skills);
		for(int i = 0; i < D3Constants.FOLLOWERS_SKILLS_HOLDERS_IDS.length; i++) {
			TextView skillImage = (TextView)followerView.findViewById(D3Constants.FOLLOWERS_SKILLS_HOLDERS_IDS[i]);
			TextView skillLabel = (TextView)followerView.findViewById(D3Constants.FOLLOWERS_SKILLS_HOLDERS_LABELS_IDS[i]);
			if(skills.get(i) == null || skills.get(i).getIcon() == null){
				//skillImage.setText(String.valueOf(D3Constants.FOLLOWERS_SKILLS_ENABLED_LEVELS[i]));
			}else{
				setImage(D3Constants.SKILL_SMALL_ICON_URL + skills.get(i).getIcon() + ".png", skillImage);
				skillImage.setOnClickListener(new SkillClickListener(getSkillTipUrl(skills.get(i), mFollowers.get(followerKey).getSlug())));
				skillLabel.setText(String.valueOf(skills.get(i).getName()));
			}
		}
	}
	
	private String getSkillTipUrl(D3FollowerSkill skill, String followerSlug) {
		return "http://" + mModel.getHeroModel().getServer() + D3Constants.SKILL_TOOL_TIP_URL 
				+ followerSlug + "/" + skill.getSlug();
	}
	
	private class SkillClickListener implements View.OnClickListener {
		private String mToolTipUrl;
		SkillClickListener(String toolTipUrl){
			mToolTipUrl = toolTipUrl;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), TooltipActivity.class);
			intent.putExtra(TooltipActivity.TOOLTIP_URL_KEY, mToolTipUrl);
			startActivity(intent);
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
