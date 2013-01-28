package kpk.dev.d3app.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kpk.dev.d3app.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;

public class Utils {
	public static Typeface getFont(Context aContext) {
		Typeface typeFace = Typeface.createFromAsset(aContext.getAssets(), "fonts/d3_bold.ttf");
		
		return typeFace;
	}
	
	public static void setFont(ViewGroup aViewGroup){
		if(aViewGroup != null){
			for(int i = 0; i < aViewGroup.getChildCount(); i++){
				View v = aViewGroup.getChildAt(i);
				if(ViewGroup.class.isInstance(v)){
					setFont((ViewGroup)v);
				}else{
					try{
						Method setTypefaceMethod = v.getClass().getMethod("setTypeface", Typeface.class);
						setTypefaceMethod.invoke(v, getFont(aViewGroup.getContext()));
					}catch(Exception e){
						//e.printStackTrace();
					}
				}
			}
		}
	}
	
	public static void setFont(View view){
		if(view != null){
			try{
				Method setTypefaceMethod = view.getClass().getMethod("setTypeface", Typeface.class);
				setTypefaceMethod.invoke(view, getFont(view.getContext()));
			}catch(Exception e){
				
			}
		}
	}
	
	public static int getDrawableForItem(String color) {
		if (color.equalsIgnoreCase("white")) {
			return R.drawable.item_white;
		} else if (color.equalsIgnoreCase("yellow")) {
			return R.drawable.item_yellow;
		} else if (color.equalsIgnoreCase("orange")) {
			return R.drawable.item_orange;
		} else if (color.equalsIgnoreCase("blue")) {
			return R.drawable.item_blue;
		} else if (color.equalsIgnoreCase("green")) {
			return R.drawable.item_green;
		} else if (color.equalsIgnoreCase("grey")) {
			return R.drawable.item_gray;
		}
		return 0;
	}
	
	public static List<String> getListFromString(String str) {
		final List<String> resultList = new ArrayList<String>();
		final String[] arr = str.split(",");
		for(String s : arr) {
			if(s != null && s.length() > 0) {
				resultList.add(s);
			}
		}
		return resultList;
	}
	
	public static synchronized String removeHiphen(String classString) {
		StringBuilder heroClass = new StringBuilder();
		
		int hiphenIndex = -1;
		for(int i = 0; i < classString.length(); i++) {
			if((int)classString.charAt(i) == 45){
				hiphenIndex = i;
			}
		}
		if(hiphenIndex > -1){
			heroClass.append(classString.substring(0, hiphenIndex));
			heroClass.append(classString.substring(hiphenIndex + 1, classString.length()));
		}else{
			heroClass.append(classString);
		}
		return heroClass.toString();
	}
	
	public static String getLastUpdatedTime(long before) {
		Calendar lastUpdateDate = Calendar.getInstance();
		lastUpdateDate.setTime(new Date(before * 1000));
		Calendar today = Calendar.getInstance();
		long diff = today.getTimeInMillis() - lastUpdateDate.getTimeInMillis();
		long diffHours = diff / (60 * 60 * 1000);
		long diffDays = diff / (24 * 60 * 60 * 1000);
		StringBuilder timeStringBuilder = new StringBuilder();
		if(diffDays > 0){
			timeStringBuilder.append(diffDays + " days ");
		}
		if(diffHours > 0){
			timeStringBuilder.append((diffHours - (diffDays * 24)) + " hours ");
		}
		
		timeStringBuilder.append("ago");
		if(timeStringBuilder.length() == 3){
			timeStringBuilder.insert(0, "0 hours ");
		}
		return timeStringBuilder.toString();
	} 
	
	public static Bitmap decodeImageFromStorage(String pathName, int imageWidth, int imageHeight, int density) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateSampleSize(options, imageWidth, imageHeight);
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(pathName, options);
		bmp.setDensity(density);
		return bmp;
	}

	private static int calculateSampleSize(Options options, int imageWidth,
			int imageHeight) {
		final int rawWidth = options.outWidth;
		final int rawHeight = options.outHeight;
		int inSampleSize = 1;
		
		if(rawWidth > imageWidth || rawHeight > imageHeight) {
			if(rawWidth > rawHeight) {
				inSampleSize = Math.round((float)rawHeight / (float)imageHeight);
			}else{
				inSampleSize = Math.round((float)rawWidth / (float)imageWidth);
			}
		}
		return inSampleSize;
	}

	public static String getFileNameFromURL(String url) {
		return url.substring(url.lastIndexOf('/') + 1, url.length());
	}
	
	public static boolean getIsStorageAvailable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    return false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    return false;
		}
	}
	
	public static boolean isConnectedToInternet(Context context) {
		boolean isConnected = false;
		
		final ConnectivityManager connMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connMan.getActiveNetworkInfo();
		
		if(activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()){
			isConnected = true;
		}
		return isConnected;
	}
	
	public static SharedPreferences getAppSharedPreferences(Context context) {
		return context.getSharedPreferences(D3Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
	}
}
