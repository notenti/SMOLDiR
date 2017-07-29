package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;

public class WelcomeFragment extends Fragment {

    public static final String TAG = "WelcomeFragment";
    @BindView(R.id.btn_registerDevice)
    Button _registerDeviceButton;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.text_signup)
    TextView _signupText;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        _registerDeviceButton = view.findViewById(R.id.btn_registerDevice);
        _loginButton = view.findViewById(R.id.btn_login);
        _signupText = view.findViewById(R.id.text_signup);

        _signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupFragment signupFragment = new SignupFragment();
                ((WelcomeActivity) getActivity()).setNewFragment(signupFragment);
            }
        });

        _registerDeviceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                ((WelcomeActivity) getActivity()).setNewFragment(registerFragment);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                ((WelcomeActivity) getActivity()).setNewFragment(loginFragment);

            }
        });

        return view;

    }
}
