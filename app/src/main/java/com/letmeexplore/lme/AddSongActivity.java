package com.letmeexplore.lme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddSongActivity extends AppCompatActivity {
    private EditText songName;
    private EditText singer;
    private EditText songType;
    private EditText year;
    private EditText length;
    private Button button;

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songName.getText().toString().isEmpty()||singer.getText().toString().isEmpty()||songType.getText().toString().isEmpty()||
                        year.getText().toString().isEmpty()||length.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Boş Alanları Doldurunuz",Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseControl databaseControl=new DatabaseControl("Songs");
                    Song song=new Song(songName.getText().toString(),songType.getText().toString(),singer.getText().toString(),
                            year.getText().toString(),length.getText().toString(),null);
                    databaseControl.sendSong(song);
            }
        }

    });
    }

}
