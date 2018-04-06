package com.letmeexplore.lme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {
    ImageView ımageView;
    ImageView facebookLog;
    ImageView GoogleLog;
    ImageView SıgnUp;
    ImageView LogIn;
    EditText userName;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ımageView=(ImageView)findViewById(R.id.imageView);
    }
}
