package kpk.dev.d3app.ui.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.SharedPreferences;
import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.PreferencesAdapter;
import kpk.dev.d3app.models.PreferencesObject;
import kpk.dev.d3app.ui.fragments.SingleChoiceListDialog;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DiabloPreferenceActivity extends FragmentActivity {
    private static final String DIALOG_TAG = "LIST_DIALOG";
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.preferences_layout);
		setUpListView();
	}

	private void setUpListView() {
		final ListView preferencesListView = (ListView)findViewById(R.id.preferences_headers_list);
		Utils.setFont(preferencesListView);
		preferencesListView.setAdapter(new PreferencesAdapter(this, R.layout.preferences_headers_row_layout, R.id.preference_header_title, getOptionsList()));
		preferencesListView.setOnItemClickListener(onPrefsSelectedListener);
	}

    private AdapterView.OnItemClickListener onPrefsSelectedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showSelectedPrefsDialog((PreferencesObject)parent.getItemAtPosition(position));
        }
    };

    private void showSelectedPrefsDialog(PreferencesObject obj) {
        Bundle args = new Bundle();
        args.putString(SingleChoiceListDialog.OPTIONS_TITLE_KEY, obj.getTitle());
        SingleChoiceListDialog.PrefType prefsType = (obj.getTitle().equalsIgnoreCase("update interval"))? SingleChoiceListDialog.PrefType.UpdateInterval : SingleChoiceListDialog.PrefType.CacheOptions;
        args.putString(SingleChoiceListDialog.PREFS_TYPE_KEY, prefsType.name());
        args.putInt(SingleChoiceListDialog.SELECTED_OPTION_KEY, getSelectedOptionFromPrefs(obj.getTitle()));
        args.putStringArrayList(SingleChoiceListDialog.OPTIONS_ARRAY_KEY, obj.getOptionsItems());
        SingleChoiceListDialog dialog = SingleChoiceListDialog.getInstance(args);
        dialog.show(getSupportFragmentManager(), DIALOG_TAG);
    }

    private int getSelectedOptionFromPrefs(String title) {
        SharedPreferences prefs = getSharedPreferences(D3Constants.SHARED_PREFERENCES_FILE, MODE_APPEND);
        String key = (title.equalsIgnoreCase("update interval"))? D3Constants.SHARED_PREFS_UPDATE_INTERVAL_KEY : D3Constants.SHARED_PREFS_CACHE_OPTIONS_KEY;
        return prefs.getInt(key, 0);
    }

	private List<PreferencesObject> getOptionsList() {
		String[] headers = getResources().getStringArray(R.array.settings_headers_array);
        String[] descriptions = getResources().getStringArray(R.array.settings_descriptions_array);
		String[] intervalOpts = getResources().getStringArray(R.array.update_interval_array);
		String[] imagesOpts = getResources().getStringArray(R.array.clear_images_prefs);
        final List<PreferencesObject> prefs = new ArrayList<PreferencesObject>();
        int i = 0;
        for(String header : headers){
            PreferencesObject obj = new PreferencesObject(header, descriptions[i]);
            obj.setTitle(header);
            ArrayList<String> opts;
            if(header.equalsIgnoreCase(headers[0])){
                opts = getOptionsForHeader(intervalOpts);
            }else{
                opts = getOptionsForHeader(imagesOpts);
            }
            obj.setOptionsItems(opts);
            prefs.add(obj);
            i++;
        }
		return prefs;
	}

    private ArrayList<String> getOptionsForHeader(String[] opts) {
        final ArrayList<String> optsList = new ArrayList<String>();
        for(String opt : opts) {
            optsList.add(opt);
        }
        return optsList;
    }
}
