package com.gamer.bilgi.yarismasi;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class listViewFragment extends Fragment {

    View view;
    ListView listView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference reference;
    ProgressDialog progressDialog;
    private static CustomAdapter adapter;
    ArrayList<CustomListviewModel> dataModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_listview,container,false);
        listView = (ListView)view.findViewById(R.id.clist);
        reference = databaseReference.child("Users");
        dataModels= new ArrayList<CustomListviewModel>();
        adapter= new CustomAdapter(dataModels,getContext());
        dialog("Yükleniyor...");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(!(dataSnapshot.child("email").getValue().toString().equals("tolgaiskender@gmail.com"))){
                    dataModels.add(new CustomListviewModel(String.valueOf(dataSnapshot.child("KullaniciAdi").getValue()),String.valueOf(dataSnapshot.child("photo").getValue())
                            ,String.valueOf("Puan: "+dataSnapshot.child("puan").getValue().toString())
                            ,"",String.valueOf("Çözülen Test: "+dataSnapshot.child("oynama_sayisi").getValue())));

                    Collections.sort(dataModels, new Comparator<CustomListviewModel>(){
                        public int compare(CustomListviewModel obj1, CustomListviewModel obj2) {


                            return extractInt(obj1.getPuan()) - extractInt(obj2.getPuan()); // To compare string values

                        }
                        int extractInt(String s) {
                            String num = s.replaceAll("\\D", "");
                            // return 0 if no digits found
                            return num.isEmpty() ? 0 : Integer.parseInt(num);
                        }
                    });
                    Collections.reverse(dataModels);
                    listView.setAdapter(adapter);
                }
                Log.d("selami524",dataSnapshot.child("email").getValue().toString());


                progressDialog.dismiss();


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


        adapter.notifyDataSetChanged();
        return view;
    }
    public void dialog(String message) {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }
}
