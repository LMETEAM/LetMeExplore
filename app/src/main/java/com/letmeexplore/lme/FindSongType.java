package com.letmeexplore.lme;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 Kullanıcı çalma listesini oluşturup veri tabanına gönderdikten sonra kullanıcının veri tabanında
 oluşturduğu çalma listesinde müziklerin sayısı ve şarkı id’lerinden yola çıkılacak. Müziklerin
 songType bilgileri alınacak. Oluşturulan sınıfa gönderildikten sonra sınıf, algoritmayla
 en çok bulunan müzik tarzını ve ve bu tarzın puanını (1 ile 5 arası) döndürecek.
 Bu sayede kullanıcıyı veri tabanındaki aynı tür dinleyen diğer kullanıcılarında bulunmuş olduğu kategoriye yerleştireceğiz.
 */

public class FindSongType {
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private Context context;
    public List<String> stringList;
    public List<Song> songList;
    private DatabaseControl databaseControl;
    private Map< String,Integer > map;
    private int Value;  //Şarkı Tarzının Puanı
    private String SongType; //Şarkı Tarzı

    public FindSongType(List<Song> list) {
        // Write a message to the database
        this.context=context;
        stringList = new ArrayList<String>();
        songList=new ArrayList<Song>();
        songList=list;
        map= new HashMap<String,Integer>();
        findSongtype();
    }
    void findSongtype(){
        List<String> songType=new ArrayList<String>(); //Şarkı tarzlarnı içerecek yeni bir liste oluşturduk
        int enbüyük; //En çok tekrar eden tarzın frekansı
        int ikincibüyük;//En çok tekrar eden ikinci tarzın frekansı
        for (int i=0;i<songList.size();i++){ //songList ten şarının tarzını teker teker çekip songType'a attık
            songType.add(songList.get(i).getSongType());
        }
        //----- Şarkı tarzlarının tekrar edilme frekansını bulup etiketledi map aracılığıyla-----
        List<String> asList = songType;
        for(String s: songType){
            map.put(s,Collections.frequency(asList,s));//Hash map olduğu için birden fazla kez gelen aynı tarz bir defa ekleniyor
        }

        //En çok tekrar eden iki tarzı ve frekansını bulduk
        Map.Entry<String,Integer> maxEntry = null;
        if (map.size()>1){
            for(Map.Entry<String,Integer> entry : map.entrySet()) {
                if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
                }
            }
            SongType=maxEntry.getKey();
            enbüyük=maxEntry.getValue();
            maxEntry=null;
            map.remove(SongType); //İkinci en çok tekrar eden tarzı bulmak için birincisini remove ettik map tan
            for(Map.Entry<String,Integer> entry : map.entrySet()) {
                if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }
            ikincibüyük=maxEntry.getValue();
            enbüyük=((enbüyük-ikincibüyük)/(enbüyük+ikincibüyük))*10;
            if(enbüyük>5){
                Value=5;
            }else if(enbüyük<1){
                Value=1;
            }else Value=enbüyük;
        }
        else {
            for(Map.Entry<String,Integer> entry : map.entrySet()) {
                if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                    maxEntry = entry;
                }
            }
            if (songList.size()<5){ //Şarkı listesi 5ten küçükse
            SongType=maxEntry.getKey();
            Value=3; //Puan 3
            }
            else if(songList.size()<10){
                SongType=maxEntry.getKey();
                Value=4;
            }
            else if(songList.size()>10){
                SongType=maxEntry.getKey();
                Value=5;
            }

        }
    }
    String getSongType(){
        return SongType;
    }
    int getValue(){
        return Value;
    }



}
