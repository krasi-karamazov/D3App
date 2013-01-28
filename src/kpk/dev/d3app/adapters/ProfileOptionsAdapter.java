package kpk.dev.d3app.adapters;

import kpk.dev.d3app.R;
import kpk.dev.d3app.util.Utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProfileOptionsAdapter extends ArrayAdapter<String> {
	
	private static class ViewHolder{
		TextView txtView;
	}
	private String[] mItems;
	public ProfileOptionsAdapter(Context context, int layoutID, String[] items){
		super(context, android.R.layout.simple_list_item_single_choice, layoutID, items);
		mItems = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View row = convertView;
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.profile_options_item, parent, false);
			holder = new ViewHolder();
			holder.txtView = (TextView)row.findViewById(R.id.options_label);
			row.setTag(holder);
		}else{
			holder = (ViewHolder)row.getTag();
		}
		holder.txtView.setTypeface(Utils.getFont(getContext()));
		holder.txtView.setText(mItems[position]);
		return row;
	}
}
