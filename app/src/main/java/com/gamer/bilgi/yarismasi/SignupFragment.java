package com.gamer.bilgi.yarismasi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class SignupFragment extends Fragment implements View.OnClickListener {
    private AdView mAdView;
    Fragment fragment;
    Button girBtn,uyeolBtn;
    EditText kEmail,kSifre,kResifre,kAd;
    View view;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.activity_singup,container,false);
        MobileAds.initialize(getContext(), "ca-app-pub-4634667815519230~1183057724");
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
       firebaseAuth = FirebaseAuth.getInstance();
       uyeolSetting();
       uyeolBtn.setOnClickListener(this);
       return view;
    }


    public void uyeolSetting(){
        uyeolBtn = (Button)view.findViewById(R.id.btn_signup);
        kEmail = (EditText)view.findViewById(R.id.input_emailk);
        kSifre = (EditText)view.findViewById(R.id.input_passwordk);
        kResifre = (EditText)view.findViewById(R.id.input_repasswordk);
        kAd = (EditText)view.findViewById(R.id.input_name);
    }

    public void dialog(String message) {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }


    @Override
    public void onClick(View v) {
        final String email = kEmail.getText().toString().trim();
        final String kad = kAd.getText().toString().trim();
        final String password = kSifre.getText().toString().trim();
        String repassword = kResifre.getText().toString().trim();
        //final String photo = "default";
        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(kad) && TextUtils.isEmpty(password) && TextUtils.isEmpty(repassword)){
            Toast.makeText(getContext(), "Boş alanları doldurunuz !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!(TextUtils.equals(password,repassword))){
            Toast.makeText(getContext(), "Şifreler aynı olmak zorunda !", Toast.LENGTH_SHORT).show();
            return;
        }

        else{
            dialog("Kayıt olunuyor...");
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Başarıyla Kayıt olundu!", Toast.LENGTH_SHORT).show();
                        DatabaseReference  database = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference  currentUser = database.child("Users").child(firebaseAuth.getCurrentUser().getUid());
                        currentUser.child("KullaniciAdi").setValue(kad);
                        currentUser.child("email").setValue(email);
                        currentUser.child("sifre").setValue(password);
                        currentUser.child("photo").setValue("https://firebasestorage.googleapis.com/v0/b/gamerbilgi-df79d.appspot.com/o/userPhoto%2Fdefaultuser.png?alt=media&token=fa305180-6202-4684-974d-a3909f3a1c0c");
                        currentUser.child("puan").setValue("0");
                        currentUser.child("gonderilen_soru_sayisi").setValue("0");
                        currentUser.child("begendigi_soru_sayisi").setValue("0");
                        currentUser.child("begenmedigi_soru_sayisi").setValue("0");
                        currentUser.child("oynama_sayisi").setValue("0");
                        progressDialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),  task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}
