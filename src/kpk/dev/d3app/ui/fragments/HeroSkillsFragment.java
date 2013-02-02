package kpk.dev.d3app.ui.fragments;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.models.accountmodels.D3PassiveSkill;
import kpk.dev.d3app.models.accountmodels.D3Rune;
import kpk.dev.d3app.models.accountmodels.D3Skill;
import kpk.dev.d3app.models.accountmodels.HeroModelDecorator;
import kpk.dev.d3app.ui.activities.TooltipActivity;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.widgets.ActiveSkillView;
import kpk.dev.d3app.widgets.PassiveSkillView;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class HeroSkillsFragment extends AbstractFragment<HeroModelDecorator> {
	private HeroModelDecorator mModel;
	private LinearLayout mSkillsHolder;
	private List<D3Skill> mSkills;
	private List<D3PassiveSkill> mPassiveSkills;
	private PassiveSkillView mPassiveSkillsView1;
	private PassiveSkillView mPassiveSkillsView2;
	private PassiveSkillView mPassiveSkillsView3;
	public void setData(HeroModelDecorator data) {
		mModel = data;
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.hero_skills_layout, container, false);
		mSkillsHolder = (LinearLayout)rootView.findViewById(R.id.skills_container);
		mPassiveSkillsView1 = (PassiveSkillView)rootView.findViewById(R.id.passive_skill_1);
		mPassiveSkillsView2 = (PassiveSkillView)rootView.findViewById(R.id.passive_skill_2);
		mPassiveSkillsView3 = (PassiveSkillView)rootView.findViewById(R.id.passive_skill_3);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSkills = mModel.getSkills().getActive();
		mPassiveSkills = mModel.getSkills().getPassive();
		fillSkillsList(mSkills);
		setActiveSkillsData();
		setPassiveSkillsData();
	}
	
	private void setPassiveSkillsData() {
		if(mPassiveSkills == null || mPassiveSkills.size() == 0){
			mPassiveSkillsView1.setVisibility(View.GONE);
			mPassiveSkillsView2.setVisibility(View.GONE);
			mPassiveSkillsView3.setVisibility(View.GONE);
			return;
		}
		if(mPassiveSkills.get(0) != null) {
			mPassiveSkillsView1.setLabel(mPassiveSkills.get(0).getName());
			setImage(D3Constants.SKILL_ICON_URL + mPassiveSkills.get(0).getIcon() + ".png", mPassiveSkillsView1.getImageView());
			mPassiveSkillsView1.setOnClickListener(new SkillClickListener(getPassiveToolTipUrl(mPassiveSkills.get(0))));
		}
		if(mPassiveSkills.size() > 1 && mPassiveSkills.get(1) != null) {
			mPassiveSkillsView2.setLabel(mPassiveSkills.get(1).getName());
			setImage(D3Constants.SKILL_ICON_URL + mPassiveSkills.get(1).getIcon() + ".png", mPassiveSkillsView2.getImageView());
			mPassiveSkillsView2.setOnClickListener(new SkillClickListener(getPassiveToolTipUrl(mPassiveSkills.get(1))));
		}
		if(mPassiveSkills.size() > 2 && mPassiveSkills.get(2) != null) {
			mPassiveSkillsView3.setLabel(mPassiveSkills.get(2).getName());
			setImage(D3Constants.SKILL_ICON_URL + mPassiveSkills.get(2).getIcon() + ".png", mPassiveSkillsView3.getImageView());
			mPassiveSkillsView3.setOnClickListener(new SkillClickListener(getPassiveToolTipUrl(mPassiveSkills.get(2))));
		}
	}
	

	private void setActiveSkillsData() {
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			for(int i = 0; i < mSkills.size(); i++){
				D3Skill skill = mSkills.get(i);
				if(skill != null){
					mSkillsHolder.addView(getPopulatedSkillView(skill, i + 1));
				}
			}
		}else{
			for(int i = 0; i < mSkills.size(); i+= 2){
				D3Skill skill = mSkills.get(i);
				D3Skill rightSkill = null;
				if(i + 1 < mSkills.size()){
					rightSkill = mSkills.get(i + 1);
				}
				LinearLayout activeSkillsRowContainer = new LinearLayout(getActivity());
				activeSkillsRowContainer.setOrientation(LinearLayout.HORIZONTAL);
				activeSkillsRowContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				if(skill != null){
					activeSkillsRowContainer.addView(getPopulatedSkillView(skill, i + 1));
				}
				
				if(rightSkill != null){
					activeSkillsRowContainer.addView(getPopulatedSkillView(rightSkill, i + 2));
				}
				mSkillsHolder.addView(activeSkillsRowContainer);
			}
		}
		
	}
	
	private ActiveSkillView getPopulatedSkillView(D3Skill skill, int skillNum) {
		ActiveSkillView activeSkillView = new ActiveSkillView(getActivity());
		if(skill.getCategorySlug().equalsIgnoreCase("primary")){
			activeSkillView.setSkillNumber(getResources().getDrawable(R.drawable.left_mouse_icon), -1);
		}else if(skill.getCategorySlug().equalsIgnoreCase("secondary")) {
			activeSkillView.setSkillNumber(getResources().getDrawable(R.drawable.right_mouse_icon), -1);
		}else{
			activeSkillView.setSkillNumber(null, skillNum);
		}
		if(skill.getRune() != null){
			activeSkillView.setRuneName(skill.getRune().getName());
			activeSkillView.setRuneImage(getRuneDrawable(skill.getRune()));
		}else{
			activeSkillView.getRuneImageView().setVisibility(View.INVISIBLE);
			activeSkillView.getRuneNameView().setVisibility(View.INVISIBLE);
		}
		setImage(D3Constants.SKILL_ICON_URL + skill.getIcon() + ".png", activeSkillView.getSkillImageView());
		activeSkillView.setSkillName(skill.getName());
		activeSkillView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		activeSkillView.setOnClickListener(new SkillClickListener(getActiveToolTipUrl(skill)));
		return activeSkillView;
	}

	private Drawable getRuneDrawable(D3Rune rune) {
		Drawable runeDrawable = null;
		if(rune.getType().equals("a")){
			runeDrawable = getResources().getDrawable(R.drawable.rune_a);
		}else if(rune.getType().equals("b")){
			runeDrawable = getResources().getDrawable(R.drawable.rune_b);
		}else if(rune.getType().equals("c")){
			runeDrawable = getResources().getDrawable(R.drawable.rune_c);
		}else if(rune.getType().equals("d")){
			runeDrawable = getResources().getDrawable(R.drawable.rune_d);
		}else if(rune.getType().equals("e")){
			runeDrawable = getResources().getDrawable(R.drawable.rune_e);
		}
		return runeDrawable;
	}
	
	private class SkillClickListener implements View.OnClickListener {
		private String mToolTipUrl;
		SkillClickListener(String toolTipUrl){
			mToolTipUrl = toolTipUrl;
		}
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), TooltipActivity.class);
			intent.putExtra(TooltipActivity.TOOLTIP_URL_KEY, mToolTipUrl);
			startActivity(intent);
		}
	}
	
	private void fillSkillsList(List<D3Skill> skills) {
		if(skills.size() < 6){
			while(skills.size() < 6){
				skills.add(null);
			}
		}
	}

	private String getActiveToolTipUrl(D3Skill skill) {
		return "http://" + mModel.getHeroModel().getServer() + D3Constants.SKILL_TOOL_TIP_URL 
				+ mModel.getHeroModel().getclass() + "/" + skill.getSlug() + ((skill.getRune() != null)?"?runeType=" + skill.getRune().getType():"");
	}
	
	private String getPassiveToolTipUrl(D3PassiveSkill skill) {
		return "http://" + mModel.getHeroModel().getServer() + D3Constants.SKILL_TOOL_TIP_URL 
				+ mModel.getHeroModel().getclass() + "/" + skill.getSlug();
	}
}
