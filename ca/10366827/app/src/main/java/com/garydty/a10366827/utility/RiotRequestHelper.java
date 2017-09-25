package com.garydty.a10366827.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.garydty.a10366827.BuildConfig;
import com.garydty.a10366827.R;
import com.garydty.a10366827.models.Summoner;

import java.util.HashMap;

/**
 * Created by Gary Doherty on 25/09/2017.
 */

public class RiotRequestHelper {
    private static String ENDPOINT = "https://euw1.api.riotgames.com";
    private static String IMAGE_ENDPOINT = "http://avatar.leagueoflegends.com/euw/";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static RiotRequestHelper mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private HashMap<String, String> mHeaders;
//    private Cache mCache;

    private RiotRequestHelper(Context ctx){
        mCtx = ctx;

        mRequestQueue = getRequestQueue();

//        mCache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024);

        mHeaders = new HashMap<>();
                    mHeaders.put("X-Riot-Token", API_KEY);
                    mHeaders.put("Accept-Language", "en-US,en;q=0.8");

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized RiotRequestHelper getInstance(Context ctx){
        if(mInstance == null){
            mInstance = new RiotRequestHelper(ctx);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static String createUrl(String path, String name){
        return ENDPOINT + path + name;
    }

    public <T> void addGsonRequestToQueue(String url, Class<T> clazz, Response.Listener<T> onPostsLoaded,
                                          Response.ErrorListener onPostsError){
        addToRequestQueue(
            new GsonRequest<>(
                url,
                clazz,
                mHeaders,
                onPostsLoaded,
                onPostsError)
        );
    }

    public void addGetSummonerIconRequestToQueue(String summonerName, Response.Listener<Bitmap> onResult,
                                              Response.ErrorListener onError){

        ImageRequest summonerIconRequest = new ImageRequest(
                IMAGE_ENDPOINT + summonerName.concat(".png"),
                onResult,
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                onError);

        addToRequestQueue(summonerIconRequest);
    }

    public void getRiotRSSFeed(Response.Listener<String> onResult, Response.ErrorListener onError){
        addToRequestQueue(
                new StringRequest(
                    Request.Method.GET, "http://euw.leagueoflegends.com/en/rss.xml",
                    onResult,
                    onError
                )
        );
    }
}
