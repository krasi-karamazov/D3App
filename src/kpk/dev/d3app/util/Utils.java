package kpk.dev.d3app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import kpk.dev.d3app.R;
import kpk.dev.d3app.models.accountmodels.D3Item;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

public class Utils {
	private static DecimalFormat twoDForm = new DecimalFormat("#.##");
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
	
	public static List<D3Item> getListFromMap(Map<String, D3Item> items) {
		final Set<Entry<String, D3Item>> itemsSet = items.entrySet();
		Iterator<Entry<String, D3Item>> itemsIterator = itemsSet.iterator();
		final ArrayList<D3Item> itemsList = new ArrayList<D3Item>();
		while(itemsIterator.hasNext()){
			Entry<String, D3Item> entry = itemsIterator.next();
			D3Item d3Item = entry.getValue();
			d3Item.setType(entry.getKey());
			itemsList.add(d3Item);
		}
		return itemsList;
	}
	
	public static void writeStatsStrings(Entry<String, Map<String, String>> entry, TextView titleView, TextView statsView){
		titleView.setText(entry.getKey());
		Map<String, String> statsMap = entry.getValue();
		Set<Entry<String, String>> statsSet = statsMap.entrySet();
		StringBuilder statsBuilder = new StringBuilder();
		for(Entry<String, String> statsEntry : statsSet) {
			statsBuilder.append("<b>" + statsEntry.getKey() + ":" + "</b>" + " " + statsEntry.getValue() + "<br>");
		}
		statsView.setText(Html.fromHtml(statsBuilder.toString()));
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

    public static int getPixelsForDisplayScale(Context context, int dps){
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
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
			timeStringBuilder.delete(0, timeStringBuilder.length());
			timeStringBuilder.insert(0, "Just now");
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
	
	public static Map<String, Map<String, String>> getOrganizedStatMaps(Map<String, Number> stats){
		Map<String, Map<String, String>> organizedStats = new HashMap<String, Map<String,String>>();
		organizedStats.put(D3Constants.MAIN_ATTRIBUTES_KEY, setupMainAttributes(stats));
		organizedStats.put(D3Constants.OFFENSIVE_KEY, setupOffensive(stats));
		organizedStats.put(D3Constants.LIFE_KEY, setupLife(stats));
		organizedStats.put(D3Constants.DEFENSIVE_KEY, setupDefensive(stats));
		organizedStats.put(D3Constants.ADVENTURE_KEY, setupAdventure(stats));
		return organizedStats;
	}
	
	private static Map<String, String> setupAdventure(Map<String, Number> stats) {
		Map<String, String> mAdventure = new HashMap<String, String>();
		mAdventure.put("Gold find", "+" + Double.valueOf(twoDForm.format(stats.get("goldFind").doubleValue() * 100)).toString() + "%");
		mAdventure.put("Magic find", "+" + Double.valueOf(twoDForm.format(stats.get("magicFind").doubleValue() * 100)).toString() + "%");
		return mAdventure;
	}
	
	private static Map<String, String> setupDefensive(Map<String, Number> stats) {
		Map<String, String> mDefensive = new HashMap<String, String>();
		mDefensive.put("Block ammount min", Double.valueOf(stats.get("blockAmountMin").floatValue()).toString());
		mDefensive.put("Block ammount max", Double.valueOf(stats.get("blockAmountMax").floatValue()).toString());
		mDefensive.put("Block chance", Double.valueOf(twoDForm.format(stats.get("blockChance").doubleValue() * 100)).toString() + "%");
		mDefensive.put("Thorns", Double.valueOf(stats.get("thorns").floatValue()).toString());
		mDefensive.put("Physical resistance", Double.valueOf(stats.get("physicalResist").floatValue()).toString());
		mDefensive.put("Fire resistance", Double.valueOf(stats.get("fireResist").floatValue()).toString());
		mDefensive.put("Cold resistance", Double.valueOf(stats.get("coldResist").floatValue()).toString());
		mDefensive.put("Lightning resistance", Double.valueOf(stats.get("lightningResist").floatValue()).toString());
		mDefensive.put("Poison resistance", Double.valueOf(stats.get("poisonResist").floatValue()).toString());
		mDefensive.put("Arcane resistance", Double.valueOf(stats.get("arcaneResist").floatValue()).toString());
		mDefensive.put("Damage reduction", Double.valueOf(twoDForm.format(stats.get("damageReduction").doubleValue() * 100)).toString() + "%");
		
		return mDefensive;
	}
	
	private static Map<String, String> setupLife(Map<String, Number> stats) {
		Map<String, String> mLife = new HashMap<String, String>();
		mLife.put("Life", Double.valueOf(stats.get("life").floatValue()).toString());
		mLife.put("Life steal", Double.valueOf(stats.get("lifeSteal").floatValue()).toString() + "%");
		mLife.put("Life on hit", Double.valueOf(stats.get("lifeOnHit").floatValue()).toString());
		mLife.put("Life per kill", Double.valueOf(stats.get("lifePerKill").floatValue()).toString());
		return mLife;
	}

	private static Map<String, String> setupOffensive(Map<String, Number> stats) {
		Map<String, String> mOffensive = new HashMap<String, String>();
		mOffensive.put("Attack speed", Double.valueOf(twoDForm.format(stats.get("attackSpeed").doubleValue())).toString());
		mOffensive.put("Damage increase", Double.valueOf(twoDForm.format(stats.get("damageIncrease").doubleValue() * 100)).toString() + "%");
		mOffensive.put("Critical hit chance", Double.valueOf(twoDForm.format(stats.get("critChance").doubleValue() * 100)).toString() + "%");
		mOffensive.put("Critical hit damage", "+" + (Double.valueOf(twoDForm.format(stats.get("critDamage").doubleValue() * 100)) - 100) + "%");
		return mOffensive;
	}
	
	private static Map<String, String> setupMainAttributes(Map<String, Number> stats) {
		Map<String, String> mMainAttributes = new HashMap<String, String>();
		mMainAttributes.put("Strength", Integer.valueOf(stats.get("strength").intValue()).toString());
		mMainAttributes.put("Dexterity", Integer.valueOf(stats.get("dexterity").intValue()).toString());
		mMainAttributes.put("Intelligence", Integer.valueOf(stats.get("intelligence").intValue()).toString());
		mMainAttributes.put("Vitality", Integer.valueOf(stats.get("vitality").intValue()).toString());
		mMainAttributes.put("Armor", Integer.valueOf(stats.get("armor").intValue()).toString());
		mMainAttributes.put("Damage", Integer.valueOf(stats.get("damage").intValue()).toString());
		return mMainAttributes;
	}
}
