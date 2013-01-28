package kpk.dev.d3app.adapters;

import java.io.File;
import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.cache.MemoryCache;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.models.accountmodels.IProfileModel;
import kpk.dev.d3app.models.accountmodels.ProfileModel;
import kpk.dev.d3app.tasks.FileDownloader;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilesAdapter extends ArrayAdapter<IProfileModel> {
	private MemoryCache mMemCache;
	public ProfilesAdapter(Context context, int layoutID, List<IProfileModel> profiles) {
		super(context, layoutID, R.layout.profile_list_item, profiles);
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
		
		ProfileModel profile = (ProfileModel)getItem(position);
		if(profile != null) {
			viewHolder.regionInitials.setText("Region: " + profile.getServer());
			viewHolder.battleTag.setText(profile.getBattleTag());
			
			viewHolder.lastUpdated.setText("Last updated: " + Utils.getLastUpdatedTime(profile.getLastUpdated()));
			if(profile.getLastPlayedHeroPortrait() == null){
				profile.setLastPlayedHeroPortrait(ProfileModel.getHeroPortrait(profile.getLastHeroPlayed(), profile));
			}
			File portraitImage = new File(D3Constants.getExternalImageDirectory() + Utils.getFileNameFromURL(profile.getLastPlayedHeroPortrait()));
			Bitmap bmp = mMemCache.getBitmapFromMemoryCache(portraitImage.getName());
			if(checkIfBitmapExistsOnDiskAndCache(portraitImage, bmp == null)){
				bmp = null;
			}
			if(bmp != null){
				viewHolder.heroPortrait.setImageBitmap(bmp);
			}else{
				new FileDownloader(getContext().getApplicationContext()).downloadFile(getScreenDensity(), profile.getLastPlayedHeroPortrait(), portraitImage, new FileDownloadListener() {
					
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
