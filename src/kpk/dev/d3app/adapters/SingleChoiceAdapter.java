package kpk.dev.d3app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import kpk.dev.d3app.R;
import kpk.dev.d3app.models.SingleChoiceItemObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kpkdev
 * Date: 3/24/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleChoiceAdapter extends ArrayAdapter<SingleChoiceItemObject> {

    static class ViewHolder{
        TextView titleField;
        RadioButton radioButton;
    }

    public SingleChoiceAdapter(Context context, int layoutId, int textViewId, List<SingleChoiceItemObject> items){
        super(context, layoutId, textViewId, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if(row == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_choice_item, parent, false);
            holder.titleField = (TextView)row.findViewById(R.id.preference_header_title);
            holder.radioButton = (RadioButton)row.findViewById(R.id.pref_button);
            row.setTag(holder);
        }else {
            holder = (ViewHolder)row.getTag();
        }
        holder.titleField.setText(getItem(position).getTitle());
        holder.radioButton.setChecked(getItem(position).getisChecked());
        return row;
    }
}
