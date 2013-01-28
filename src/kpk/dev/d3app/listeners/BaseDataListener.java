package kpk.dev.d3app.listeners;

import kpk.dev.d3app.models.accountmodels.IProfileModel;

public interface BaseDataListener {
	void dataReady(IProfileModel model, boolean newObject, String[] returnedArgs);
}
