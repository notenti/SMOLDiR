package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.app.ProgressDialog;
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

    //TODO: Determine a URL that we can reliably send the data to on the Pi
    String url = "https://192.168.0.112/queryCode.php";
    SendToDevicesActivity sendToDevicesActivity = new SendToDevicesActivity();

    @BindView(R.id.input_registration_code)
    EditText _registrationCode;
    @BindView(R.id.btn_registerDevice)
    Button _registrationButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstancestate) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);

        _registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        return view;
    }

    public void register() {

        if (!validate()) {
            onRegistrationFailed();
            return;
        }

        Log.d(TAG, "Register");



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
                        onRegistrationSuccess();
                        //onRegistrationFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onRegistrationSuccess() {
        _registrationButton.setEnabled(true);
        getActivity().getFragmentManager().popBackStack();
    }

    public void onRegistrationFailed() {
        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
        _registrationButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String code = _registrationCode.getText().toString();
        params.put("code", code);



        if (code.isEmpty() || code.length() < 5) {
            _registrationCode.setError("incorrect number of characters");
            valid = false;
        } else {
            sendToDevicesActivity.queryServer(getActivity(), url, params, new SendToDevicesActivity.VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) {
                    try {
                        // Registration successful
                        _registrationCode.setError(null);
                        Log.d(TAG, result.toString(1));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return valid;
    }
}
