package com.v4.nate.smokedetect;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SendToDevicesActivity {


    public void sendHush(Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.1.239/test.php";

        //POST params to be sent to the server
        HashMap<String, String> params = new HashMap<>();
        params.put("detectorID", "1");
        params.put("hush", "1");

        JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Response:%n %s", response.toString(1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

}
