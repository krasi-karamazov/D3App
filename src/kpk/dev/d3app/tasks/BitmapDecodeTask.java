package kpk.dev.d3app.tasks;

import java.io.File;

import kpk.dev.d3app.cache.MemoryCache;
import kpk.dev.d3app.listeners.FileDownloadListener;
import kpk.dev.d3app.util.Utils;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class BitmapDecodeTask extends AsyncTask<File, Integer, Bitmap> {
	private MemoryCache mMemoryCache;
	private int mDensity;
	private String mBitmapKey;
	private FileDownloadListener mDownloadListener;
	public BitmapDecodeTask(int density, MemoryCache memoryCache, final FileDownloadListener fileDownloadListener){
		mDensity = density;
		mMemoryCache = memoryCache;
		mDownloadListener = fileDownloadListener;
	}
	
	@Override
	protected Bitmap doInBackground(File... params) {
		Bitmap bmp = Utils.decodeImageFromStorage(params[0].getPath(), 500, 500, mDensity);
		mBitmapKey = params[0].getName();
		return bmp;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		mMemoryCache.addBitmapToCache(mBitmapKey, result);
		mDownloadListener.fileDownloaded();
	}
}
