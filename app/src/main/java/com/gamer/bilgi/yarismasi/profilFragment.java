package com.gamer.bilgi.yarismasi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.support.constraint.Constraints.TAG;
import static com.gamer.bilgi.yarismasi.MainActivity.imageVieww;
import static com.gamer.bilgi.yarismasi.MainActivity.userModel;

public class profilFragment extends Fragment implements View.OnClickListener{
    Fragment fragment;
    View view;
    ProgressDialog progressDialog;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference databaseReference;
    EditText ad,email,sifre,resifre;
    Button dznBtn;
    ImageView profilimage;
    private AdView mAdView;
    StorageReference storage;
    private static int SELECT_PICTURE = 1;
    Uri selectedImageURI;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profil,container,false);
        MobileAds.initialize(getContext(), "ca-app-pub-4634667815519230~1183057724");
        mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        databaseReference = database.child("Users").child(currentFirebaseUser.getUid());
        tanimlama();
        return view;
    }

    public void tanimlama(){
        storage = FirebaseStorage.getInstance().getReference();
        ad = (EditText)view.findViewById(R.id.input_name);
        email = (EditText)view.findViewById(R.id.input_emailk);
        sifre = (EditText)view.findViewById(R.id.input_passwordk);
        resifre= (EditText)view.findViewById(R.id.input_repasswordk);
        profilimage = (ImageView) view.findViewById(R.id.profil_image);
        dznBtn = (Button)view.findViewById(R.id.btn_duzenle);

        profilimage.setOnClickListener(this);
        dznBtn.setOnClickListener(this);
        if(userModel.getPhotos().equals("default")){
            profilimage.setImageDrawable(getResources().getDrawable(R.drawable.defaultuser));
        }
        else{
            Picasso.get().load(userModel.getPhotos()).into(profilimage);
        }
        ad.setText(userModel.getNames());
        email.setText(userModel.getEmails());
        sifre.setText(userModel.getSifre());
        resifre.setText(userModel.getSifre());


    }

    @Override
    public void onClick(View v) {
        if(v == dznBtn){
            userModel.setSifre(sifre.getText().toString().trim());
            userModel.setResifre(resifre.getText().toString().trim());
            userModel.setNames(ad.getText().toString().trim());
            if(TextUtils.isEmpty(userModel.getNames()) || TextUtils.isEmpty(userModel.getResifre()) || TextUtils.isEmpty(userModel.getSifre())) {
                Toast.makeText(getContext(), "Boş alanları doldurunuz", Toast.LENGTH_SHORT).show();
            }
            else if (!(sifre.getText().toString().equals(resifre.getText().toString()))   ) {
                Toast.makeText(getContext(), "Şifreler Eşleşmiyor", Toast.LENGTH_SHORT).show();
            }
            else {


                if (selectedImageURI != null) {
                    dialog("Profil Düzenleniyor...");
                    final StorageReference filepath = storage.child("userPhoto").child(selectedImageURI.getLastPathSegment());
                    filepath.putFile(selectedImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    databaseReference.child("photo").setValue(uri.toString());
                                    databaseReference.child("KullaniciAdi").setValue(userModel.getNames());
                                    databaseReference.child("sifre").setValue(userModel.getSifre());
                                    Picasso.get().load(userModel.getPhotos()).into(imageVieww);
                                }
                            });

                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Profil Güncellendi", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Hata oluştu İnternet bağlantını kontrol et", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                  // double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    progressDialog.setMessage("Profil Düzenleniyor...");
                                }
                            });
                }
                else {
                    databaseReference.child("KullaniciAdi").setValue(userModel.getNames());
                    databaseReference.child("sifre").setValue(userModel.getSifre());
                    Toast.makeText(getContext(), "Profil Güncellendi", Toast.LENGTH_SHORT).show();
                }


            }
        }
        else{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
           selectedImageURI = data.getData();
            Picasso.get().load(selectedImageURI).into(profilimage);
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
