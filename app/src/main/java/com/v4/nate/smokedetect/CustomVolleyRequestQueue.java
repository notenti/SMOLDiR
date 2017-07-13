package com.v4.nate.smokedetect;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;


public class CustomVolleyRequestQueue extends Application {

    public static final String TAG = "VolleyPatterns";
    private static CustomVolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized CustomVolleyRequestQueue getInstance() {

        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 *1024*1024);
            //Network network = new BasicNetwork(new HurlStack());
            //mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            // Don't forget to start the volley request queue
            //mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        //set default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        //set the default tag if the tag is empty
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
