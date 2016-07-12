package activitytest.android.com.estimoteandroidapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class EstimoteApiTask extends AsyncTask<URL, Integer, Long> {
    private static final String TAG = "EstimoteApiTask";

    @Override
    protected Long doInBackground(URL... urls) {

        for (URL url : urls) {
            // Making HTTP request
            try {
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();
                String encoding = connection.getContentEncoding();
                encoding = encoding == null ? "UTF-8" : encoding;
                String body = IOUtils.toString(in, encoding);
                Log.d(TAG, body);
            }
            catch (Exception ex)
            {
                Log.d(TAG, ex.getStackTrace().toString());
            }

        }

        return Long.valueOf(0);
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.d(TAG, "Result: " + result);
    }
}
