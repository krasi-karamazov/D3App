package kpk.dev.d3app.widgets;


import kpk.dev.d3app.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TimePlayedWidget extends ImageView {
	private double mProgress;
	private Paint mPaint;
	private static final String BARBARIAN_COLOR = "#ff0000";
	private static final String DEMON_HUNTER_COLOR = "#FF33CC";
	private static final String MONK_COLOR = "#FF6600";
	private static final String WITCH_DOCTOR_COLOR = "#339900";
	private static final String WIZARD_COLOR = "#3399FF";
	public enum ClassColor {
		barbarian(),
		demon_hunter(),
		monk(),
		witch_doctor(),
		wizard();
	}
	public TimePlayedWidget(Context context){
		super(context);
	}
	
	public TimePlayedWidget(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public TimePlayedWidget(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public void setData(double progress, ClassColor classColor) {
		mProgress = progress;
		int color = -1;
		if(classColor.name().equalsIgnoreCase(ClassColor.barbarian.name())){
			color = Color.parseColor(BARBARIAN_COLOR);
		}else if(classColor.name().equalsIgnoreCase(ClassColor.demon_hunter.name())){
			color = Color.parseColor(DEMON_HUNTER_COLOR);
		}else if(classColor.name().equalsIgnoreCase(ClassColor.monk.name())){
			color = Color.parseColor(MONK_COLOR);
		}else if(classColor.name().equalsIgnoreCase(ClassColor.witch_doctor.name())){
			color = Color.parseColor(WITCH_DOCTOR_COLOR);
		}else if(classColor.name().equalsIgnoreCase(ClassColor.wizard.name())){
			color = Color.parseColor(WIZARD_COLOR);
		}
		init(color);
	}
	
	@SuppressWarnings("deprecation")
	public void init(int color) {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(color);
		mPaint.setAlpha(70);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.portrait_frame));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mPaint == null) return;
		//KPKLog.d("PROGRESS " + mProgress);
		canvas.drawRect(0, getMeasuredHeight() - (int)(getMeasuredHeight()  * mProgress), getMeasuredWidth(), getMeasuredHeight(), mPaint);
	}
}
