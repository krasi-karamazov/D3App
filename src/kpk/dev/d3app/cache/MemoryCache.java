package kpk.dev.d3app.cache;

import java.nio.ByteBuffer;

import kpk.dev.d3app.util.KPKLog;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCache {
	private LruCache< String, Bitmap> mMemoryCache;
	private Context mAppContext;
	private static MemoryCache sCache;
	public static MemoryCache getInstance(Context context) {
		if(sCache == null) {
			sCache = new MemoryCache(context);
		}
		return sCache;
	}
	
	private MemoryCache(Context context) {
		mAppContext = context;
		final int memClass = ((ActivityManager) mAppContext.getSystemService(
	            Context.ACTIVITY_SERVICE)).getMemoryClass();
		final int cacheSize = 1024 * 1024 * memClass / 8;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return getBitmapBytes(value);
			}
		};
	}
	
	private int getBitmapBytes(Bitmap value) {
		int bytes = value.getWidth() * value.getHeight() * 4;
		KPKLog.d("bytes " + String.valueOf(bytes));
		ByteBuffer buffer = ByteBuffer.allocate(bytes);
		value.copyPixelsToBuffer(buffer);
		byte[] array = buffer.array();
		if (array != null) {
			return array.length * 4;
		}
		return 0;
	}
	
	public void addBitmapToCache(String key, Bitmap value) {
		if(getBitmapFromMemoryCache(key)== null && value != null) {
			mMemoryCache.put(key, value);
		}
	}
	
	public void removeBitmapFromCache(String key){
		if(getBitmapFromMemoryCache(key) != null){
			mMemoryCache.remove(key);
		}
	}
	
	public Bitmap getBitmapFromMemoryCache(String key) {
		if(key == null){
			return null;
		}
		return mMemoryCache.get(key);
	}
}
