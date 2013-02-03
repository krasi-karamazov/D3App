package kpk.dev.d3app.listeners;

import java.util.List;

import kpk.dev.d3app.models.accountmodels.IProfileModel;

public interface DataReadyListener extends BaseDataListener {
	void dataReadyListener(List<IProfileModel> models);
}
