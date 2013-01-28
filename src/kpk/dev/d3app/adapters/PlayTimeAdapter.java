package kpk.dev.d3app.adapters;

import java.util.Map;

import kpk.dev.d3app.R;
import kpk.dev.d3app.widgets.TimePlayedWidget;
import kpk.dev.d3app.widgets.TimePlayedWidget.ClassColor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class PlayTimeAdapter extends BaseAdapter {
	private Context mContext;
	private Map<String, Double> mItems;
	public PlayTimeAdapter(Context context, Map<String, Double> map) {
		mContext = context;
		mItems = map;
	}
	@Override
	public int getCount() {
		return mThumbIds.length;
	}

	@Override
	public Map.Entry<String, Double> getItem(int position) {
		int i = 0;
		Map.Entry<String, Double> item = null;
		for(Map.Entry<String, Double> entry : mItems.entrySet()){
			if(i == position){
				item = entry;
			}
			i++;
		}
		return item;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TimePlayedWidget timePlayedView;
		if (convertView == null) {
			timePlayedView = new TimePlayedWidget(mContext);
			timePlayedView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, GridView.LayoutParams.WRAP_CONTENT));
        } else {
        	timePlayedView = (TimePlayedWidget) convertView;
        }
		timePlayedView.setImageResource(mThumbIds[position]);
		timePlayedView.setData(getItem(position).getValue(), classes[position]);
		timePlayedView.invalidate();
		return timePlayedView;
	}
	
	private Integer[] mThumbIds = {
           R.drawable.barbarian_progress, 
           R.drawable.dhunter_progress, 
           R.drawable.monk_progress, 
           R.drawable.wdoctor_progress, 
           R.drawable.wizard_progress
    };
	private ClassColor[] classes = {
			ClassColor.barbarian, 
			ClassColor.demon_hunter, 
			ClassColor.monk, 
			ClassColor.witch_doctor, 
			ClassColor.wizard
	};
}
