package kpk.dev.d3app.ui.activities;

import kpk.dev.d3app.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
		preferencesListView.setAdapter(new ArrayAdapter<String>(this, R.layout.preferences_headers_row_layout, R.id.preference_header_label, getResources().getStringArray(R.array.settings_headers_array)));
	}
}
