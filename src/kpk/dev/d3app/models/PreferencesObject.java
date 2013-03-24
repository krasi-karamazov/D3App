package kpk.dev.d3app.models;

import java.util.ArrayList;
import java.util.List;

public class PreferencesObject {
	private String mTitle;
	private String mDescription;
	private ArrayList<String> mOptionsItems;
	public PreferencesObject(String title, String description){
		mTitle = title;
		mDescription = description;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getmDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public ArrayList<String> getOptionsItems() {
		return mOptionsItems;
	}

	public void setOptionsItems(ArrayList<String> optionsItems) {
		this.mOptionsItems = optionsItems;
	}
}
