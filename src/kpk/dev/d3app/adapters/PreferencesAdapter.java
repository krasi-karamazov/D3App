package kpk.dev.d3app.adapters;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.models.PreferencesObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PreferencesAdapter extends ArrayAdapter<PreferencesObject> {
	public PreferencesAdapter(Context context, int layoutId, int textViewId, List<PreferencesObject> objects){
		super(context, layoutId, textViewId, objects);
	}
	
	static class ViewHolder{
		TextView titleField;
		TextView descriptionField;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if(row == null){
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.preferences_headers_row_layout, parent, false);
			holder.titleField = (TextView)row.findViewById(R.id.preference_header_title);
			holder.descriptionField = (TextView)row.findViewById(R.id.preference_header_description);
			row.setTag(holder);
		}else{
			holder = (ViewHolder)row.getTag();
		}
		holder.titleField.setText(getItem(position).getTitle());
		holder.descriptionField.setText(getItem(position).getmDescription());
		return row;
	}
}
