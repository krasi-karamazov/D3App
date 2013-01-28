package kpk.dev.d3app.widgets;

import java.util.List;

import kpk.dev.d3app.util.KPKLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ProgressionProgressBar extends View {
	private List<Boolean> mList;
	private Paint mPaint;
	private Paint mSegmentPaint;
	private int mLastCompletedAct = -1;
	public ProgressionProgressBar(Context context) {
		super(context);
	}
	
	public ProgressionProgressBar(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public ProgressionProgressBar(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public void init(int color) {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(color);
		mSegmentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mSegmentPaint.setColor(Color.BLACK);
	}
	
	public void setDataArrayList(List<Boolean> list) {
		mList = list;
		int j = mList.size();
		if(mList == null || mList.size() == 0) return;
		while(j > 0){
			j--;
			if(mList.get(j) == true){
				mLastCompletedAct = j;
				break;
			}
			
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(mList == null) return;
		
		for(int i = 0; i < mLastCompletedAct + 1; i++) {
			
			float segmentWidth = (getWidth() / mList.size());
			canvas.drawRect(i * segmentWidth + 2, 0,  (i * segmentWidth) + segmentWidth, getHeight(), mPaint);
			canvas.save();
			canvas.restore();
		}
	}
	
}
