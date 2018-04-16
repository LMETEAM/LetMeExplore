package com.letmeexplore.lme;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.letmeexplore.lme.AddSongActivity.RC_SELECT_IMAGE;

public class signupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signupButton;
    private EditText emailEdit;
    private EditText passwordEdit;
    private ImageView picture;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        signupButton = (Button) findViewById(R.id.signupButton);
        emailEdit = (EditText) findViewById(R.id.signupMail);
        passwordEdit = (EditText) findViewById(R.id.signupPassword);
        picture = findViewById(R.id.userPP);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        user = new User();
        signupButton.setOnClickListener(this);

    }
    //--Kullanıcı resmi için alım başladı--
    void setImageView(){
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseImage=new Intent(Intent.ACTION_GET_CONTENT);
                browseImage.setType("image/*");
                startActivityForResult(Intent.createChooser(browseImage,"Select Picture"),RC_SELECT_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SELECT_IMAGE&&resultCode==RESULT_OK){
            Uri choosenOne = data.getData();
            Picasso.with(picture.getContext()).load(choosenOne).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.userPP));

        }
    }
    //--Kullanıcı resmi alındı ve imageView'ın içine yerleştirildi--!

    private void registerUser(){
        String email = emailEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(this,"Please enter an email.",Toast.LENGTH_SHORT).show();
            //to stop the function execution further
            return;
        }
        if(TextUtils.isEmpty(password)){
        // email is empty
            Toast.makeText(this,"Please enter a password.",Toast.LENGTH_SHORT).show();
            //to stop the function execution further
            return;
        }

        //if email and password are typed in
        progressDialog.setMessage("Registering the user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            


                            Toast.makeText(signupActivity.this,"Registered Succesfully.",Toast.LENGTH_SHORT).show();
                            Intent getBackToMain = new Intent(signupActivity.this,MainActivity.class);
                            startActivity(getBackToMain);
                            progressDialog.dismiss();
                            finish();
                        }
                        else{
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(signupActivity.this,"This email has been registered before.",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(signupActivity.this,"Registration has failed.",Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();

                        }
                    }
                });

    }

    @Override
    public void onClick(View view){
        if(view == signupButton) {
            registerUser();
        }
        else if(view == picture){
            setImageView();
        }
    }
}
