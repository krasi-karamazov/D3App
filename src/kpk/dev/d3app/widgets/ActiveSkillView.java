package kpk.dev.d3app.widgets;

import kpk.dev.d3app.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActiveSkillView extends RelativeLayout {
	private TextView mSkillName;
	private TextView mRuneName;
	private ImageView mSkillIcon;
	private ImageView mRuneImage;
	private TextView mSkillNumber;
	public ActiveSkillView(Context context){
		super(context);
		init(context);
	}
	
	public ActiveSkillView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	public ActiveSkillView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs,defStyle);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.active_skills_button_layout, this, true);
		
		mSkillNumber = (TextView)findViewById(R.id.skill_category_number);
		mSkillName = (TextView)findViewById(R.id.skill_name);
		mRuneName = (TextView)findViewById(R.id.rune_name);
		mSkillIcon = (ImageView)findViewById(R.id.skill_icon);
		mRuneImage = (ImageView)findViewById(R.id.rune_image);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		Button activeSkillButton = (Button)findViewById(R.id.active_skill_button);
		activeSkillButton.setOnClickListener(l);
	}
	
	public void setSkillName(String skillName){
		mSkillName.setText(skillName);
	}
	
	public void setRuneName(String runeName){
		mRuneName.setText(runeName);
	}
	
	public void setSkillImage(Bitmap skillIcon){
		mSkillIcon.setImageBitmap(skillIcon);
	}
	
	public void setRuneImage(Drawable runeImage){
		mRuneImage.setImageDrawable(runeImage);
	}
	
	public ImageView getSkillImageView() {
		return mSkillIcon;
	}
	
	public ImageView getRuneImageView() {
		return mRuneImage;
	}
	
	public TextView getRuneNameView() {
		return mRuneName;
	}
	
	@SuppressWarnings("deprecation")
	public void setSkillNumber(Drawable skillNumberDrawable, int number){
		if(number > 0){
			mSkillNumber.setText(Integer.valueOf(number).toString());
		}else if(skillNumberDrawable != null) {
			mSkillNumber.setBackgroundDrawable(skillNumberDrawable);
		}
	}
}
