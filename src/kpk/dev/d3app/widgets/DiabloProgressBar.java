package kpk.dev.d3app.widgets;

import kpk.dev.d3app.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class DiabloProgressBar extends ProgressBar {
	
	
	public DiabloProgressBar(Context context) {
		super(context);
		init(Color.RED);
	}
	
	public DiabloProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(Color.RED);
	}
	
	public DiabloProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray attrsArray = getContext().obtainStyledAttributes(attrs, R.styleable.progress_styleable);
		int color = attrsArray.getColor(R.styleable.progress_styleable_progress_color, Color.RED);
		init(color);
		attrsArray.recycle();
	}
	
	private void init(int color) {
		this.setIndeterminate(true);
		PorterDuffColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
		this.getIndeterminateDrawable().setColorFilter(filter);
	}
	
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
