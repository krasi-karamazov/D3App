package kpk.dev.d3app.ui.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kpk.dev.d3app.R;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.models.accountmodels.D3Item;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.tasks.FileDownloader;
import kpk.dev.d3app.ui.activities.TooltipActivity;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HeroArmoryFragment extends AbstractFragment<HeroModelDecorator> {
	
	private View mRootView;
	private HeroModelDecorator mModel;
	private final Map<String, Integer> mClassesMap = new LinkedHashMap<String, Integer>();
	@Override
	public void setData(HeroModelDecorator data) {
		mModel = data;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.armory_fragment_layout, container, false);
		mClassesMap.put("barbarian_male", R.drawable.armory_barbarian_male);
		mClassesMap.put("barbarian_female", R.drawable.armory_barbarian_female);
		mClassesMap.put("monk_male", R.drawable.armory_monk_male);
		mClassesMap.put("monk_female", R.drawable.armory_monk_female);
		mClassesMap.put("demon-hunter_male", R.drawable.armory_dh_male);
		mClassesMap.put("demon-hunter_female", R.drawable.armory_dh_female);
		mClassesMap.put("witch-doctor_male", R.drawable.armory_wd_male);
		mClassesMap.put("witch-doctor_female", R.drawable.armory_wd_female);
		mClassesMap.put("wizard_male", R.drawable.armory_wizard_male);
		mClassesMap.put("wizard_female", R.drawable.armory_wizard_female);
		return mRootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setBackgroundImage();
		setItemsImageViews();
	}
	
	private void setItemsImageViews() {
		final List<D3Item> items = Utils.getListFromMap(mModel.getItems());
		if(items == null || items.size() <= 0) {
			for(int j = 0; j < D3Constants.ITEMS_HOLDERS_IDS.length; j++) {
				for(int k = 0; k < D3Constants.ITEMS_SOCKETS[j].length; k++){
					mRootView.findViewById(D3Constants.ITEMS_SOCKETS[j][k]).setVisibility(View.GONE);
				}
			}
			return;
		}
		for(int i = 0; i < items.size(); i++) {
			for(int j = 0; j < D3Constants.ITEMS_HOLDERS_IDS.length; j++) {
				for(int k = 0; k < D3Constants.ITEMS_GEMS[j].length; k++){
					mRootView.findViewById(D3Constants.ITEMS_SOCKETS[j][k]).setVisibility(View.INVISIBLE);
				}
				if(items.get(i).getType().equalsIgnoreCase(D3Constants.ITEMS_TYPES[j])){
					items.get(i).setImageViewID(D3Constants.ITEMS_HOLDERS_IDS[j]);
					items.get(i).setGemsArray(D3Constants.ITEMS_GEMS[j]);
					items.get(i).setSocketsArray(D3Constants.ITEMS_SOCKETS[j]);
				}
			}
		}
		loadImages(items);
	}
	
	private void loadImages(List<D3Item> items) {
		for(D3Item item : items){
			final ImageView view = (ImageView)mRootView.findViewById(item.getImageViewID());
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
			if(item.getGemsString() != null) {
				List<String> gemsURL = Utils.getListFromString(item.getGemsString());
				for(int i = 0; i < gemsURL.size(); i++) {
					final ImageView imgView = (ImageView)mRootView.findViewById(item.getGemsArray()[i]);
					View socketView = mRootView.findViewById(item.getSocketsArray()[i]); 
					socketView.setVisibility(View.VISIBLE);
					final File gemIconFile = new File(D3Constants.getExternalImageDirectory() + Utils.getFileNameFromURL(gemsURL.get(i)));
					Bitmap gemBmp = getMemoryCache().getBitmapFromMemoryCache(gemIconFile.getName());
					if(checkIfBitmapExistsOnDiskAndCache(gemIconFile, gemBmp == null)){
						gemBmp = null;
					}
					if(gemBmp!= null){
						imgView.setImageBitmap(gemBmp);
					}else{
						new FileDownloader(getActivity().getApplicationContext()).downloadFile(getScreenDensity(), gemsURL.get(i), gemIconFile, new FileDownloadListener() {
							public void fileDownloaded() {
								imgView.setImageBitmap(getMemoryCache().getBitmapFromMemoryCache(gemIconFile.getName()));
							};
						});
					}
				}
			}
			view.setOnClickListener(showItemDetailsListener);
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
	
	private int getScreenDensity() {
		DisplayMetrics metrics = new DisplayMetrics();    
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);    
		return metrics.densityDpi;    
	}

	private void setBackgroundImage() {
		final ImageView backgroundView = (ImageView)mRootView.findViewById(R.id.equipment_image);
		String className = mModel.getHeroModel().getclass() + "_" + mModel.getHeroModel().getGender().name();
		backgroundView.setImageDrawable(getResources().getDrawable(mClassesMap.get(className)));
	}
}
