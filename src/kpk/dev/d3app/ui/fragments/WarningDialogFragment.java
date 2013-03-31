package kpk.dev.d3app.ui.fragments;

import kpk.dev.d3app.R;
import kpk.dev.d3app.util.Utils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WarningDialogFragment extends BaseDialog {
	public static String MESSAGE_KEY = "dialog_message";
	
	public static WarningDialogFragment getInstance(Bundle data){
		WarningDialogFragment dialogFragment = new WarningDialogFragment();
		dialogFragment.setArguments(data);
		return dialogFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(getArguments().getInt(DIALOG_LAYOUT_KEY), container, false);
		//Utils.setFont((ViewGroup)titleView);
		final TextView titleView = (TextView)rootView.findViewById(R.id.dialog_title);
		titleView.setTypeface(Utils.getFont(getActivity()));
		final TextView messageView = (TextView)rootView.findViewById(R.id.dialog_message);
		//messageView.setTypeface(Utils.getFont(getActivity()));
		final Button okButton = (Button)rootView.findViewById(R.id.ok_button);
		okButton.setTypeface(Utils.getFont(getActivity()));
		titleView.setText(getArguments().getString(TITLE_KEY));
		messageView.setText(getArguments().getString(MESSAGE_KEY));
		
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getDialogWatcher() != null) {
					getDialogWatcher().closeDialogs(getTag());
				}
			}
		});
		return rootView;
	}
}
