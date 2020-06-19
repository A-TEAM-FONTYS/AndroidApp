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


public class AppStatHandler extends AsyncTask<String, String, String> {
    protected String doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://fitphone-ateam.herokuapp.com/data";
        String json = getJsonAsString(strings[0], strings[1]);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .post(body)
                .header("Authorization", "Bearer "+ strings[2])
                .url(url)
                .build();

        Call call = client.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getResponseString(response);
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

    private String getJsonAsString(String appName, String timeUsed){
        String json = null;
        try {
            json = new JSONObject()
                    .put("appName", appName)
                    .put("timeUsed", Integer.parseInt(timeUsed))
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getResponseString(Response response){
        String responseString = null;
        try {
            responseString = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response.code() == 400){
            responseString = "Something went wrong. Please try again or contact our support";
        }

        return responseString;
    }
}
