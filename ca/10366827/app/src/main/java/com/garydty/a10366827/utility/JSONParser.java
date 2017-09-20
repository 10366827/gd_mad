package com.garydty.a10366827.utility;

/**
 * Created by Gary Doherty on 17/09/2017.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JSONParser {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    // constructor
    public JSONParser() {
        client = new OkHttpClient();
    }

    public JSONObject getJSONFromUrl(String url) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        ResponseBody response = client.newCall(request).execute().body();
        if(response == null)
            throw new IOException(getClass().getSimpleName() + "[getJSONFromUrl]: response null.");

        return ( new JSONObject(response.string()) );
    }

    String postJSON(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response == null)
            throw new IOException(getClass().getSimpleName() + "[post]: response null.");

        return response.body().string();
    }
}
