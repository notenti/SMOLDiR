package com.v4.nate.smokedetect;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

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

    public static final String TAG = "LoginFragment";
    public static final int REQUEST_SIGNUP = 0;
    final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
            R.style.AppTheme_Dark_Dialog);
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
//    @BindView(R.id.btn_login_fragment)
//    Button _loginButton;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    public void login(Boolean result) {
        Log.d(TAG, "Login");

        Utility.setUserLoggedIn(getActivity(), result);
        if (result) {
            EditText _emailText = (EditText) getActivity().findViewById(R.id.input_email);
            EditText _passwordText = (EditText) getActivity().findViewById(R.id.input_password);
            String username = _emailText.getText().toString();
            String password = _passwordText.getText().toString();
            Utility.saveUsernameAndPassword(getActivity(), username, password);

            getActivity().finish();
        } else {
            Utility.saveUsernameAndPassword(getActivity(), null, null);
            TextView error = (TextView) getActivity().findViewById(R.id.error);
            error.setText("Login failed! Please try again.");
        }
    }

    public void tryLogin(View view) {
        EditText _emailText = (EditText) getActivity().findViewById(R.id.input_email);
        EditText _passwordText = (EditText) getActivity().findViewById(R.id.input_password);
        String username = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            TextView error = (TextView) getActivity().findViewById(R.id.error);
            error.setText("");
            CheckLoginTask loginTask = new CheckLoginTask();
            loginTask.execute(username, password);
        }
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
            login(result);
        }
    }

}
