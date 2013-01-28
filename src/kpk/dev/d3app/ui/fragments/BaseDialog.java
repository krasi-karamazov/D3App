package kpk.dev.d3app.ui.fragments;

import kpk.dev.d3app.ui.interfaces.IDialogWatcher;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseDialog extends DialogFragment {
	private IDialogWatcher mDialogWatcher;
	public static final String DIALOG_LAYOUT_KEY = "dialog_layout";
	public static String TITLE_KEY = "dialog_title";
	public enum DialogType{
		addProfileDialog(),
		profileOptionsDialog(),
		heroOptionsDialog();
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		setRetainInstance(true);
	}
	
	public final void setDialogWatcher(IDialogWatcher watcher) {
		mDialogWatcher = watcher;
	}
	
	public final IDialogWatcher getDialogWatcher() {
		return mDialogWatcher;
	}
	
	protected final View getDialogView(int layoutID) {
		LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialogView = inflater.inflate(layoutID, null, false);
		return dialogView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, android.R.style.Theme_Dialog);
	}
}
