package com.letmeexplore.lme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {
    ImageView ımageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ımageView=(ImageView)findViewById(R.id.imageView);
    }
}
