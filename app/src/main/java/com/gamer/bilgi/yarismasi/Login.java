package com.gamer.bilgi.yarismasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.gamer.bilgi.yarismasi.MainFragment.countDownTimer;

public class Login extends AppCompatActivity {
    Fragment fragment;
    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        boolean internet = isNetworkConnected();
        if (internet == true){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                Intent i = new Intent(Login.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
               fragment = new LoginFragment();
                addFragment(fragment);

            }
        }
        else {
            builder1 = new AlertDialog.Builder(Login.this);
            View v =  LayoutInflater.from(Login.this).inflate(R.layout.custom_layout,null);
            TextView textView = (TextView)v.findViewById(R.id.title);
            TextView aciklama = (TextView)v.findViewById(R.id.acik);
            ImageButton img = (ImageButton)v.findViewById(R.id.image);
            textView.setText("İnternet bağlantını Kontrol et !");
            builder1.setCancelable(false);
                img.setImageResource(R.drawable.sad);
                aciklama.setText("Bağlantı Yok !");
            builder1.setNegativeButton(
                    "Çık",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            // Toast.makeText(getContext(), sorular.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

            builder1.setView(v);
            builder1.show();

        }



    }

    private boolean isNetworkConnected() {

        ConnectivityManager cm = (ConnectivityManager)   getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public void uyeOlText(View view){
        fragment = null;
        fragment = new SignupFragment();
        addFragment(fragment);
    }
    public void girisYapText(View view){
        fragment = null;
        fragment = new LoginFragment();
        addFragment(fragment);
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.login, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
       if(fragment instanceof LoginFragment){
           finish();
       }
       else{
           fragment = new LoginFragment();
           addFragment(fragment);
       }

    }
}

