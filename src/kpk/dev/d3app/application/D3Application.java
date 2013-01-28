package kpk.dev.d3app.application;

import java.io.File;

import kpk.dev.d3app.R;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
import kpk.dev.d3app.util.Utils;
import android.app.Application;
import android.widget.Toast;

public class D3Application extends Application implements D3ApplicationBase {

	@Override
	public void onCreate() {
		super.onCreate();
		KPKLog.DEBUG_MODE = Boolean.TRUE;
		KPKLog.LOG_TAG = "D3Application";
		if(Utils.getIsStorageAvailable()) {
			File appDir = new File(D3Constants.getExternalImageDirectory());
			if(!appDir.exists()) {
				appDir.mkdirs();
			}
		}else {
			Toast.makeText(this, getString(R.string.external_storage_error_message), Toast.LENGTH_SHORT).show();
			System.exit(0);
			return;
		}
	}
}
