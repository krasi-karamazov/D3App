package kpk.dev.d3app.ui.interfaces;

import kpk.dev.d3app.ui.fragments.BaseDialog.DialogType;
import android.os.Bundle;

public interface IDialogWatcher {
	void closeDialogs(String tag);
	void closeDialogsWithData(DialogType type, String tag, Bundle dialogData);
	enum QueryTypes{
		deleteData(),
		loadData();
	}
}
