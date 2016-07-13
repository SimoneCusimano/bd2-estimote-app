package android.com.estimoteandroidapp.estimoteandroidapp;

import android.os.AsyncTask;
import android.util.Log;

import com.estimote.sdk.Nearable;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class EstimoteApiTask extends AsyncTask<URL, Integer, Long> {
    private static final String TAG = "EstimoteApiTask";

    private List<Nearable> _nearables;

    public EstimoteApiTask(List<Nearable> nearables) {
        this._nearables = nearables;
    }

    @Override
    protected Long doInBackground(URL... urls) {
        int statusCode = -1;

        for (URL url : urls) {
            // Making HTTP request
            try {
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();
                String encoding = connection.getContentEncoding();
                encoding = encoding == null ? "UTF-8" : encoding;
                String body = IOUtils.toString(in, encoding);
                statusCode = ((HttpURLConnection)connection).getResponseCode();
                Log.d(TAG, body);
            }
            catch (Exception ex)
            {
                Log.d(TAG, ex.getStackTrace().toString());
            }

        }

        return Long.valueOf(statusCode);
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.d(TAG, "Result: " + result);
    }
}
