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
        for (Song song:songList){ //songList ten şarının tarzını teker teker çekip songType'a attık
            songType.add(song.getSongType());
        }
        //----- Şarkı tarzlarının tekrar edilme frekansını bulup etiketledi map aracılığıyla-----
       // List<String> asList = songType;  !!!!
        for(String s: songType){
            map.put(s,Collections.frequency(songType,s));//Hash map olduğu için birden fazla kez gelen aynı tarz bir defa ekleniyor
        }
        float songlistcount=songList.size();
        //En çok tekrar eden tarzı ve frekansını bulduk
        if (map.size()>1){
            int maxValueInMap=(Collections.max(map.values()));  // Bu en çok tekrar eden değeri döndürür.
            for (Map.Entry<String, Integer> entry : map.entrySet()) {  //Map üzerinde gezinir
                if (entry.getValue()==maxValueInMap) {
                    SongType=entry.getKey();
                    enbüyük=entry.getValue();
                    Value=(int)(100*(enbüyük/songlistcount));
                    //map.remove(SongType);
                    break;
                }
            }
        }
        else {
            int maxValueInMap=(Collections.max(map.values()));
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue()==maxValueInMap) {
                    SongType=entry.getKey();
                    Value=100;
                    break;
                }
            }
            if (songList.size()<5){ //Şarkı listesi 5ten küçükse
            Value=50; //Puan 3
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
