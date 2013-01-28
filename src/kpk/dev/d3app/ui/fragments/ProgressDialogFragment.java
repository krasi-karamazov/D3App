package kpk.dev.d3app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProgressDialogFragment extends BaseDialog {
	
	public static ProgressDialogFragment getInstance(Bundle data){
		ProgressDialogFragment dialogFragment = new ProgressDialogFragment();
		dialogFragment.setArguments(data);
		return dialogFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(getArguments().getInt(DIALOG_LAYOUT_KEY), container, false);
		return rootView;
	}
	
	@Override
	public void onDestroyView() {
	    if (getDialog() != null && getRetainInstance())
	        getDialog().setDismissMessage(null);
	    super.onDestroyView();
	}
}
