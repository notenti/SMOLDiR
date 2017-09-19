package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiFragment extends Fragment {

    private static final String TAG = "WifiFragment";
    HashMap<String, String> params = new HashMap<>();
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    boolean valid = false;

    String url = "http://192.168.0.105/handleWifi.php";
    SendToDevicesActivity send = new SendToDevicesActivity();

    @BindView(R.id.input_password)
    EditText _password;
    @BindView(R.id.btn_push_wifi_credentials)
    Button _pushWifiCredentials;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View view = inflater.inflate(R.layout.fragment_wifi, container, false);
        ButterKnife.bind(this, view);
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        TextView textView = view.findViewById(R.id.ssid);
        textView.setText(wifiInfo.getSSID());
        _pushWifiCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushWifiCredentials();
            }
        });
        return view;
    }


    public void pushWifiCredentials() {
        Log.d(TAG, "Wifi Credentials");


        String password = _password.getText().toString();
        params.put("ssid", wifiInfo.getSSID());
        params.put("password", password);

        if (password.isEmpty()) {
            _password.setError("empty string");
        } else {
            send.queryServer(getActivity(), url, params, new SendToDevicesActivity.VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) {
                    try {
                        valid = result.getBoolean("return");
                        Log.d(TAG, result.getString("ssid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        valid = false;
                    }
                }
            });
        }

    }

}
