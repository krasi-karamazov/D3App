package kpk.dev.d3app.adapters;

import java.io.File;
import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.cache.MemoryCache;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.models.accountmodels.D3Skill;
import kpk.dev.d3app.tasks.FileDownloader;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
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

public class SkillsAdapter extends ArrayAdapter<D3Skill> {
	private MemoryCache mMemCache;
	private String mServer;
	public SkillsAdapter(Context context, int defaultResId, int resId, List<D3Skill> skills, String server){
		super(context, defaultResId, resId, skills);
		mServer = server;
		mMemCache = MemoryCache.getInstance(context.getApplicationContext());
	}
	
	static class ViewHolder{
		ImageView skillImage;
		TextView skillCategory;
		TextView skillEnabledLevel;
		ImageView mouseIcon;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.skill_item_layout, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.skillImage = (ImageView)row.findViewById(R.id.skill_image);
			viewHolder.skillCategory = (TextView)row.findViewById(R.id.skill_category);
			viewHolder.skillEnabledLevel = (TextView)row.findViewById(R.id.skill_enabled_level);
			viewHolder.mouseIcon = (ImageView)row.findViewById(R.id.mouse_icon);
			row.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)row.getTag();
		}
		D3Skill skill = getItem(position);
		if(position == 0){
			viewHolder.mouseIcon.setImageResource(R.drawable.left_mouse_icon);
		}else if(position == 1) {
			viewHolder.mouseIcon.setImageResource(R.drawable.right_mouse_icon);
		}else{
			viewHolder.mouseIcon.setVisibility(View.INVISIBLE);
		}
		if(skill != null) {
			viewHolder.skillEnabledLevel.setVisibility(View.GONE);
			viewHolder.skillCategory.setText(skill.getName());
			String icon = D3Constants.SKILL_ICON_URL + skill.getIcon() + ".png";
			File skillImage = new File(D3Constants.getExternalImageDirectory() + Utils.getFileNameFromURL(icon));
			Bitmap bmp = mMemCache.getBitmapFromMemoryCache(skillImage.getName());
			if(checkIfBitmapExistsOnDiskAndCache(skillImage, bmp == null)){
				bmp = null;
			}
			if(bmp != null){
				viewHolder.skillImage.setImageBitmap(bmp);
			}else{
				new FileDownloader(getContext().getApplicationContext()).downloadFile(getScreenDensity(), icon, skillImage, new FileDownloadListener() {
					
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
			
		}else{
			viewHolder.skillImage.setImageBitmap(null);
			viewHolder.skillEnabledLevel.setVisibility(View.VISIBLE);
			viewHolder.skillCategory.setText("");
			Utils.setFont(viewHolder.skillEnabledLevel);
			viewHolder.skillEnabledLevel.setText(Integer.valueOf(skillEnabledLevels[position]).toString());
			KPKLog.d(Integer.valueOf(skillEnabledLevels[position]).toString());
			viewHolder.skillImage.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.portrait_frame));
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
	
	private static int[] skillEnabledLevels = {1, 2, 4, 9, 14, 19};
}
