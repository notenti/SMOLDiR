package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;

public class WelcomeFragment extends Fragment {
    public static final String TAG = "WelcomeFragment";
    public static final int RC_SIGN_IN = 9001;
    @BindView(R.id.btn_registerDevice)
    Button _registerDeviceButton;
    @BindView(R.id.sign_in_button)
    Button _googleSignup;
    Boolean login = false;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        login = prefs.getBoolean("login", false);


        _registerDeviceButton = view.findViewById(R.id.btn_registerDevice);
        _googleSignup = view.findViewById(R.id.sign_in_button);

        _registerDeviceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                ((WelcomeActivity) getActivity()).setNewFragment(registerFragment);
            }
        });

        _googleSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                //.requestIdToken(getString(R.string.server_client_id))
                .build();
        //mAuth = FirebaseAuth.getInstance();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage((WelcomeActivity) getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return view;

    }

    private void sign() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.stopAutoManage((WelcomeActivity) getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStart() {
        super.onStart();

//        if (!login) {
//            OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//            if (optionalPendingResult.isDone()) {
//                GoogleSignInResult result = optionalPendingResult.get();
//                handleSignInResult(result);
//                Log.e("CACHE STATUS", "Got cached sign-in");
//            } else {
//                showProgressDialog();
//                optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                    @Override
//                    public void onResult(GoogleSignInResult googleSignInResult) {
//                        hideProgressDialog();
//                        handleSignInResult(googleSignInResult);
//                    }
//                });
//            }
//        }

        // FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Toast.makeText(getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            //firebaseAuthWithGoogle(account);
//            String idToken = account.getIdToken();
//            Toast.makeText(getActivity(), idToken, Toast.LENGTH_SHORT).show();
//
//            //Logging for debugging, can remove later
//            String name = account.getDisplayName();
//            Log.e("DISPLAY NAME", name);
//            String email = account.getEmail();
//            Log.e("USER EMAIL", email);
//            String profile = String.valueOf(account.getPhotoUrl());
//            Log.e("USER PROFILE", profile);
//            Log.e("ID", account.getId());


            Intent intent = new Intent(getActivity(), LandingActivity.class);
            startActivity(intent);

        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }


}
