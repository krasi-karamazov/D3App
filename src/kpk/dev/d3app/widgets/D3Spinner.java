package kpk.dev.d3app.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.Spinner;

public class D3Spinner extends Spinner {
	
	private OnItemClickListener mOnItemClickListener;
	
	public D3Spinner(Context context) {
		super(context);
	}
	
	public D3Spinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public D3Spinner(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener l) {
		this.mOnItemClickListener = l;
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		if(this.mOnItemClickListener != null) {
			this.mOnItemClickListener.onItemClick(this, getSelectedView(), getSelectedItemPosition(), getSelectedItemId());
		}
	}
}
