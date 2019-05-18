package com.gamer.bilgi.yarismasi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static com.gamer.bilgi.yarismasi.MainActivity.userModel;

public class SoruGonderFragment extends Fragment{
    private FirebaseAuth firebaseAuth;
    View view;
    RadioGroup radioGroup,radioGroupoyun;
    Button gonder;
    RadioButton oyunButton;
    EditText soru,cevapA,cevapB,cevapC,cevapD;
    TextView kurallar;
    SoruModel soruModel;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ProgressDialog progressDialog;
    DatabaseReference referencesorugonder;
    DatabaseReference userinfo;
    String soru_sayi;
    private AdView mAdView;
    AlertDialog.Builder builder1;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    int i = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.soru_gonder,container,false);
        MobileAds.initialize(getContext(), "ca-app-pub-4634667815519230~1183057724");
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        soruModel = new SoruModel();
        radioGroup = (RadioGroup)view.findViewById(R.id.soruCevap);
        radioGroupoyun = (RadioGroup)view.findViewById(R.id.oyunGrup);
        soru = (EditText)view.findViewById(R.id.Qust);
        cevapA = (EditText)view.findViewById(R.id.ansA);
        cevapB = (EditText)view.findViewById(R.id.ansB);
        cevapC = (EditText)view.findViewById(R.id.ansC);
        cevapD = (EditText)view.findViewById(R.id.ansD);
        kurallar = (TextView) view.findViewById(R.id.kurallar);
        gonder = (Button)view.findViewById(R.id.gonderSoru);

        readFromDatabase();

        gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(soru.getText())&& TextUtils.isEmpty(cevapA.getText())&& TextUtils.isEmpty(cevapB.getText())&& TextUtils.isEmpty(cevapC.getText())&& TextUtils.isEmpty(cevapD.getText())  ) {
                    Toast.makeText(getContext(), "Boş alanları doldurun !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(radioGroup.getCheckedRadioButtonId() == -1 || radioGroupoyun.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getContext(), "Dogru cevabi veya Oyun türünü seçmeyi unuttunuz", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dialog("Sorunuz gönderiliyor..");
                        userInfo();
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(radioButtonID);
                        int position = radioGroup.indexOfChild(radioButton);
                        if(position == 0){
                            soruModel.setCevap("A");

                        }
                        else if(position == 1){
                            soruModel.setCevap("B");
                        }
                        else if(position == 2){
                            soruModel.setCevap("C");
                        }
                        else if(position == 3){
                            soruModel.setCevap("D");
                        }
                        int radioButtonID2 = radioGroupoyun.getCheckedRadioButtonId();
                        oyunButton = (RadioButton)view.findViewById(radioButtonID2);
                        soruModel.setOyunTur(oyunButton.getText().toString());
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference postsRef = database.child("Sorular");

                        DatabaseReference currentUser2 = postsRef.push();
                        String postId = currentUser2.getKey();
                        DatabaseReference currentUser = postsRef.child(postId);
                        currentUser.child("Soru").setValue(soru.getText().toString());
                        currentUser.child("sikA").setValue(cevapA.getText().toString());
                        currentUser.child("sikB").setValue(cevapB.getText().toString());
                        currentUser.child("sikC").setValue(cevapC.getText().toString());
                        currentUser.child("sikD").setValue(cevapD.getText().toString());
                        currentUser.child("cevap").setValue(soruModel.getCevap());
                        currentUser.child("oyunTur").setValue(soruModel.getOyunTur());
                        currentUser.child("onay").setValue("0");
                        currentUser.child("gonderen_id").setValue( currentFirebaseUser.getUid());
                        currentUser.child("begeni_sayisi").setValue("0");
                        currentUser.child("begenmeme_sayisi").setValue("0");

                        progressDialog.dismiss();

                        soru.setText("");
                        cevapA.setText("");
                        cevapB.setText("");
                        cevapC.setText("");
                        cevapD.setText("");
                        radioGroup.clearCheck();
                        radioGroupoyun.clearCheck();

                        Toast.makeText(getContext(), "Sorunuz gönderildi. Teşekkürler :)", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });
        kurallar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder1 = new AlertDialog.Builder(getContext());
                View viev =  LayoutInflater.from(getContext()).inflate(R.layout.custom_layout,null);
                TextView textView = (TextView)viev.findViewById(R.id.title);
                TextView aciklama = (TextView)viev.findViewById(R.id.acik);
                ImageView img = (ImageView)viev.findViewById(R.id.image);
                aciklama.setText("Göndereceğiniz sorunun onaylanmadan yayimlanmayacağını unutmayın \n Argo,küfür,Irkçılık içeren sorular gönderen kişilerin hesapları kapatılacaktir !");
                    img.setImageResource(R.drawable.warning);
                    textView.setText("Dikkat !");

                builder1.setNegativeButton(
                        "Anladim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setView(viev);
                builder1.show();
            }
        });
        return view;
    }

    public void dialog(String message) {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }
    @Override
    public void onResume()
    {
        radioGroup.clearCheck();
        super.onResume();
    }
    public void readFromDatabase(){
        referencesorugonder = database.child("Oyunlar");

        referencesorugonder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final RadioButton[] rb = new RadioButton[(int) dataSnapshot.getChildrenCount()];
                for(int i =1; i<dataSnapshot.getChildrenCount(); i++){


                        rb[i]  = new RadioButton(getContext());
                    RadioGroup.LayoutParams params
                            = new RadioGroup.LayoutParams(getContext(), null);
                    params.setMargins(15, 0, 0, 0);
                    rb[i].setLayoutParams(params);

                        rb[i].setBackground(getResources().getDrawable(R.drawable.mybackground7));
                        radioGroupoyun.addView(rb[i]);
                        rb[i].setText( dataSnapshot.child("OyunAd"+i).getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    public void userInfo(){
        userinfo = database.child("Users").child(currentFirebaseUser.getUid());
        userinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference user = database.child("Users").child(currentFirebaseUser.getUid());
               soru_sayi = dataSnapshot.child("gonderilen_soru_sayisi").getValue().toString();
                int soru_sayisi = Integer.valueOf(soru_sayi);
                soru_sayisi = soru_sayisi+1;
                user.child("gonderilen_soru_sayisi").setValue(String.valueOf(soru_sayisi));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
