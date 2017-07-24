package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SignupFragment signupFragment = new SignupFragment();
                ((WelcomeActivity) getActivity()).setNewFragment(signupFragment);

            }
        });
        return view;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //TODO: Implement own authentication logic here

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        //On complete call either onLogicSuccess or onLoginFailed
                        onLoginSuccessful();
                        //onLoginFailed();
                        progressDialog.dismiss();

                    }
                }, 3000);

    }

    public void onLoginSuccessful() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(getActivity(), LandingActivity.class);
        startActivity(intent);
        getActivity().getFragmentManager().popBackStack();
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty()) {//|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    public class CheckLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String responseJsonStr = null;
            try {
                final String GET_PROFILE_BASE_STRING = "https://api.domain.com/user?";
                Uri builtUri = Uri.parse(GET_PROFILE_BASE_STRING).buildUpon().build();
                URL url = new URL(builtUri.toString());
                //Create the request to server and open the connection
                urlConnection = (HttpsURLConnection) url.openConnection();
                //Create the SSL connection
                SSLContext sc;
                sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new SecureRandom());
                urlConnection.setSSLSocketFactory(sc.getSocketFactory());
                //Add API credentials
                String user = params[0];
                String password = params[1];
                String userpass = user + ":" + password;
                //Create the Authentication Token
                String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
                //Add the required headers
                urlConnection.addRequestProperty("Authorization", basicAuth);
                urlConnection.addRequestProperty("Content-Type", "application/json");
                urlConnection.addRequestProperty("accept", "application/json");
                //Method
                urlConnection.setRequestMethod("GET");
                //connect
                urlConnection.connect();

                int status = urlConnection.getResponseCode();
                String reason = urlConnection.getResponseMessage();

                Log.v("LOGIN", status + reason);

                //Read the input stream into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                responseJsonStr = buffer.toString();
                // getNameDataFromJson(responseJsonStr);


            } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                Log.e("LOGIN", "Error", e);
                return false;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            login();
        }
    }

}
