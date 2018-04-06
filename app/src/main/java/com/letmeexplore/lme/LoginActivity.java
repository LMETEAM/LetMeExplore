package com.letmeexplore.lme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {
    ImageView ımageView;
    ImageView facebookLog;
    ImageView googleLog;
    ImageView signUp;
    ImageView logIn;
    EditText userName;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ımageView=(ImageView)findViewById(R.id.imageView);
        facebookLog=(ImageView)findViewById(R.id.facebookAccount);
        googleLog=(ImageView)findViewById(R.id.googleAccount);
        signUp=(ImageView)findViewById(R.id.signUp);
        logIn=(ImageView)findViewById(R.id.loginButton);
        userName=(EditText)findViewById(R.id.userName);
        password=(EditText)findViewById(R.id.password);

    }
    void login(View view){
        Intent ıntent=new Intent(this,MainActivity.class);
        startActivity(ıntent);
        finish();
    }
}
