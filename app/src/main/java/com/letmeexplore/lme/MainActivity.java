package com.letmeexplore.lme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private ImageView ımageView;
    private ImageView facebookLog;
    private ImageView googleLog;
    private ImageView signUp;
    private ImageView logIn;
    private EditText userName;
    private EditText password;
    private android.support.v7.app.ActionBar actionBar;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN=1;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ımageView=(ImageView)findViewById(R.id.imageView);
        facebookLog=(ImageView)findViewById(R.id.facebookAccount);
        googleLog=(ImageView)findViewById(R.id.googleAccount);
        signUp=(ImageView)findViewById(R.id.signUp);
        logIn=(ImageView)findViewById(R.id.loginButton);
        userName=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        mAuth=FirebaseAuth.getInstance();
        mProgressDialog=new ProgressDialog(this);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Login işlemi
                signInWithEmail();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // SignUp ekranına gidiş
                signUp();
            }
        });
        //*************GOOGLE SING IN**************
        //Configure Google Sıgn In
        googleLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignMethod();
                signInWithGoogle();
            }
        });
    }

    private void signInWithEmail() {
        String email = userName.getText().toString().trim();
        String passw = password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            // email is empty
            Toast.makeText(this,"Please enter an email.",Toast.LENGTH_SHORT).show();
            //to stop the function execution further
            return;
        }
        if(TextUtils.isEmpty(passw)){
            // email is empty
            Toast.makeText(this,"Please enter a password.",Toast.LENGTH_SHORT).show();
            //to stop the function execution further
            return;
        }
        mAuth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent goHomeActivity=new Intent(MainActivity.this,HomeActivity.class);
                    Toast.makeText(getApplicationContext(),"Sign in Success",Toast.LENGTH_SHORT).show();
                    startActivity(goHomeActivity);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp(){
        Intent signUpIntent = new Intent(this,signupActivity.class);
        startActivity(signUpIntent);
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                         // Google Sign In was successful, authenticate with Firebase
                mProgressDialog.setMessage("Starting Sign in...");
                mProgressDialog.show();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                mProgressDialog.dismiss();
                // ...
            }
        }}
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.show();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //-------DATABASE Kullanıcının Bilgilerini Gönderme---------
                            String userName=mAuth.getCurrentUser().getDisplayName();
                            String userUid=mAuth.getCurrentUser().getUid();
                            String photoUrl=mAuth.getCurrentUser().getPhotoUrl().toString();
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference myref =database.getReference("Users/"+userUid+"/properties");
                            myref.setValue(new User(userName,photoUrl,userUid));
                            // -------Home Activity Geçiş ------
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    void GoogleSignMethod(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    Toast.makeText(MainActivity.this, "Bir Hata Var", Toast.LENGTH_SHORT).show();
                }
            }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }
    }

    String NameFind(String displayName){
        int index=displayName.indexOf(" ");
        String name=displayName.substring(0,index);
        return name;
    }
    String SurnameFind(String displayName){
        int index=displayName.indexOf(" ");
        String surname=displayName.substring(index+1,displayName.length());
        return surname;
    }
}