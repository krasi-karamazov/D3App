package kpk.dev.d3app.ui.fragments;

import java.io.File;

import kpk.dev.d3app.cache.MemoryCache;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.tasks.FileDownloader;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public abstract class AbstractFragment<T> extends Fragment {
	private MemoryCache mMemoryCache;
	public abstract void setData(T data);
	
	protected final void setImage(final String portraitUrl, final ImageView imgView) {
		final File portraitImage = new File(D3Constants.getExternalImageDirectory() + Utils.getFileNameFromURL(portraitUrl));
		Bitmap bmp = mMemoryCache.getBitmapFromMemoryCache(portraitImage.getName());
		if(checkIfBitmapExistsOnDiskAndCache(portraitImage, bmp == null)){
			bmp = null;
		}
		if(bmp != null) {
			imgView.setImageBitmap(bmp);
		}else{
			new FileDownloader(getActivity().getApplicationContext()).downloadFile(getScreenDensity(), portraitUrl, portraitImage,new FileDownloadListener() {
				public void fileDownloaded() {
					imgView.setImageBitmap(mMemoryCache.getBitmapFromMemoryCache(portraitImage.getName()));
				};
			});
		}
	}
	
	protected final boolean checkIfBitmapExistsOnDiskAndCache(File file, boolean bitmapIsNull){
		if(!file.exists() && !bitmapIsNull){
			mMemoryCache.removeBitmapFromCache(file.getName());
			return true;
		}
		return false;
	}
	
	protected final MemoryCache getMemoryCache() {
		return mMemoryCache;
	}
	
	private int getScreenDensity() {
		DisplayMetrics metrics = new DisplayMetrics();    
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);    
		return metrics.densityDpi;    
	}
	
	@Override
	public final void onAttach(Activity activity) {
		super.onAttach(activity);
		setRetainInstance(true);
		mMemoryCache = MemoryCache.getInstance(getActivity().getApplicationContext());
	}
}
