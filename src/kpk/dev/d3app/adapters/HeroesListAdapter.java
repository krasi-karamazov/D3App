package kpk.dev.d3app.adapters;

import java.io.File;
import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.cache.MemoryCache;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.models.accountmodels.HeroModel;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.tasks.FileDownloader;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HeroesListAdapter extends ArrayAdapter<HeroModel> {
	private MemoryCache mMemCache;
	public HeroesListAdapter(Context context, int layoutID, List<HeroModel> heroes) {
		super(context, layoutID, R.layout.profile_list_item, heroes);
		mMemCache = MemoryCache.getInstance(context.getApplicationContext());
	}
	
	static class ViewHolder{
		ImageView heroPortrait;
		TextView regionInitials;
		TextView battleTag;
		TextView lastUpdated;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.profile_list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.heroPortrait = (ImageView)row.findViewById(R.id.portrait_last_hero_played);
			viewHolder.regionInitials = (TextView)row.findViewById(R.id.server_region_label);
			viewHolder.battleTag = (TextView)row.findViewById(R.id.battle_tag_label);
			viewHolder.lastUpdated = (TextView)row.findViewById(R.id.last_updated_label);
			row.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)row.getTag();
		}
		
		HeroModel hero = getItem(position);
		
		if(hero != null) {
			StringBuilder heroClass = new StringBuilder();
			int hiphenIndex = -1;
			for(int i = 0; i < hero.getclass().length(); i++) {
				if((int)hero.getclass().charAt(i) == 45){
					hiphenIndex = i;
				}
			}
			if(hiphenIndex > -1){
				heroClass.append(hero.getclass().substring(0, hiphenIndex) + " ");
				heroClass.append(hero.getclass().substring(hiphenIndex + 1, hero.getclass().length()));
			}else{
				heroClass.append(hero.getclass());
			}
			viewHolder.regionInitials.setText(Html.fromHtml(getHeroClass(hero) + " " + hero.getLevel() 
					+ "<font color='#A791C2'>(" + hero.getParagonLevel() + ")</font>") + ((hero.getHandrdcore())?" Hardcore":""));
			viewHolder.battleTag.setText(hero.getName());
			
			viewHolder.lastUpdated.setText("Last updated: " + Utils.getLastUpdatedTime(hero.getLastUpdated()));
			
			if(hero.getPortrait() == null){
				
				hero.setPortrait(ProfileModel.HERO_PORTRAIT_URL + heroClass.toString() + "_" 
						+ hero.getGender().name() + ".png");
			}
			File portraitImage = new File(D3Constants.getExternalImageDirectory() + Utils.getFileNameFromURL(hero.getPortrait()));
			Bitmap bmp = mMemCache.getBitmapFromMemoryCache(portraitImage.getName());
			if(checkIfBitmapExistsOnDiskAndCache(portraitImage, bmp == null)){
				bmp = null;
			}
			if(bmp != null){
				viewHolder.heroPortrait.setImageBitmap(bmp);
			}else{
				new FileDownloader(getContext().getApplicationContext()).downloadFile(getScreenDensity(), hero.getPortrait(), portraitImage, new FileDownloadListener() {
					
					@Override
					public void fileDownloaded() {
						
						((Activity)getContext()).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								notifyDataSetChanged();
							}
						});
					}
				});
			}
		}
		return row;
	}
	
	private String getHeroClass(HeroModel hero){
		String heroClass = "";
		if(hero.getclass().equalsIgnoreCase("monk")){
			heroClass = getContext().getString(R.string.monk_label);
		}else if(hero.getclass().equalsIgnoreCase("barbarian")){
			heroClass = getContext().getString(R.string.barbarian_label);
		}else if(hero.getclass().equalsIgnoreCase("wizard")){
			heroClass = getContext().getString(R.string.wizard_label);
		}else if(hero.getclass().equalsIgnoreCase("demon-hunter")){
			heroClass = getContext().getString(R.string.dh_label);
		}else if(hero.getclass().equalsIgnoreCase("witch-doctor")){
			heroClass = getContext().getString(R.string.wd_label);
		}
		return heroClass;
	}
	
	private final boolean checkIfBitmapExistsOnDiskAndCache(File file, boolean bitmapIsNull){
		if(!file.exists() && !bitmapIsNull){
			mMemCache.removeBitmapFromCache(file.getName());
			return true;
		}
		return false;
	}
	
	private int getScreenDensity() {
		DisplayMetrics metrics = new DisplayMetrics();    
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);    
		return metrics.densityDpi;    
	}
}
