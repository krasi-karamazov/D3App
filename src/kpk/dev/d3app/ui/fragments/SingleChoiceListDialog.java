package kpk.dev.d3app.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.SingleChoiceAdapter;
import kpk.dev.d3app.models.SingleChoiceItemObject;
import kpk.dev.d3app.util.D3Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kpkdev
 * Date: 3/24/13
 * Time: 4:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleChoiceListDialog extends DialogFragment {

    public static final String OPTIONS_TITLE_KEY = "title_key";
    public static final String OPTIONS_ARRAY_KEY = "options_array";
    public static final String SELECTED_OPTION_KEY = "selected_option";
    public static final String PREFS_TYPE_KEY = "pref_type";
    private List<SingleChoiceItemObject> mItemsList;
    public enum PrefType{
        UpdateInterval,
        CacheOptions;
    }
    private int mSelectedPosition;
    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        setRetainInstance(true);
    }

    public static SingleChoiceListDialog getInstance(Bundle bundle) {
        SingleChoiceListDialog dialog = new SingleChoiceListDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString(OPTIONS_TITLE_KEY));
        builder.setPositiveButton(R.string.ok_button, dialogOnClickListener);
        builder.setNegativeButton(R.string.cancel_button, dialogOnClickListener);

        populateList();
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.single_choice_list_layout, null, true);
        ListView singleChoiceList = (ListView)rootView.findViewById(R.id.single_choice_listview);
        singleChoiceList.setAdapter(new SingleChoiceAdapter(getActivity(), android.R.layout.simple_list_item_1, R.layout.single_choice_item, mItemsList));
        singleChoiceList.setOnItemClickListener(onItemClickListener);
        builder.setView(rootView);
        return builder.show();
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SingleChoiceItemObject obj = mItemsList.get(position);
            obj.setChecked(true);
            for(int i = 0; i < mItemsList.size(); i++) {
                if(i != position){
                    mItemsList.get(i).setChecked(false);
                }
            }
            mSelectedPosition = position;
            ((SingleChoiceAdapter)parent.getAdapter()).notifyDataSetChanged();
        }
    };

    DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch(which){
                case Dialog.BUTTON_POSITIVE:
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(D3Constants.SHARED_PREFERENCES_FILE, Context.MODE_APPEND).edit();
                    if(getArguments().getString(PREFS_TYPE_KEY).equalsIgnoreCase(PrefType.UpdateInterval.name())) {
                        editor.putInt(D3Constants.SHARED_PREFS_UPDATE_INTERVAL_KEY, mSelectedPosition);
                    }else{
                        editor.putInt(D3Constants.SHARED_PREFS_CACHE_OPTIONS_KEY, mSelectedPosition);
                    }
                    editor.commit();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    dismiss();
                    break;

            }
        }
    };

    private void populateList() {
        final ArrayList<String> opts = getArguments().getStringArrayList(OPTIONS_ARRAY_KEY);
        final int selected = getArguments().getInt(SELECTED_OPTION_KEY);
        mItemsList = new ArrayList<SingleChoiceItemObject>();
        for(int i = 0; i < opts.size(); i++) {
            SingleChoiceItemObject obj = new SingleChoiceItemObject();
            obj.setTitle(opts.get(i));
            if(i == selected){
                obj.setChecked(true);
            }else{
                obj.setChecked(false);
            }
            mItemsList.add(obj);
        }
    }
}
