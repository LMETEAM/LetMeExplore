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
    private Button button;
    private String[] songTypeNames,yearNames;
    private CircleImageView circleImageView;
    public static final int RC_SELECT_IMAGE=1;
    private Uri urichoosenImage;
    static String Message;

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
        Message="";

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
                        DatabaseControl databaseControl=new DatabaseControl("Songs");
                        Song song=new Song(songName.getText().toString(),songType.getText().toString(),singer.getText().toString(),
                                year.getText().toString(),length.getText().toString(),null,null);
                            databaseControl.sendSong(song,urichoosenImage); //Geri Dönüş Alamıyorum Toast için
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
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Picasso.with(circleImageView.getContext()).load(urichoosenImage).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.circleImageView));

        }}
}



