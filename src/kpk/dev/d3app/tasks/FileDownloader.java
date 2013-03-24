package kpk.dev.d3app.tasks;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import kpk.dev.d3app.cache.MemoryCache;
import kpk.dev.d3app.listeners.FileDownloadListener;

public class FileDownloader {
	private Thread downloaderThread;
	private MemoryCache mMemoryChache;
	public FileDownloader(Context context) {
		mMemoryChache = MemoryCache.getInstance(context);
	}
	
	public void downloadFile(final int density, final String fileUrl, final File filePath, final FileDownloadListener listener) {
		Runnable downloadRunnable = new Runnable() {
			
			@Override
			public void run() {
				if(filePath.exists()) {
					//KPKLog.d("File exists. Decoding and returning");
					new BitmapDecodeTask(density, mMemoryChache, listener).execute(filePath);
					return;
				}
				final File parentDir = new File(filePath.getParent());
				if(!parentDir.exists()){
					parentDir.mkdirs();
				}
				//KPKLog.d("File doesn't exist. Downloading...");
				URL url;
				DataInputStream dataStream;
				URLConnection urlConnection;
				byte[] fileData;
				try{
					url = new URL(fileUrl);
					urlConnection = url.openConnection();
					dataStream = new DataInputStream(urlConnection.getInputStream());
					fileData = new byte[urlConnection.getContentLength()];
					for(int i = 0; i < fileData.length; i++) {
						fileData[i] = dataStream.readByte();
					}
					dataStream.close();
					
					final FileOutputStream fos = new FileOutputStream(filePath);
					fos.write(fileData);
					fos.close();
					new BitmapDecodeTask(density, mMemoryChache, listener).execute(filePath);
				}catch(MalformedURLException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
				
			}
		};
		downloaderThread = new Thread(downloadRunnable);
		downloaderThread.start();
	}
}
