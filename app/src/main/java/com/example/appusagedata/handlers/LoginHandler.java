package com.example.appusagedata.handlers;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginHandler extends AsyncTask<String, String, String> {
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://fitphone-ateam.herokuapp.com/auth/login";
        String json = null;
        try {
            json = new JSONObject()
                    .put("email", strings[0])
                    .put("password", strings[1])
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .post(body)
                .url(url)
                .build();

        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseString = null;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String response) {
        //do stuff
        getResponse(response);
    }

    private String getResponse(String response) {
        //handle value
        return response;
    }
}
