package kpk.dev.d3app.tasks;

import android.os.AsyncTask;
import kpk.dev.d3app.listeners.ICleanUpCompletedListener;
import kpk.dev.d3app.util.D3Constants;
import kpk.dev.d3app.util.KPKLog;
import kpk.dev.d3app.util.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kpkdev
 * Date: 3/24/13
 * Time: 10:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class CleanupTask extends AsyncTask<Long, Void, Void> {

    private ICleanUpCompletedListener mListener;

    public CleanupTask(ICleanUpCompletedListener listener) {
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Long... params) {
        long howOld = params[0];

        if(Utils.getIsStorageAvailable()) {
            File dir = new File(D3Constants.getExternalImageDirectory());
            if(dir.exists() && dir.isDirectory()) {
                long thresholdTime = System.currentTimeMillis() - howOld;
                File[] files = dir.listFiles();
                for(File file : files){
                    long lastModifiedTime = file.lastModified();
                    if(thresholdTime > lastModifiedTime){
                        file.delete();
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(mListener != null) {
            mListener.cleanUpCompleted();
        }
    }
}
