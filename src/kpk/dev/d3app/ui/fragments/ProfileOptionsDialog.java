package kpk.dev.d3app.ui.fragments;

import kpk.dev.d3app.R;
import kpk.dev.d3app.adapters.ProfileOptionsAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class ProfileOptionsDialog extends BaseDialog {
	public static final String PROFILE_POSITION_KEY = "profile_position";
	public static final String SELECTED_OPTION_KEY = "selected_option";
	private int mSelectedProfilePosition;
	
	public enum ProfileOptions{
		delete(),
		refresh();
	}
	
	public static ProfileOptionsDialog getInstance(Bundle data){
		ProfileOptionsDialog dialogFragment = new ProfileOptionsDialog();
		dialogFragment.setArguments(data);
		return dialogFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSelectedProfilePosition = getArguments().getInt(PROFILE_POSITION_KEY);
		View rootView = inflater.inflate(getArguments().getInt(DIALOG_LAYOUT_KEY), container, false);
		final ListView optionsView = (ListView)rootView.findViewById(R.id.profile_options_list);
		optionsView.setAdapter(new ProfileOptionsAdapter(getActivity(), R.layout.profile_options_item, getActivity().getResources().getStringArray(R.array.profile_options_array)));
		optionsView.setOnItemClickListener(onOptionsClickListener);
		return rootView;
	}
	
	private AdapterView.OnItemClickListener onOptionsClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(android.widget.AdapterView<?> listView, View item, int position, long id) {
			if(getDialogWatcher() != null) {
				final Bundle dialogData = new Bundle();
				dialogData.putInt(PROFILE_POSITION_KEY, mSelectedProfilePosition);
				dialogData.putString(SELECTED_OPTION_KEY, getActivity().getResources().getStringArray(R.array.profile_options_array)[position]);
				getDialogWatcher().closeDialogsWithData(DialogType.profileOptionsDialog, getTag(), dialogData);
			}
		};
	};
}
