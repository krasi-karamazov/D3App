package kpk.dev.d3app.ui.activities;

import java.util.List;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.PreferencesAdapter;
import kpk.dev.d3app.models.PreferencesObject;
import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DiabloPreferenceActivity extends FragmentActivity {
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
		preferencesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View item, int position, long id) {
				
			}
		});
	}

	private List<PreferencesObject> getOptionsList() {
		String[] headers = getResources().getStringArray(R.array.settings_headers_array);
		String[] intervalOpts = getResources().getStringArray(R.array.update_interval_array);
		String[] imagesOpts = getResources().getStringArray(R.array.clear_images_prefs);
		return null;
	}
}
