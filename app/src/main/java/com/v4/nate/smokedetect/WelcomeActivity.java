package com.v4.nate.smokedetect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = "WelcomeActivity";
    public static final int REQUEST_LOGIN = 0;
    @BindView(R.id.btn_registerDevice)
    Button _registerDeviceButton;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.text_account)
    TextView _textAccount;
    private LoginFragment _loginFragment;
    private RegisterFragment _registerFragment;
    private Animation slideLeft;
    private Animation slideUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        slideLeft = AnimationUtils.loadAnimation(this, R.anim.close_translate);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slideup);

        _loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
        _registerFragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.register_fragment);

        getSupportFragmentManager().beginTransaction().hide(_loginFragment).hide(_registerFragment).commit();


        _registerDeviceButton = (Button) findViewById(R.id.btn_registerDevice);
        _loginButton = (Button) findViewById(R.id.btn_login);


        _registerDeviceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), RegisterFragment.class);
                //startActivity(intent);
            }
        });
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.open_translate, R.anim.close_translate)
                        .show(_loginFragment).commit();
                _registerDeviceButton.startAnimation(slideLeft);
                slideLeft.setFillAfter(true);
                //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                //intent.putExtra("id", "1");
                //startActivity(intent);


                // overridePendingTransition(R.anim.open_translate, R.anim.close_translate);
            }
        });
    }


    @Override
    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }


}

