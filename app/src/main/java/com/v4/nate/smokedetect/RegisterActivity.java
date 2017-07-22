package com.v4.nate.smokedetect;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final int REQUEST_REGISTER = 0;

    @BindView(R.id.input_registration_code)
    EditText _registrationCode;
    @BindView(R.id.btn_registerDevice)
    Button _registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        _registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register() {
        Log.d(TAG, "Register");

        if (!validate()) {
            onRegistrationFailed();
            return;
        }

        _registrationButton.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering Device...");
        progressDialog.show();

        String code = _registrationCode.getText().toString();

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

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.center_to_left, R.anim.right_to_center);
    }

    public void onRegistrationSuccess() {
        _registrationButton.setEnabled(true);
        finish();
    }

    public void onRegistrationFailed() {
        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        _registrationButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String code = _registrationCode.getText().toString();

        if (code.isEmpty() || code.length() < 10) {
            _registrationCode.setError("incorrect number of characters");
            valid = false;
        } else {
            _registrationCode.setError(null);
        }

        return valid;
    }
}
