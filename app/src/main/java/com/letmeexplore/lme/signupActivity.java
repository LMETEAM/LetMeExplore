package com.letmeexplore.lme;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.letmeexplore.lme.AddSongActivity.RC_SELECT_IMAGE;

/**
 * Created by İlker on 16.04.2018.
 */

public class signupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signupButton;
    private EditText emailEdit;
    private EditText passwordEdit,nameEdit;
    private ImageView picture;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private StorageReference storageReference;
    private DatabaseReference myRef;
    private User user;
    private Uri choosenImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        signupButton = (Button) findViewById(R.id.signupButton);
        emailEdit = (EditText) findViewById(R.id.signUpEmail);
        nameEdit = (EditText) findViewById(R.id.signUpName);
        passwordEdit = (EditText) findViewById(R.id.signupPassword);
        picture = findViewById(R.id.userPP);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        user = new User();
        choosenImage = null;

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
            choosenImage = data.getData();
            Picasso.get().load(choosenImage).noPlaceholder().centerCrop().fit()
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
                            if (choosenImage == null) {
                                //-------DATABASE Kullanıcının Bilgilerini Gönderme---------
                                progressDialog.dismiss();
                                String key = firebaseAuth.getCurrentUser().getUid();
                                user.setUid(key);
                                user.setDisplayName(nameEdit.getText().toString());
                                user.setPhotoUrl("https://firebasestorage.googleapis.com/v0/b/letmeexplore-fb83f.appspot.com/o/Users%2FPictures%2Fstduserpic.jpg?alt=media&token=e428373b-0367-4b07-bc45-3a01494e03d9");
                                myRef.child(firebaseAuth.getCurrentUser().getUid()).child("properties").setValue(user);
                                Toast.makeText(signupActivity.this,"Registered Succesfully.",Toast.LENGTH_SHORT).show();
                            }else {
                                progressDialog.dismiss();
                                String key = firebaseAuth.getCurrentUser().getUid();
                                user.setUid(key);
                                user.setDisplayName(nameEdit.getText().toString());
                                storageReference = FirebaseStorage.getInstance().getReference("Users/Pictures/" + key);
                                storageReference.putFile(choosenImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        user.setPhotoUrl(taskSnapshot.getDownloadUrl().toString());
                                        Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                                        myRef.child(firebaseAuth.getCurrentUser().getUid()).child("properties").setValue(user);

                                    }
                                });
                                
                                Toast.makeText(signupActivity.this,"Registered Succesfully.",Toast.LENGTH_SHORT).show();
                            }

                            Intent getBackToMain = new Intent(signupActivity.this,MainActivity.class);
                            startActivity(getBackToMain);
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
