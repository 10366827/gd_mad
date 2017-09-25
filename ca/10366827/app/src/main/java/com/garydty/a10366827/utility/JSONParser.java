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

import com.garydty.a10366827.BuildConfig;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JSONParser {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final String API_KEY = BuildConfig.API_KEY;
    private OkHttpClient client;
    private static Headers requestHeaders;

    // constructor
    public JSONParser() {
        client = new OkHttpClient();
        if(requestHeaders == null){
            requestHeaders = new Headers.Builder()
                    .add("X-Riot-Token", API_KEY)
                    .add("Accept-Language", "en-US,en;q=0.8").build();
        }
    }

    public JSONObject getJSONFromUrl(String url) throws IOException, JSONException {

        Request request = new Request.Builder()
                .url(url)
                .headers(requestHeaders)
//                .header("X-Riot-Token", "RGAPI-2a4e8631-b83c-4890-8166-cae97f167476")
//                .header("Accept-Language", "en-US,en;q=0.8")
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
