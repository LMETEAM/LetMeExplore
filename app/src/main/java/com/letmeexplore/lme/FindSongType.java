package com.letmeexplore.lme;

import android.content.Context;
import java.util.ArrayList;
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
    private Context context;
    private List<String> songKeyList;
    private List<Song> songList;
    private int Value;
    private String SongType; //Şarkı Tarzı

    public FindSongType(List<Song> songList) {
        this.songList = songList;
    }

    public FindSongType() {
        // Write a message to the database
    }

    public List<String> getSongKeyList() {
        return songKeyList;
    }

    public void setSongKeyList(List<String> stringList) {
        this.songKeyList = stringList;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void findSongtype(){
        SongType="LME";
        Map< String,Integer > map= new HashMap<String,Integer>();
        List<String> songType=new ArrayList<>(); //Şarkı tarzlarnı içerecek yeni bir liste oluşturduk
        int enbüyük = 0; //En çok tekrar eden tarzın frekansı
        int ikincibüyük=0;//En çok tekrar eden ikinci tarzın frekansı
        for (Song song:songList){ //songList ten şarının tarzını teker teker çekip songType'a attık
            songType.add(song.getSongType());
        }
        //----- Şarkı tarzlarının tekrar edilme frekansını bulup etiketledi map aracılığıyla-----
       // List<String> asList = songType;  !!!!
        for(String s: songType){
            map.put(s,Collections.frequency(songType,s));//Hash map olduğu için birden fazla kez gelen aynı tarz bir defa ekleniyor
        }
        //En çok tekrar eden iki tarzı ve frekansını bulduk
        if (map.size()>1){
            int maxValueInMap=(Collections.max(map.values()));  // This will return max value in the Hashmap
            for (Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                if (entry.getValue()==maxValueInMap) {
                    SongType=entry.getKey();
                    enbüyük=entry.getValue();
                    map.remove(SongType);
                    break;
                }
            }
            int maxSecondValueInMap=(Collections.max(map.values()));
            for (Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                if (entry.getValue()==maxSecondValueInMap) {
                    ikincibüyük=entry.getValue();
                    break;
                }
            }

            enbüyük=((enbüyük-ikincibüyük)/(enbüyük+ikincibüyük))*10;
            if(enbüyük>5){
                Value=5;
            }else if(enbüyük<1){
                Value=1;
            }else Value=enbüyük;
        }
        else {
            int maxValueInMap=(Collections.max(map.values()));  // This will return max value in the Hashmap
            for (Map.Entry<String, Integer> entry : map.entrySet()) {  // Itrate through hashmap
                if (entry.getValue()==maxValueInMap) {
                    SongType=entry.getKey();
                    break;
                }
            }
            if (songList.size()<5){ //Şarkı listesi 5ten küçükse
            Value=3; //Puan 3
            }
            else if(songList.size()<10){

                Value=4;
            }
            else if(songList.size()>10){
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
