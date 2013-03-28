package kpk.dev.d3app.adapters;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.models.bnetmodels.Server;
import kpk.dev.d3app.util.Utils;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ServersAdapter extends ArrayAdapter<Server> {
	
	public ServersAdapter(Context context, int layoutID, List<Server> servers){
		super(context, layoutID, servers);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if(row == null){
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.server_item_layout, parent, false);
		}
		TextView serverName = (TextView)row.findViewById(R.id.server_name);
		ImageView serverIndcatior = (ImageView)row.findViewById(R.id.server_indicator_arrow);
		serverIndcatior.setImageDrawable((getItem(position).getIsAvailable().equalsIgnoreCase("available")?getContext().getResources().getDrawable(R.drawable.green_arrow):getContext().getResources().getDrawable(R.drawable.red_arrow)));
		Typeface diabloFont = Utils.getFont(getContext());
		serverName.setTypeface(diabloFont);
		serverName.setText(getItem(position).getName());
		return row;
	}
}
