package com.gamer.bilgi.yarismasi;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.Map;

@IgnoreExtraProperties
public class SoruModel {

    @PropertyName("Soru")
    private String soru;
    private String begeni_sayisi;
    private String begenmeme_sayisi;
    private String cevap;
    private String gonderen_id;
    private String onay;
    private String oyunTur;
    private String sikA;
    private String sikB;
    private String sikC;
    private String sikD;
    private int zorluk;
    private int zorlukSaniye;



    public  SoruModel(){

    }



    public SoruModel (String soru, String sikA, String sikB, String sikC, String sikD, String cevap, String oyunTur, String begeni_sayisi, String begenmeme_sayisi, String gonderen_id, String onay, int zorluk,int zorlukSaniye) {

        this.soru = soru;
        this.sikA = sikA;
        this.sikB = sikB;
        this.sikC = sikC;
        this.sikD = sikD;
        this.cevap = cevap;
        this.oyunTur = oyunTur;
        this.begeni_sayisi = begeni_sayisi;
        this.begenmeme_sayisi = begenmeme_sayisi;
        this.gonderen_id = gonderen_id;
        this.onay = onay;
        this.zorluk = zorluk;
        this.zorlukSaniye = zorlukSaniye;

    }

    public String getSoru() {
        return soru;
    }

    public void setSoru(String soru) {
        this.soru = soru;
    }
    public String getGonderen_id() {
        return gonderen_id;
    }

    public void setGonderen_id(String gonderen_id) {
        this.gonderen_id = gonderen_id;
    }



    public String getSikA() {
        return sikA;
    }

    public void setSikA(String sikA) {
        this.sikA = sikA;
    }

    public String getSikB() {
        return sikB;
    }

    public void setSikB(String sikB) {
        this.sikB = sikB;
    }

    public String getSikC() {
        return sikC;
    }

    public void setSikC(String sikC) {
        this.sikC = sikC;
    }

    public String getSikD() {
        return sikD;
    }

    public void setSikD(String sikD) {
        this.sikD = sikD;
    }

    public String getCevap() {
        return cevap;
    }

    public void setCevap(String cevap) {
        this.cevap = cevap;
    }

    public String getOyunTur() {
        return oyunTur;
    }

    public void setOyunTur(String oyunTur) {
        this.oyunTur = oyunTur;
    }

    public String getBegeni_sayisi() {
        return begeni_sayisi;
    }

    public void setBegeni_sayisi(String begeni_sayisi) {
        this.begeni_sayisi = begeni_sayisi;
    }

    public String getBegenmeme_sayisi() {
        return begenmeme_sayisi;
    }

    public void setBegenmeme_sayisi(String begenmeme_sayisi) {
        this.begenmeme_sayisi = begenmeme_sayisi;
    }

    public String getOnay() {
        return onay;
    }

    public void setOnay(String onay) {
        this.onay = onay;
    }

    public int getZorluk() {
        return zorluk;
    }

    public void setZorluk(int zorluk) {
        this.zorluk = zorluk;
    }

    public int getZorlukSaniye() {
        return zorlukSaniye;
    }

    public void setZorlukSaniye(int zorlukSaniye) {
        this.zorlukSaniye = zorlukSaniye;
    }
}
