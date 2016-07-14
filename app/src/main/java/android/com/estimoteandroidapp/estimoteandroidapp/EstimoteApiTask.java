package android.com.estimoteandroidapp.estimoteandroidapp;

import android.os.AsyncTask;
import android.util.Log;

import com.estimote.sdk.Nearable;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
            try {
                JSONArray jsonArray = new JSONArray(_nearables);

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("Estimotes", String.valueOf(jsonArray))
                        .build();

                Request request = new Request.Builder()
                        .url(url.toString())
                        .post(formBody)
                        .build();

                Response response = client.newCall(request).execute();
                String rm = response.body().string();
                Log.d(TAG, rm);
            } catch (IOException e) {
                Log.d(TAG, "Error: " + e.getMessage());
            }

        }

        return Long.valueOf(statusCode);
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.d(TAG, "Result: " + result);
    }
}