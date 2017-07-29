package com.v4.nate.smokedetect;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;

public class WelcomeFragment extends Fragment {

    public static final String TAG = "WelcomeFragment";
    @BindView(R.id.btn_registerDevice)
    Button _registerDeviceButton;
    @BindView(R.id.btn_login)
    Button _loginButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        _registerDeviceButton = view.findViewById(R.id.btn_registerDevice);
        _loginButton = view.findViewById(R.id.btn_login);

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
