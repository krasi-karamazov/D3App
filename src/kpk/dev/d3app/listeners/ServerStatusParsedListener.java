package kpk.dev.d3app.listeners;

import java.util.List;

import kpk.dev.d3app.models.bnetmodels.Region;

public interface ServerStatusParsedListener {
	void serverStatusParsed(List<Region> regions);
	void serverStatusParseProgress();
}
