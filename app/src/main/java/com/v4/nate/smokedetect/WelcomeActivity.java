package com.v4.nate.smokedetect;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import butterknife.BindView;

public class WelcomeActivity extends AppCompatActivity {

    public static final String TAG = "WelcomeActivity";

    @BindView(R.id.btn_registerDevice)
    Button _registerDeviceButton;
    @BindView(R.id.btn_login)
    Button _loginButton;
//    private RegisterFragment _registerFragment;
//    private Animation slideLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        _registerDeviceButton = findViewById(R.id.btn_registerDevice);
//        _registerFragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.register_fragment);
        _loginButton = findViewById(R.id.btn_login);

//        getSupportFragmentManager().beginTransaction().hide(_registerFragment).commit();


        _registerDeviceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_center, R.anim.center_to_right);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_center, R.anim.center_to_left);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //disable going back to the MainActivity
        moveTaskToBack(true);
    }


}

