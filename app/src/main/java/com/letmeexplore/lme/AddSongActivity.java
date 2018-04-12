package com.letmeexplore.lme;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddSongActivity extends AppCompatActivity {
    private EditText songName;
    private EditText singer;
    private EditText songType;
    private EditText year;
    private EditText length;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private StorageReference storageReference;
    private Button button;
    private String[] songTypeNames,yearNames;
    private CircleImageView circleImageView;
    public static final int RC_SELECT_IMAGE=1;
    private Uri urichoosenImage;
    public String Message;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        songName=(EditText)findViewById(R.id.upload_songName);
        singer=(EditText)findViewById(R.id.upload_singer);
        songType=(EditText)findViewById(R.id.upload_songType);
        year=(EditText)findViewById(R.id.upload_year);
        length=(EditText)findViewById(R.id.upload_length);
        button=(Button)findViewById(R.id.button2);
        urichoosenImage=null;
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("Songs");
        storageReference=FirebaseStorage.getInstance().getReference("Songs/Photos");
        circleImageView=(CircleImageView)findViewById(R.id.circleImageView);
        songTypeNames= new String[]{"Classical", "Electronic", "Pop", "Hip hop", "Rock", "Indie", "Jazz", "Punk","Metal","Dance","Funk","Modern Classical"};
        yearNames=new String[]{"2018","2017","2016","2015","2014","2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003",
        "2002","2001","2000","1999","1998","1997","1996","1995","1994","1993","1992","1991","1990"};
        yearListener();
        songTypeListener();
        buttonListener();
        setCircleImageView();

    }
        void buttonListener(){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(songName.getText().toString().isEmpty()||singer.getText().toString().isEmpty()||songType.getText().toString().isEmpty()||
                            year.getText().toString().isEmpty()||length.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(),"Boş Alanları Doldurunuz!",Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            final Song song=new Song(songName.getText().toString(),songType.getText().toString(),singer.getText().toString(),
                                    year.getText().toString(),length.getText().toString(),null,null);
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    int count=0;
                                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                                        count++;
                                        Song song1=ds.child("properties").getValue(Song.class);
                                        if((song.getSongName().toUpperCase().equalsIgnoreCase(song1.getSongName().toUpperCase()))&&song.getSongType().toUpperCase()
                                                .equalsIgnoreCase(song1.getSongType().toUpperCase())){
                                            Toast.makeText(getApplicationContext(),"This song is already exists",Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        // Yukarıda bizim girdiğimiz şeyin database'de olup olmadığı kontrol edildi, kod halen devam ediyorsa bir eşleşme
                                        // bulunamamış demektir.
                                        if(count==dataSnapshot.getChildrenCount()) {
                                            if (urichoosenImage == null) {
                                                String key = myRef.push().getKey();
                                                song.setSongkey(key);
                                                song.setSongPhotoUrl("https://firebasestorage.googleapis.com/v0/b/letmeexplore-fb83f.appspot.com/o/Songs%2FPhotos%2Fdefaultimage.png?alt=media&token=b97f0aa5-7ee2-4536-a024-6222f69573ee");
                                                myRef.push().child("properties").setValue(song);
                                                Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                                                Picasso.with(circleImageView.getContext()).load(R.drawable.daddylesssons).noPlaceholder().centerCrop().fit()
                                                        .into((ImageView) findViewById(R.id.circleImageView));
                                                break;
                                            } else {

                                                String key = myRef.push().getKey();
                                                song.setSongkey(key);
                                                storageReference = FirebaseStorage.getInstance().getReference("Songs/Photos/" + key);
                                                storageReference.putFile(urichoosenImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        song.setSongPhotoUrl(taskSnapshot.getDownloadUrl().toString());
                                                        myRef.push().child("properties").setValue(song);
                                                        Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                                                        Picasso.with(circleImageView.getContext()).load(R.drawable.daddylesssons).noPlaceholder().centerCrop().fit()
                                                                .into((ImageView) findViewById(R.id.circleImageView));
                                                    }
                                                });
                                                break;
                                            }
                                        }
                                     }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            songName.setText("");
                            songType.setText("");
                            singer.setText("");
                            year.setText("");
                            length.setText("");
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            });
        }
        void songTypeListener(){
            songType.setFocusable(false);
            songType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder= new AlertDialog.Builder(AddSongActivity.this);
                    builder.setTitle("Choose SongType");
                    builder.setItems(songTypeNames, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            songType.setText(songTypeNames[which]);
                        }
                    });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
            });
        }
        void yearListener(){
        year.setFocusable(false);

        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(AddSongActivity.this);
                builder.setTitle("Choose Year");
                builder.setItems(yearNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        year.setText(yearNames[which]);
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
    }
        void setCircleImageView(){
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chooseımage=new Intent(Intent.ACTION_GET_CONTENT);
                    chooseımage.setType("image/*");
                    startActivityForResult(Intent.createChooser(chooseımage,"Select Picture"),RC_SELECT_IMAGE);
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SELECT_IMAGE){
            urichoosenImage=data.getData();
            Picasso.with(circleImageView.getContext()).load(urichoosenImage).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.circleImageView));

        }}
}



