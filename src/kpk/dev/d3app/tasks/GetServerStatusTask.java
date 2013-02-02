package kpk.dev.d3app.tasks;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes;
import net.htmlparser.jericho.Source;
import kpk.dev.d3app.listeners.ServerStatusParsedListener;
import kpk.dev.d3app.models.bnetmodels.Region;
import kpk.dev.d3app.models.bnetmodels.Server;
import kpk.dev.d3app.util.D3Constants;

public class GetServerStatusTask extends Thread {
	private ServerStatusParsedListener mListener;
	private List<Region> mRegions;
	public GetServerStatusTask(ServerStatusParsedListener listener){
		mListener = listener;
		mRegions = new ArrayList<Region>();
	}
	
	@Override
	public void run() {
		super.run();
		mListener.serverStatusParseProgress();
		MicrosoftConditionalCommentTagTypes.register();
		MasonTagTypes.register();
		URL serversURL = null;
		try{
			serversURL = new URL(D3Constants.SERVERS_PAGE_URL);
		}catch(MalformedURLException e){}
		if(serversURL == null) {
			
			mListener.serverStatusParsed(null);
			return;
		}
		try{
			URLConnection connection = serversURL.openConnection();
			InputStream is = connection.getInputStream();
			StringBuilder stringBuilder = new StringBuilder();
			int c;
			while((c = is.read()) != -1) {
				stringBuilder.append((char)c);	
			}
			mListener.serverStatusParseProgress();
			Source source = new Source(stringBuilder.toString());
			getTags(source.getAllElementsByClass("box"));
			mListener.serverStatusParsed(mRegions);
		}catch(IOException e){
			e.printStackTrace();
			mListener.serverStatusParsed(null);
			return;
		}
		
	}

	private void getTags(List<Element> elements) {
		for (Element segment : elements) {
			final Region region = new Region();
			final String regionName = segment.getAllElementsByClass("header-3").get(0).getTextExtractor().toString();
			region.setName(regionName);
			final List<Server> servers = new ArrayList<Server>();
			final List<Element> serversElements= segment.getAllElementsByClass("server");
			for(Element serverElement : serversElements) {
				String serverName = serverElement.getAllElementsByClass("server-name").get(0).getContent().getTextExtractor().toString();
				String serverNameTrimmed = serverName.trim();
				
				if(serverNameTrimmed.indexOf("&#160;") == -1 && serverNameTrimmed.length() > 0){
					String isServerAvailable = serverElement.getAllElementsByClass("status-icon").get(0).getAttributeValue("data-tooltip");
					servers.add(new Server(serverName, isServerAvailable, regionName));
				}
			}
			region.setServers(servers);
			mRegions.add(region);
			mListener.serverStatusParseProgress();
		}
	}
}
