package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

    public static final String TAG = "LoginFragment";
    public static final int REQUEST_SIGNUP = 0;


    @BindView(R.id.input_email)
    EditText _emailText;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);




        return view;
    }

}
