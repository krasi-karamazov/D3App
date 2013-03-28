package kpk.dev.d3app.tasks;

public enum ModelType {
	heroes(),
	artisans(),
	profiles(),
	profile();
	String mServer;
	String mBattleTag;
	public void profile(String server, String battleTag){
		mServer = server;
		mBattleTag = battleTag;
	}
}
