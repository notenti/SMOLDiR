package com.v4.nate.smokedetect;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class SendToDevicesActivity {

    public void queryServer(Context context, String url, HashMap<String, String> params, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccessResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(req);
    }

    public void sendToServer(Context context, String URL, HashMap<String, String> params) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(req);
    }

    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result);
    }

}
