package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    HashMap<String, String> params = new HashMap<>();
    boolean valid = false;

    SharedPreferences sharedPreferences;


    //TODO: Determine a URL that we can reliably send the data to on the Pi
    String url = "http://192.168.0.105/queryCode.php";
    SendToDevicesActivity send = new SendToDevicesActivity();

    @BindView(R.id.input_registration_code)
    EditText _registrationCode;
    @BindView(R.id.btn_registerDevice)
    Button _registrationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences("test", Context.MODE_PRIVATE);

        _registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        return view;
    }

    public void register() {

        Log.d(TAG, "Register");

        String code = _registrationCode.getText().toString();
        params.put("code", code);
        if (code.isEmpty() || code.length() < 6) {
            _registrationCode.setError("incorrect number of characters");
        } else {
            send.queryServer(getActivity(), url, params, new SendToDevicesActivity.VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) { //Response was successful
                    try {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        valid = result.getBoolean("return");
                        Log.d(TAG, result.getString("homeID"));
                        Log.d(TAG, result.getString("deviceID"));
                        editor.putString("HomeID", result.getString("homeID"));
                        editor.apply();
                        _registrationCode.setError(null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        valid = false;
                    }
                }
            });
        }

        _registrationButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Welcome_Dialog);
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

    public void onRegistrationSuccess() {
        _registrationButton.setEnabled(true);
        Toast.makeText(getActivity(), "Registration success", Toast.LENGTH_SHORT).show();
        //getActivity().getFragmentManager().popBackStack();
    }

    public void onRegistrationFailed() {
        _registrationButton.setEnabled(true);
        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
    }
}
