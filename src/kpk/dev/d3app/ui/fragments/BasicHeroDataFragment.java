package kpk.dev.d3app.ui.fragments;

import java.text.NumberFormat;
import java.util.Locale;


import kpk.dev.d3app.R;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.widgets.ProgressionProgressBar;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BasicHeroDataFragment extends AbstractFragment<HeroModelDecorator> {
	private HeroModelDecorator mHeroModel;
	private View mRootView;
	private String mClass; 
	@Override
	public void setData(HeroModelDecorator data) {
		mHeroModel = data;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.hero_details_fragment_layout, container, false);
		return mRootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupImages();
		setupText();
		setupProgressBar();
	}

	private void setupText() {
		final TextView heroName = (TextView)mRootView.findViewById(R.id.hero_name);
		final TextView heroLevels = (TextView)mRootView.findViewById(R.id.hero_levels);
		final TextView heroLife = (TextView)mRootView.findViewById(R.id.hero_life_field);
		final TextView heroResouces = (TextView)mRootView.findViewById(R.id.hero_resources_field);
		final TextView eliteKills = (TextView)mRootView.findViewById(R.id.elite_kills_label);
		heroName.setText(mHeroModel.getHeroModel().getName());
		heroLevels.setText(Html.fromHtml(mClass + " " + mHeroModel.getHeroModel().getLevel() + "<font color='#A791C2'>(" + mHeroModel.getHeroModel().getParagonLevel() + ")</font>"));
		heroLife.setText(getLifeString(Double.valueOf(mHeroModel.getStats().get("life").toString())));
		heroResouces.setText(getResoursesString(Double.valueOf(mHeroModel.getStats().get("primaryResource").toString())
				, Double.valueOf(mHeroModel.getStats().get("secondaryResource").toString())));
		eliteKills.setText(eliteKills.getText() + "\n" + mHeroModel.getKills().get("elites").toString());
	}
	
	private String getResoursesString(Double primary, Double secondary) {
		NumberFormat f = NumberFormat.getInstance(new Locale("en-US"));
		f.setMaximumFractionDigits(0);
		StringBuilder resString = new StringBuilder(f.format(primary));
		String secondaryString = f.format(secondary);
		if(!secondaryString.equalsIgnoreCase("0")){
			resString.append("\n" + secondaryString);
		}
		return resString.toString();
	}
	
	private String getLifeString(Double lifeValue) {
		NumberFormat f = NumberFormat.getInstance(new Locale("en-US"));
		f.setMaximumFractionDigits(0);
		StringBuilder resString = new StringBuilder(f.format(lifeValue));
		if(lifeValue >= 10000) {
			String temp = resString.substring(0, 2).toString() + "." + resString.substring(3, 4).toString() + "k";
			resString.replace(0, resString.length(), temp);
		}
		return resString.toString();
	}

//	private String getStatsFromMap(){
//		final StringBuilder stats = new StringBuilder();
//		Set<Entry<String, Number>> entrySet = mHeroModel.getStats().entrySet();
//		Iterator<Entry<String, Number>> iterator = entrySet.iterator();
//		while(iterator.hasNext()) {
//			Entry<String, Number> entry = iterator.next();
//			if(entry.getKey().equalsIgnoreCase("strength") || entry.getKey().equalsIgnoreCase("dexterity") || entry.getKey().equalsIgnoreCase("inteligence") || 
//					entry.getKey().equalsIgnoreCase("vitality") || entry.getKey().equalsIgnoreCase("armor") || entry.getKey().equalsIgnoreCase("damage")){
//				String str = entry.getKey().substring(0, 1).toUpperCase(new Locale("en-US")) + entry.getKey().substring(1, entry.getKey().length());
//				NumberFormat f = NumberFormat.getInstance(new Locale("en-US"));
//				f.setMaximumFractionDigits(2);
//				stats.append("<b>" + str + "</b>" + ":\t" + f.format(entry.getValue()) + "<br><br>");
//			}
//		}
//		return stats.toString();
//	}

	private void setupImages() {
		final ImageView heroPortrait = (ImageView)mRootView.findViewById(R.id.hero_portrait);
		setImage(mHeroModel.getHeroModel().getPortrait(), heroPortrait);
		final ImageView lifeImage = (ImageView)mRootView.findViewById(R.id.hero_life_image);
		final ImageView resoursesImage = (ImageView)mRootView.findViewById(R.id.hero_resources_image);
		lifeImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.life_drawable));
		getResourseImage(resoursesImage);
	}

	private void getResourseImage(ImageView resoursesImage) {
		final TextView heroLifeLabel = (TextView)mRootView.findViewById(R.id.life_label);
		heroLifeLabel.setText("LIFE");
		final TextView heroResourseLabel = (TextView)mRootView.findViewById(R.id.resourse_label);
		Drawable dr = null;
		if(mHeroModel.getHeroModel().getclass().equalsIgnoreCase("monk")){
			mClass = getString(R.string.monk_label);
			heroResourseLabel.setText("SPIRIT");
			dr = getResources().getDrawable(R.drawable.spirit_drawable);
		}else if(mHeroModel.getHeroModel().getclass().equalsIgnoreCase("barbarian")){
			mClass = getString(R.string.barbarian_label);
			heroResourseLabel.setText("FURY");
			dr = getResources().getDrawable(R.drawable.fury_drawable);
		}else if(mHeroModel.getHeroModel().getclass().equalsIgnoreCase("wizard")){
			mClass = getString(R.string.wizard_label);
			heroResourseLabel.setText("ARCANE POWER");
			dr = getResources().getDrawable(R.drawable.energy_drawable);
		}else if(mHeroModel.getHeroModel().getclass().equalsIgnoreCase("demon-hunter")){
			mClass = getString(R.string.dh_label);
			heroResourseLabel.setText("HATRED/DISCIPLINE");
			dr = getResources().getDrawable(R.drawable.dh_drawable);
		}else if(mHeroModel.getHeroModel().getclass().equalsIgnoreCase("witch-doctor")){
			mClass = getString(R.string.wd_label);
			heroResourseLabel.setText("MANA");
			dr = getResources().getDrawable(R.drawable.mana_drawable);
		}
		resoursesImage.setImageDrawable(dr);
	}

	private void setupProgressBar() {
		final ProgressionProgressBar progressBar = (ProgressionProgressBar)mRootView.findViewById(R.id.hero_progression_bar);
		progressBar.setDataArrayList(mHeroModel.getProgressionBooleanList());
		if(mHeroModel.getHeroModel().getHandrdcore()){
			progressBar.init(Color.RED);
		}else{
			progressBar.init(Color.YELLOW);
		}
	}
}
