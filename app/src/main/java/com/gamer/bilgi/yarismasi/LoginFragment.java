package com.gamer.bilgi.yarismasi;

import android.app.Activity;
import android.app.ActivityOptions;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private AdView mAdView;
    Button girBtn;
    EditText email,sifre;
    ProgressDialog progressDialog;
    View view;
    private FirebaseAuth firebaseAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_login,container,false);
        MobileAds.initialize(getContext(), "ca-app-pub-4634667815519230~1183057724");
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        firebaseAuth = FirebaseAuth.getInstance();
        girisSetting();
        return view;
    }

    public void girisSetting(){
        girBtn = (Button)view.findViewById(R.id.btn_login);
        email = (EditText)view.findViewById(R.id.input_email);
        sifre = (EditText)view.findViewById(R.id.input_password);
        girBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        String email2 = email.getText().toString().trim();
        String password2 = sifre.getText().toString().trim();

        if (TextUtils.isEmpty(email2) && TextUtils.isEmpty(password2)) {
            Toast.makeText(getContext(), "Boş alanları doldur", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            dialog("Giriş yapılıyor..");
            firebaseAuth.signInWithEmailAndPassword(email2, password2)
                    .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                } else {

                                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                }
                                progressDialog.dismiss();



                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Giriş Yapılamadı", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }




    public void dialog(String message) {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }


}
