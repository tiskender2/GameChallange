package com.gamer.bilgi.yarismasi;

import android.text.SpannableString;

public class UserModel {

    String names;
    String emails;
    String photos;
    String sifre;
    String resifre;
    String gond_sayi;
    String begendigi_soru_sayi;
    String begenmedigi_soru_sayi;


    public UserModel(){

    }
    public UserModel(String names, String emails,String photos,String sifre,String resifre,String gond_sayi,String begendigi_soru_sayi,String begenmedigi_soru_sayi){
        this.names = names;
        this.emails = emails;
        this.photos = photos;
        this.sifre = sifre;
        this.resifre = resifre;
        this.gond_sayi = gond_sayi;
        this.begendigi_soru_sayi = begendigi_soru_sayi;
        this.begenmedigi_soru_sayi = begenmedigi_soru_sayi;

    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photo) {
        this.photos = photo;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getResifre() {
        return resifre;
    }

    public void setResifre(String resifre) {
        this.resifre = resifre;
    }

    public String getGond_sayi() {
        return gond_sayi;
    }

    public void setGond_sayi(String gond_sayi) {
        this.gond_sayi = gond_sayi;
    }

    public String getBegendigi_soru_sayi() {
        return begendigi_soru_sayi;
    }

    public void setBegendigi_soru_sayi(String begendigi_soru_sayi) {
        this.begendigi_soru_sayi = begendigi_soru_sayi;
    }

    public String getBegenmedigi_soru_sayi() {
        return begenmedigi_soru_sayi;
    }

    public void setBegenmedigi_soru_sayi(String begenmedigi_soru_sayi) {
        this.begenmedigi_soru_sayi = begenmedigi_soru_sayi;
    }
}
