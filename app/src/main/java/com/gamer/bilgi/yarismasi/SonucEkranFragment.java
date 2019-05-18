package com.gamer.bilgi.yarismasi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import static com.gamer.bilgi.yarismasi.Oyunbasla.soruModel;

public class SonucEkranFragment extends Fragment implements View.OnClickListener{
    int yenipuan;
    View view;
    Fragment fragment;
    int sonuc,oynandi;
    ImageButton gohome,share,rate,puan;
    TextView alPuan,alpuanSonuc,tpuanSonuc,yToplamPuan;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference databaseReference;
    File imagePath;
    private AdView mAdView;
    private RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sonuc_ekran,container,false);
        MobileAds.initialize(getContext(), "ca-app-pub-4634667815519230~1183057724");
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setRewardedVideoAdListener(rewardedVideoAdListener);
        loadRewardedVideoAd();
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId("ca-app-pub-4634667815519230/7561597968");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else {
            Log.d("TAG2", "The interstitial wasn't loaded yet.");
        }
        mInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdClosed() {
                // Load the next interstitial.

            }

        });
        alPuan = (TextView)view.findViewById(R.id.alPuan);
        alpuanSonuc = (TextView)view.findViewById(R.id.alpuanSonuc);
        tpuanSonuc = (TextView)view.findViewById(R.id.tPuanSonuc);
        yToplamPuan = (TextView)view.findViewById(R.id.yToplampuan);
        gohome =(ImageButton) view.findViewById(R.id.home);
        share =(ImageButton) view.findViewById(R.id.share);
        rate =(ImageButton) view.findViewById(R.id.rateus);
        puan =(ImageButton) view.findViewById(R.id.puan);
        gohome.setOnClickListener(this);
        share.setOnClickListener(this);
        rate.setOnClickListener(this);
        puan.setOnClickListener(this);

        databaseReference = database.child("Users").child(currentFirebaseUser.getUid());
        sonuc=10*soruModel.getZorluk();
        alPuan.setText("Alınan Puan(10*"+soruModel.getZorluk()+")");
        alpuanSonuc.setText(String.valueOf(sonuc));
        readFromDatabase();

        return view;
    }

    public void readFromDatabase(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                oynandi = Integer.valueOf(dataSnapshot.child("oynama_sayisi").getValue().toString());
                tpuanSonuc.setText(dataSnapshot.child("puan").getValue().toString());
               yToplamPuan.setText(String.valueOf(Integer.valueOf(tpuanSonuc.getText().toString())+sonuc));
               databaseReference.child("puan").setValue(yToplamPuan.getText().toString());
               databaseReference.child("oynama_sayisi").setValue((oynandi+1));
           yenipuan = Integer.valueOf(yToplamPuan.getText().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        if(v == gohome){
            fragment = null;
            fragment = new Oyunbasla();
            addFragment(fragment);
        }
        else if (v == share){

           Bitmap b=  takescreenshotOfRootView(this.getView());
            saveBitmap(b);
            shareIt();
        }
        else if(v == puan){

            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }
            else {
                Log.d("TAG2", "The interstitial wasn't loaded yet.");
            }
        }
        else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getContext().getPackageName())));
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
    public static Bitmap takescreenshot(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotOfRootView(View v) {
        return takescreenshot(v.getRootView());
    }


    private void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/scrnshot.jpeg"); ////File imagePath
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }}
    private void shareIt() {

        Uri imageUri = FileProvider.getUriForFile(
                getContext(),
                "com.gamer.bilgi.yarismasi.provider", //(use your app signature + ".provider" )
                imagePath);
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "İşte En yüksek Puanım";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bakmak ister misin ?");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));}

    RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
        @Override
        public void onRewardedVideoAdLoaded() {
            Toast.makeText(getActivity(), "Ödüllü reklamın yüklendi izleyip +10 puan kazanabilirsin ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoStarted() {

        }

        @Override
        public void onRewardedVideoAdClosed() {

            loadRewardedVideoAd();

        }

        @Override
        public void onRewarded(RewardItem reward) {

        }

        @Override
        public void onRewardedVideoAdLeftApplication() {

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {

        }

        @Override
        public void onRewardedVideoCompleted() {

            yenipuan=yenipuan+10;
            yToplamPuan.setText(String.valueOf(yenipuan));
            databaseReference.child("puan").setValue(String.valueOf(yenipuan));

        }
    };

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-4634667815519230/5981224300",
                new AdRequest.Builder().build());
    }


}
