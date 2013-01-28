package kpk.dev.d3app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import kpk.dev.d3app.R;
import kpk.dev.d3app.util.Utils;

public class AddProfileDialogFragment extends BaseDialog{
	public static final String BATTLE_TAG_KEY = "battletag_key";
	public static final String SELECTED_REGION_KEY = "selected_region_key";
	public static AddProfileDialogFragment getInstance(Bundle data){
		AddProfileDialogFragment dialogFragment = new AddProfileDialogFragment();
		dialogFragment.setArguments(data);
		return dialogFragment;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	public View onCreateView(android.view.LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(getArguments().getInt(DIALOG_LAYOUT_KEY), container, false);
		final TextView titleView = (TextView)rootView.findViewById(R.id.dialog_title);
		final EditText battleTagView = (EditText)rootView.findViewById(R.id.battle_tag_field);
		final Button okButton = (Button)rootView.findViewById(R.id.ok_button);
		final Button cancelButton = (Button)rootView.findViewById(R.id.cancel_button);
		final RadioGroup regionsGroup = (RadioGroup)rootView.findViewById(R.id.buttons_container);
		
		titleView.setTypeface(Utils.getFont(getActivity()));
		okButton.setTypeface(Utils.getFont(getActivity()));
		cancelButton.setTypeface(Utils.getFont(getActivity()));
		setRadioButtonFonts(rootView);
		
		titleView.setText(getArguments().getString(TITLE_KEY));
		
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getDialogWatcher() != null) {
					final Bundle dataBundle = new Bundle();
					int radioButtonID = regionsGroup.getCheckedRadioButtonId();
					RadioButton regionButton = (RadioButton)regionsGroup.findViewById(radioButtonID);
					dataBundle.putString(BATTLE_TAG_KEY, battleTagView.getText().toString());
					dataBundle.putString(SELECTED_REGION_KEY, regionButton.getText().toString());
					getDialogWatcher().closeDialogsWithData(DialogType.addProfileDialog, getTag(), dataBundle);
				}
			}
		});
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getDialogWatcher() != null) {
					getDialogWatcher().closeDialogs(getTag());
				}
			}
		});
		return rootView;
	};
	
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		
//		return getMainDialogBuilder().create();
//	}

	private void setRadioButtonFonts(View rootView) {
		RadioButton radioButtonUS = (RadioButton)rootView.findViewById(R.id.us_button);
		RadioButton radioButtonEU = (RadioButton)rootView.findViewById(R.id.eu_button);
		RadioButton radioButtonKR = (RadioButton)rootView.findViewById(R.id.kr_button);
		RadioButton radioButtonTW = (RadioButton)rootView.findViewById(R.id.tw_button);
		radioButtonUS.setTypeface(Utils.getFont(getActivity()));
		radioButtonEU.setTypeface(Utils.getFont(getActivity()));
		radioButtonKR.setTypeface(Utils.getFont(getActivity()));
		radioButtonTW.setTypeface(Utils.getFont(getActivity()));
	}
}
