package com.gamer.bilgi.yarismasi;

public class CustomListviewModel {
    String oy_sayi;
    String name;
    String photo;
    String puan;
    String soru_sayi;


    public CustomListviewModel(String name, String photo, String puan, String soru_sayi,String oy_sayi ) {
        this.name=name;
        this.photo=photo;
        this.puan=puan;
        this.soru_sayi=soru_sayi;
        this.oy_sayi=oy_sayi;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPuan() {
        return puan;
    }

    public void setPuan(String puan) {
        this.puan = puan;
    }

    public String getSoru_sayi() {
        return soru_sayi;
    }

    public void setSoru_sayi(String soru_sayi) {
        this.soru_sayi = soru_sayi;
    }

    public String getOy_sayi() {
        return oy_sayi;
    }

    public void setOy_sayi(String oy_sayi) {
        this.oy_sayi = oy_sayi;
    }
}
