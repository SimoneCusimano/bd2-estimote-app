package android.com.estimoteandroidapp.estimoteandroidapp;

import android.os.AsyncTask;
import android.util.Log;

import com.estimote.sdk.Nearable;
import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EstimoteApiTask extends AsyncTask<List<Nearable>, Integer, Long> {
    private static final String TAG = "EstimoteApiTask";
    private String API_ABSOLUTE_URL = "http://estimote-api.azurewebsites.net";
    private String API_TEST_CALL = "/api/test";
    private String API_ESTIMOTE_CALL = "/api/estimotes";

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected Long doInBackground(List<Nearable>... nearables) {
        int statusCode = -1;

            try {
                Gson gson = new Gson();
                String json = gson.toJson(nearables);
                Log.d(TAG, "Sending: " + json);

                statusCode = this.sendData("Estimotes", json);

            }
            catch (IOException e) {
                Log.d(TAG, "Exception message: " + e.getMessage());
            }

        return Long.valueOf(statusCode);
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.d(TAG, "Result: " + result);
    }

    public int sendData(String key, String value) throws IOException {
        int responseStatusCode;

        RequestBody formBody = new FormBody.Builder()
                .add(key, String.valueOf(value))
                .build();

        Request request = new Request.Builder()
                .url(API_ABSOLUTE_URL + API_ESTIMOTE_CALL)
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String responseMessage = response.body().string();
        responseStatusCode = response.code();


        Log.d(TAG, responseMessage);

        return responseStatusCode;
    }

}