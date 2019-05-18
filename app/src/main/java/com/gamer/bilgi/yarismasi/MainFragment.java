package com.gamer.bilgi.yarismasi;


import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import static com.gamer.bilgi.yarismasi.MainActivity.userModel;
import static com.gamer.bilgi.yarismasi.Oyunbasla.soruModel;


public class MainFragment extends Fragment implements View.OnClickListener {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference reference;
    DatabaseReference reference2;
    DatabaseReference reference3;
    DatabaseReference referencem4;
    DatabaseReference referencem5;
    DatabaseReference referencem6;
    DatabaseReference referencem7;
    DatabaseReference referencem8;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    Button soruBtn,cevapaBtn,cevapbBtn,cevapcBtn,cevapdBtn,oyunTur,totalSoru,like,dislike;
    ArrayList<String> soru;
    ArrayList<String> cevaplar;
    ArrayList<String> begeniler;
    SoruModel soruModelim;
    boolean begendim=false;
    boolean begenmedim=false;
    static CountDownTimer countDownTimer;
    TextView tvTime,begenisayi,begenmeme_sayi,gonderenkisi;
    Fragment fragment;
    String gelensoru,soruKey;
    int gelensoruindex =0;
    AlertDialog.Builder builder1;
    View view;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    int soru_begeni_sayi;
    int soru_begenmeme_sayi;
    int begendigi_soru_sayisi;
    int begenmedigi_soru_sayisi;

    Animation animAlpha;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.soru_activity, container, false);
        MobileAds.initialize(getContext(), "ca-app-pub-4634667815519230~1183057724");
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-4634667815519230/7561597968");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        soruBtn = (Button)view.findViewById(R.id.Qust);
        cevapaBtn = (Button)view.findViewById(R.id.ans1);
        cevapbBtn = (Button)view.findViewById(R.id.ans2);
        cevapcBtn = (Button)view.findViewById(R.id.ans3);
        cevapdBtn = (Button)view.findViewById(R.id.ans4);
        oyunTur = (Button)view.findViewById(R.id.nasiloyna);
        totalSoru = (Button)view.findViewById(R.id.TotalQust);
        like = (Button)view.findViewById(R.id.like);
        dislike = (Button)view.findViewById(R.id.dislike);
        tvTime = (TextView)view.findViewById(R.id.saniye);
        begenisayi = (TextView)view.findViewById(R.id.txtTrue);
        begenmeme_sayi = (TextView)view.findViewById(R.id.txtFalse);
        gonderenkisi = (TextView)view.findViewById(R.id.gonderen);
        cevapaBtn.setOnClickListener(this);
        cevapbBtn.setOnClickListener(this);
        cevapcBtn.setOnClickListener(this);
        cevapdBtn.setOnClickListener(this);
        like.setOnClickListener(this);
        dislike.setOnClickListener(this);
        gonderenkisi.setVisibility(View.GONE);
        reference2 = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilen_sorular");
        reference3 = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilmeyen_sorular");
        referencem6 = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begendigi_soru_sayisi");
        referencem7 = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenmedigi_soru_sayisi");
        referencem8 = databaseReference.child("Users");

        reference = databaseReference.child("Sorular");
        soruModelim = new SoruModel();
        cevaplar = new ArrayList<>();
        begeniler = new ArrayList<String>();
        soru = new ArrayList<>();
        if (getArguments() != null) {
            cevaplar  = getArguments().getStringArrayList("arraylist");
            Collections.shuffle(cevaplar);
        }

        if (cevaplar.size() > 0){
            for(int i=0; i<10; i++){
                Object randomItem = cevaplar.get(i);
                soru.add(randomItem.toString());
            }

            soruCek();
        }
        timer();


        animAlpha = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha);

        return view;
    }


    public void soruCek(){
        begendim = false;
        begenmedim = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if(getActivity() == null)
                return;
          getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    like.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                    dislike.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));

                }
            });

        }
        gelensoru = soru.get(gelensoruindex);
        gelensoruindex++;
        referencem5 = databaseReference.child("Sorular").child(gelensoru).child("begeni_sayisi");
        referencem4 = databaseReference.child("Sorular").child(gelensoru).child("begenmeme_sayisi");
        reference.child(gelensoru).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("onay").getValue().toString().equals("1")) {
                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            soruBtn.setText(dataSnapshot.child("Soru").getValue().toString());
                            cevapaBtn.setText(dataSnapshot.child("sikA").getValue().toString());
                            cevapbBtn.setText(dataSnapshot.child("sikB").getValue().toString());
                            cevapcBtn.setText(dataSnapshot.child("sikC").getValue().toString());
                            cevapdBtn.setText(dataSnapshot.child("sikD").getValue().toString());
                            oyunTur.setText(dataSnapshot.child("oyunTur").getValue().toString());
                            begenisayi.setText(dataSnapshot.child("begeni_sayisi").getValue().toString());
                            begenmeme_sayi.setText(dataSnapshot.child("begenmeme_sayisi").getValue().toString());
                            totalSoru.setText((gelensoruindex) + "/" + soru.size());
                        }
                    });

                    soruModelim.setCevap(dataSnapshot.child("cevap").getValue().toString());
                    soruModelim.setBegeni_sayisi(dataSnapshot.child("begeni_sayisi").getValue().toString());
                    soruModelim.setBegenmeme_sayisi(dataSnapshot.child("begenmeme_sayisi").getValue().toString());
                    soruModelim.setGonderen_id(dataSnapshot.child("gonderen_id").getValue().toString());

                     soru_begeni_sayi = Integer.valueOf(soruModelim.getBegeni_sayisi());
                     soru_begenmeme_sayi = Integer.valueOf(soruModelim.getBegenmeme_sayisi());
                     begendigi_soru_sayisi = Integer.valueOf(userModel.getBegendigi_soru_sayi());
                     begenmedigi_soru_sayisi = Integer.valueOf(userModel.getBegenmedigi_soru_sayi());
                }
                else {
                    Log.d("sorumus",dataSnapshot.child("Soru").getValue().toString());
                }
                clickAble(true);
                countDownTimer.start();

                if(soruModelim.getGonderen_id() != null) {
                    referencem8.child(soruModelim.getGonderen_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                            if (getActivity() == null)
                                return;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!(dataSnapshot.child("email").getValue().toString().equals("tolgaiskender@gmail.com"))) {
                                        gonderenkisi.setText("Gönderen: " + dataSnapshot.child("KullaniciAdi").getValue().toString());
                                        gonderenkisi.setVisibility(View.VISIBLE);
                                    } else {
                                        gonderenkisi.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                reference2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot2, @Nullable String s) {

                        if(gelensoru.equals(dataSnapshot2.getValue().toString())){
                            begendim = true;
                            if(getActivity() == null)
                                return;
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    like.setBackground(getResources().getDrawable(R.drawable.thumb_up_button));

                                }
                            });

                            begeniler.add(dataSnapshot2.getKey());
                            soruKey = dataSnapshot2.getKey();
                        }


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                reference3.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot2, @Nullable String s) {

                        if(gelensoru.equals(dataSnapshot2.getValue().toString())){
                            begenmedim = true;
                            if(getActivity() == null)
                                return;
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    dislike.setBackground(getResources().getDrawable(R.drawable.thumb_down_button));

                                }
                            });

                            begeniler.add(dataSnapshot2.getKey());
                            soruKey = dataSnapshot2.getKey();
                        }


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        v.startAnimation(animAlpha);
        if(v == cevapaBtn){
            countDownTimer.cancel();
            bcevapRenk(cevapaBtn);
            if(soruModelim.getCevap().equals("A")){
               dcevapRenk(cevapaBtn);

            }
            else {
                yanlisCevap();
            }



        }
        else if (v == cevapbBtn){
            countDownTimer.cancel();
            bcevapRenk(cevapbBtn);
            if(soruModelim.getCevap().equals("B")){
                dcevapRenk(cevapbBtn);
            }
            else {
                yanlisCevap();
            }

        }
        else if (v == cevapcBtn){
            countDownTimer.cancel();
            bcevapRenk(cevapcBtn);
            if(soruModelim.getCevap().equals("C")){
                dcevapRenk(cevapcBtn);
            }
            else {
                yanlisCevap();
            }

        }
        else if (v == cevapdBtn){
            countDownTimer.cancel();
            bcevapRenk(cevapdBtn);
            if(soruModelim.getCevap().equals("D")){
                dcevapRenk(cevapdBtn);
            }
            else {
                yanlisCevap();
            }

        }
        else if(v==like){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (begendim == true && begenmedim == false) {
                    DatabaseReference referencem = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilen_sorular");
                    referencem.child(soruKey).removeValue();
                    begendim = false;
                    soru_begeni_sayi--;
                    begendigi_soru_sayisi--;
                    final int finalSoru_begeni_sayi = soru_begeni_sayi;
                    final int finalBegendigi_soru_sayisi1 = begendigi_soru_sayisi;
                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            referencem5.setValue(String.valueOf(finalSoru_begeni_sayi));
                            referencem6.setValue(String.valueOf(finalBegendigi_soru_sayisi1));
                            begenisayi.setText(String.valueOf(finalSoru_begeni_sayi));
                            like.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));

                        }
                    });
                }
                else if(begendim==false && begenmedim==true){
                    DatabaseReference referencem = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilmeyen_sorular");
                    referencem.child(soruKey).removeValue();
                    DatabaseReference referencem2 = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilen_sorular").push();
                    referencem2.setValue(gelensoru);
                    begendim = true;
                    begenmedim= false;
                    soru_begeni_sayi++;
                    soru_begenmeme_sayi--;
                    begendigi_soru_sayisi++;
                    begenmedigi_soru_sayisi--;
                    final int finalSoru_begeni_sayi = soru_begeni_sayi;
                    final int finalSoru_begenmeme_sayi = soru_begenmeme_sayi;
                    if(getActivity() == null)
                        return;
                    final int finalBegendigi_soru_sayisi2 = begendigi_soru_sayisi;
                    final int finalBegenmedigi_soru_sayisi = begenmedigi_soru_sayisi;
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            begenisayi.setText(String.valueOf(finalSoru_begeni_sayi));
                            begenmeme_sayi.setText(String.valueOf(finalSoru_begenmeme_sayi));
                            referencem5.setValue(String.valueOf(finalSoru_begeni_sayi));
                            referencem4.setValue(String.valueOf(finalSoru_begenmeme_sayi));
                            referencem6.setValue(String.valueOf(finalBegendigi_soru_sayisi2));
                            referencem7.setValue(String.valueOf(finalBegenmedigi_soru_sayisi));
                            like.setBackground(getResources().getDrawable(R.drawable.thumb_up_button));
                            dislike.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));

                        }
                    });
                }


                else if(begenmedim==false && begendim==false) {
                    DatabaseReference referencem = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilen_sorular").push();
                    referencem.setValue(gelensoru);
                    begendim = true;
                    soru_begeni_sayi++;
                    begendigi_soru_sayisi++;
                    final int finalSoru_begeni_sayi = soru_begeni_sayi;
                    final int finalBegendigi_soru_sayisi = begendigi_soru_sayisi;
                    if(getActivity() == null)
                        return;

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            referencem5.setValue(String.valueOf(finalSoru_begeni_sayi));
                            referencem6.setValue(String.valueOf(finalBegendigi_soru_sayisi));
                            begenisayi.setText(String.valueOf(finalSoru_begeni_sayi));
                            like.setBackground(getResources().getDrawable(R.drawable.thumb_up_button));
                        }
                    });

                }
            }
           }

           else if(v == dislike){

            if(begenmedim==false && begendim==false){
                DatabaseReference referencem = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilmeyen_sorular").push();
                referencem.setValue(gelensoru);
                begenmedim = true;
                soru_begenmeme_sayi++;
                begenmedigi_soru_sayisi++;
                final int finalBegenmedigi_soru_sayisi = begenmedigi_soru_sayisi;
                final int finalSoru_begenmeme_sayi = soru_begenmeme_sayi;

                if(getActivity() == null)
                    return;


                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        referencem4.setValue(String.valueOf(finalSoru_begenmeme_sayi));
                        referencem7.setValue(String.valueOf(finalBegenmedigi_soru_sayisi));
                        begenmeme_sayi.setText(String.valueOf(finalSoru_begenmeme_sayi));
                        dislike.setBackground(getResources().getDrawable(R.drawable.thumb_down_button));
                    }
                });
            }
            else if(begenmedim==true && begendim == false){
                DatabaseReference referencem = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilmeyen_sorular");
                referencem.child(soruKey).removeValue();
                begenmedim=false;
                soru_begenmeme_sayi--;
                begenmedigi_soru_sayisi--;
                final int finalSoru_begenmeme_sayi = soru_begenmeme_sayi;
                final int finalBegenmedigi_soru_sayisi1 = begenmedigi_soru_sayisi;
                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        referencem4.setValue(String.valueOf(finalSoru_begenmeme_sayi));
                        referencem7.setValue(String.valueOf(finalBegenmedigi_soru_sayisi1));
                        begenmeme_sayi.setText(String.valueOf(finalSoru_begenmeme_sayi));
                        dislike.setBackground(getResources().getDrawable(R.drawable.ic_thumb_down_black_24dp));

                    }
                });
            }
            else if(begenmedim==false && begendim==true){
                DatabaseReference referencem = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilen_sorular");
                referencem.child(soruKey).removeValue();
                DatabaseReference referencem2 = databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).child("begenilmeyen_sorular").push();
                referencem2.setValue(gelensoru);
                begenmedim = true;
                begendim = false;
                soru_begenmeme_sayi++;
                begenmedigi_soru_sayisi++;
                begendigi_soru_sayisi--;
                soru_begeni_sayi--;
                Log.d("sorular begeni",String.valueOf(soru_begeni_sayi));
                Log.d("sorular begenmeme",String.valueOf(soru_begenmeme_sayi));
                final int finalSoru_begeni_sayi = soru_begeni_sayi;
                final int finalSoru_begenmeme_sayi = soru_begenmeme_sayi;
                final int finalBegendigi_soru_sayisi = begendigi_soru_sayisi;
                final int finalBegenmedigi_soru_sayisi2 = begenmedigi_soru_sayisi;
                if(getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        referencem5.setValue(String.valueOf(finalSoru_begeni_sayi));
                        referencem6.setValue(String.valueOf(finalBegendigi_soru_sayisi));
                        referencem7.setValue(String.valueOf(finalBegenmedigi_soru_sayisi2));
                        referencem4.setValue(String.valueOf(finalSoru_begenmeme_sayi));
                        begenisayi.setText(String.valueOf(finalSoru_begeni_sayi));
                        begenmeme_sayi.setText(String.valueOf(finalSoru_begenmeme_sayi));
                        like.setBackground(getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                        dislike.setBackground(getResources().getDrawable(R.drawable.thumb_down_button));

                    }
                });
            }
        }
        }


    public void dcevapRenk(final Button button){

        final Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(2000);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    button.setBackground(getContext().getDrawable(R.drawable.mybackground10));
                                }
                                else {
                                    button.setBackground(getResources().getDrawable(R.drawable.mybackground10));
                                }
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        };
        thread.start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {


            }
        }, 2000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                renkSil(button);
                if(gelensoruindex != soru.size()){
                    soruCek();
                }
                else{
                    countDownTimer.cancel();
                    fragment = new SonucEkranFragment();
                    addFragment(fragment);
                }


            }
        }, 3000);


    }
    public void bcevapRenk(final Button button){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.setBackground(getContext().getDrawable(R.drawable.mybackground3));
                }
                else {
                    button.setBackground(getResources().getDrawable(R.drawable.mybackground3));
                }
            }
        });

        clickAble(false);

    }
    public void yanlisCevap(){
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        alert("Üzgünüm !");
                    }
                }, 2000);


    }
    public void renkSil(final Button button){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.setBackground(getContext().getDrawable(R.drawable.mybackground));
                }
                else {
                    button.setBackground(getResources().getDrawable(R.drawable.mybackground));
                }
            }
        });

    }

    public void alert(String message){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        builder1 = new AlertDialog.Builder(getContext());
        View v =  LayoutInflater.from(getContext()).inflate(R.layout.custom_layout,null);
        TextView textView = (TextView)v.findViewById(R.id.title);
        TextView aciklama = (TextView)v.findViewById(R.id.acik);
        ImageView img = (ImageView)v.findViewById(R.id.image);
        textView.setText(message);
        builder1.setCancelable(false);
        if(textView.getText() == "Malesef !"){
            img.setImageResource(R.drawable.sad);
            aciklama.setText("Süre Bitti !");
        }
        else {
            img.setImageResource(R.drawable.unhappy);
            aciklama.setText("Yanlış Cevap !");
        }
        builder1.setNegativeButton(
                "Çık",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        getFragmentManager().popBackStack();
                        // Toast.makeText(getContext(), sorular.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        builder1.setView(v);
        builder1.show();

    }

    public void timer(){
        countDownTimer = new CountDownTimer(soruModel.getZorlukSaniye()*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(String.valueOf((int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                tvTime.setText(String.valueOf("0"));
                alert("Malesef !");
            }
        };

    }
    public void clickAble(Boolean deger){
        soruBtn.setClickable(deger);
        cevapaBtn.setClickable(deger);
        cevapbBtn.setClickable(deger);
        cevapcBtn.setClickable(deger);
        cevapdBtn.setClickable(deger);

    }
    public void addFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.main_linear, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }



}
