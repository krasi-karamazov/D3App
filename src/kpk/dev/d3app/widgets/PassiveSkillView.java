package kpk.dev.d3app.widgets;

import kpk.dev.d3app.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PassiveSkillView extends RelativeLayout {
	private ImageView mPassiveSkillImage;
	private TextView mPassiveSkillLabel;
	public PassiveSkillView(Context context){
		super(context);
		init(context);
	}
	
	public PassiveSkillView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	public PassiveSkillView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.passive_skills_button_layout, this, true);
		mPassiveSkillImage = (ImageView)findViewById(R.id.passive_skill_image);
		mPassiveSkillLabel = (TextView)findViewById(R.id.passive_skill_label);
	}
	
	public void setImage(Bitmap bmp){
		mPassiveSkillImage.setImageBitmap(bmp);
	}
	
	public ImageView getImageView(){
		return mPassiveSkillImage;
	}
	
	public void setLabel(String text) {
		mPassiveSkillLabel.setText(text);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		final Button button = (Button)findViewById(R.id.passive_skill_button);
		button.setOnClickListener(l);
	}
}
