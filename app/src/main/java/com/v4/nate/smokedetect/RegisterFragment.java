package com.v4.nate.smokedetect;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    HashMap<String, String> params = new HashMap<>();
    List<String> deviceList = new ArrayList<>();
    String homeID = "1376hh";
    String code;
    boolean valid = false;
    boolean alreadyRegistered = false;

    String url = "http://192.168.0.117/register.php";
    SendToDevicesActivity send = new SendToDevicesActivity();

    @BindView(R.id.input_registration_code)
    EditText _registrationCode;
    @BindView(R.id.btn_registerDevice)
    Button _registrationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        _registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        return view;
    }

    public void register() {
        deviceList.add("131313");
        deviceList.add("121212");
        setList("DeviceID", deviceList);


        Log.d(TAG, "Register");

        //Get deviceID from text input
        code = _registrationCode.getText().toString();
        params.put("code", code);


        if (code.isEmpty() || code.length() < 6) {
            _registrationCode.setError("incorrect number of characters");
        } else { //Code was entered properly
            if (checkExistingDevice()) { //If the device has already been registered, notify the user
                Toast.makeText(getActivity(), "That device has already been registered", Toast.LENGTH_SHORT).show();
            } else { //If the device has not been registered, do all the normal stuff
                send.queryServer(getActivity(), url, params, new SendToDevicesActivity.VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) { //Response was successful
                        try {
                            valid = result.getBoolean("return");
                            if (valid) {
                                deviceList.add(result.getString("deviceID").trim().replace("\n", ""));
                                setList("DeviceID", deviceList);
                                set("HomeID", result.getString("homeID").trim().replace("\n", ""));
                                FirebaseMessaging.getInstance().subscribeToTopic(result.getString("homeID").trim());
                                Toast.makeText(getActivity(), "Subscribed to topic " + result.getString("homeID").trim(), Toast.LENGTH_SHORT).show();
                                _registrationCode.setError(null);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            valid = false;
                        }
                    }
                });
                progress();
            }
        }

    }

    public void onRegistrationSuccess() {
        _registrationButton.setEnabled(true);
        Toast.makeText(getActivity(), "Registration success", Toast.LENGTH_SHORT).show();
        WifiFragment wifiFragment = new WifiFragment();
        ((WelcomeActivity) getActivity()).setNewFragment(wifiFragment);
    }

    public void onRegistrationFailed() {
        _registrationButton.setEnabled(true);
        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        System.out.println(json);
        set(key, json);
    }

    public boolean checkExistingDevice() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alreadyRegistered = false;
                if (dataSnapshot.hasChild(homeID)) {
                    if (dataSnapshot.child(homeID).hasChild(code)) {
                        System.out.println(code);
                        alreadyRegistered = true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return alreadyRegistered;
    }

    public void set(String key, String value) {
        editor.putString(key, value);
        editor.apply();
        sharedPreferences = getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
//        String homeID = sharedPreferences.getString("HomeID", null);
        String deviceID = sharedPreferences.getString("DeviceID", null);
        System.out.println(deviceID);

    }

    public void progress() {
        _registrationButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.AppTheme_Welcome_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        //TODO: Implement some sort of verification thing to add this new node to the network

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (valid)
                            onRegistrationSuccess();
                        else
                            onRegistrationFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

}
