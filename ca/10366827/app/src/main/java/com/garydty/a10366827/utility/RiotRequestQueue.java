package com.garydty.a10366827.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ResponseDelivery;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Gary Doherty on 25/09/2017.
 */

public class RiotRequestQueue  {
    public static String ENDPOINT = "https://euw1.api.riotgames.com";
    private static RiotRequestQueue mInstance;// Instantiate the cache
    private static Context mCtx;
    private ImageLoader mImageLoader;

    private RequestQueue mRequestQueue;
//    private Cache mCache;

    private RiotRequestQueue(Context ctx){
        mRequestQueue = Volley.newRequestQueue(ctx);
//        mCache = new DiskBasedCache(ctx.getCacheDir(), 1024 * 1024);

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

    public static synchronized RiotRequestQueue getInstance(Context ctx){
        if(mInstance == null){
            mInstance = new RiotRequestQueue(ctx);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
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



}
