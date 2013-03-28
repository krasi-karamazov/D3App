package kpk.dev.d3app.widgets;

import kpk.dev.d3app.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FollowerView extends LinearLayout {
	public FollowerView(Context context) {
		super(context);
		init(context, null);
	}
	
	public FollowerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.followers_view_layout, this, true);
		if(attrs != null){
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.follower_styleable);
			Drawable drawable = typedArray.getDrawable(R.styleable.follower_styleable_follower_background);
			ImageView backgroundView = (ImageView)findViewById(R.id.follower_paper_doll);
			backgroundView.setImageDrawable(drawable);
			typedArray.recycle();
		}
	}
	
	public void setImage(Bitmap bmp, int imageViewId){
		((ImageView)findViewById(imageViewId)).setImageBitmap(bmp);
	}
	
	public void setImageClickListener(OnClickListener clickListener, int imageViewId){
		findViewById(imageViewId).setOnClickListener(clickListener);
	}
	
	public void setLabel(String text, int labelId){
		((TextView)findViewById(labelId)).setText(text);
	}
}
