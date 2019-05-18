package com.gamer.bilgi.yarismasi;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.util.ArrayList;

import static com.gamer.bilgi.yarismasi.MainActivity.soruModelim;
import static com.gamer.bilgi.yarismasi.MainActivity.userModel;

public class Oyunbasla extends Fragment implements View.OnClickListener{

    View view;
    TextView oyunTur,zorlugu;
    LinearLayout zorluklar;
    ImageButton change;
    Button oynaBtn,nasiloynaBtn,soruGonder,kolay,normall,zor,uzman;
    Fragment fragment;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference reference;
    static SoruModel soruModel;
    Boolean secildi=false;
    private AdView mAdView;
    Boolean goster;
    ArrayList<String> sorular;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.oyunbasla, container, false);
        MobileAds.initialize(getContext(), "ca-app-pub-4634667815519230~1183057724");
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        oyunTur = (TextView)view.findViewById(R.id.textView2);
        change = (ImageButton) view.findViewById(R.id.change);
        zorlugu = (TextView)view.findViewById(R.id.oyunzorluk);
        oynaBtn = (Button)view.findViewById(R.id.playBtn);
        nasiloynaBtn = (Button)view.findViewById(R.id.nasiloyna);
        soruGonder = (Button)view.findViewById(R.id.soruGonder);
        kolay = (Button)view.findViewById(R.id.kolay);
        normall = (Button)view.findViewById(R.id.normal);
        zor = (Button)view.findViewById(R.id.zor);
        uzman = (Button)view.findViewById(R.id.cokzor);
        zorluklar = (LinearLayout)view.findViewById(R.id.zorluklar);
        sorular = new ArrayList<String>();
        soruModel = new SoruModel();
        oyunTur.setText("");
         if (getArguments() != null) {
            String getArgument = getArguments().getString("key_value");
              goster = getArguments().getBoolean("bool");
            SpannableString text= changeStringColor(getArgument,14);
            zorlukDefault();
             oyunTur.setText(text);

         }
         else{
             change.setVisibility(View.GONE);
             zorluklar.setVisibility(View.GONE);
             zorlugu.setVisibility(View.GONE);
         }
         oynaBtn.setOnClickListener(this);
         nasiloynaBtn.setOnClickListener(this);
         soruGonder.setOnClickListener(this);
         kolay.setOnClickListener(this);
         normall.setOnClickListener(this);
         zor.setOnClickListener(this);
         uzman.setOnClickListener(this);
         change.setOnClickListener(this);
         secildi = false;

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == oynaBtn){
            if(oyunTur.getText().equals("") || secildi == false){
                Toast.makeText(getContext(), "Başlamak için Oyun ve Zorluğu Seçmelisin !", Toast.LENGTH_SHORT).show();
            }
            else {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Hazır olduğunda Başla ya Tıkla !");
                reference = databaseReference.child("Sorular");
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.child("oyunTur").getValue().toString().equals(soruModelim.getOyunTur())){
                                String name = dataSnapshot.getKey();
                                sorular.add(name);
                            }
                            else if (soruModelim.getOyunTur().equals("Bütün Oyunlar")) {
                                String name = dataSnapshot.getKey();
                                sorular.add(name);
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
                builder1.setCancelable(true);
                //kaçkere oynadı
                builder1.setPositiveButton(
                        "Başla",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                fragment = null;
                                fragment = new MainFragment();



                                if (!(getFragmentManager().findFragmentById(R.id.main_linear) instanceof MainFragment)) {
                                    Bundle data = new Bundle();//create bundle instance
                                    data.putStringArrayList("arraylist", sorular);
                                   // data.putString("key_value", sorular.toString());//put string to pass with a key value
                                    if (sorular.size() >0){
                                        fragment.setArguments(data);
                                        addFragment(fragment);
                                    }

                                }
                            }
                        });

                builder1.setNegativeButton(
                        "Çık",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                               // Toast.makeText(getContext(), sorular.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }


        }
        else if(v == nasiloynaBtn){
            fragment = null;
            fragment = new NasilOyna();
            addFragment(fragment);
        }
        else if(v == soruGonder){
            fragment = null;
            fragment = new SoruGonderFragment();
            addFragment(fragment);
        }
        else if(v == kolay){
            secildi = true;
            soruModel.setZorluk(4);
            soruModel.setZorlukSaniye(15);
           normall.setVisibility(View.GONE);
           zor.setVisibility(View.GONE);
           uzman.setVisibility(View.GONE);
           change.setVisibility(View.VISIBLE);
           zorlugu.setVisibility(View.VISIBLE);

        }
        else if (v == normall){
            secildi = true;
            soruModel.setZorluk(5);
            soruModel.setZorlukSaniye(12);
            kolay.setVisibility(View.GONE);
            zor.setVisibility(View.GONE);
            uzman.setVisibility(View.GONE);
            change.setVisibility(View.VISIBLE);
            zorlugu.setVisibility(View.VISIBLE);
        }
        else if (v == zor){
            secildi = true;
            soruModel.setZorluk(7);
            soruModel.setZorlukSaniye(8);
            normall.setVisibility(View.GONE);
            kolay.setVisibility(View.GONE);
            uzman.setVisibility(View.GONE);
            change.setVisibility(View.VISIBLE);
            zorlugu.setVisibility(View.VISIBLE);
        }
        else if (v == uzman){
            secildi = true;
            soruModel.setZorluk(9);
            soruModel.setZorlukSaniye(6);
            normall.setVisibility(View.GONE);
            kolay.setVisibility(View.GONE);
            zor.setVisibility(View.GONE);
            change.setVisibility(View.VISIBLE);
            zorlugu.setVisibility(View.VISIBLE);
        }
        else if (v == change){
            secildi = false;
            zorlukDefault();
        }

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
    public SpannableString changeStringColor(String text, int startindex){
        SpannableString cString = new SpannableString(text);
        ForegroundColorSpan color = new ForegroundColorSpan(Color.parseColor("#000000"));
        cString.setSpan(color,startindex,text.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        underlineString(cString,startindex);
        return cString;
    }
    public SpannableString underlineString(SpannableString text,int startindex){
        text.setSpan(new UnderlineSpan(), startindex, text.length(), 0);
        return text;
    }
    public void zorlukDefault(){
        normall.setVisibility(View.VISIBLE);
        kolay.setVisibility(View.VISIBLE);
        uzman.setVisibility(View.VISIBLE);
        zor.setVisibility(View.VISIBLE);
        change.setVisibility(View.GONE);
        zorlugu.setVisibility(View.GONE);
    }

}