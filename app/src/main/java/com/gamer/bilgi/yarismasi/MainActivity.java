package com.gamer.bilgi.yarismasi;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.gamer.bilgi.yarismasi.MainFragment.countDownTimer;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment;
    ListView listView;
    ArrayAdapter<String> listAdaptor;
    TextView Uname,Ueposta;
    static ImageView imageVieww;
    static UserModel userModel;
    static SoruModel soruModelim;
    SpannableString  cString;
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference databaseReference;
    DatabaseReference reference;
    File imagePath;
    ArrayList<String> oyunlar = new ArrayList<>();

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        soruModelim = new SoruModel();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new Oyunbasla();
        fragmentTransaction.replace(R.id.main_linear, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        databaseReference = database.child("Users").child(currentFirebaseUser.getUid());

        readOyun();
        readFromDatabase();
            listView = (ListView)findViewById(R.id.populergames);
                    listView.setClickable(true);
                    listAdaptor = new ArrayAdapter<String>
                            (this, android.R.layout.simple_list_item_1, android.R.id.text1, oyunlar);

                    listView.setAdapter(listAdaptor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        Uname = (TextView) header.findViewById(R.id.nandsn);
        imageVieww = (ImageView) header.findViewById(R.id.imageView);
        imageVieww.setClickable(true);
        Ueposta = (TextView) header.findViewById(R.id.epostas);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                soruModelim.setOyunTur(parent.getItemAtPosition(position).toString());
                 Fragment frag = getSupportFragmentManager().findFragmentById(R.id.main_linear);
                final String myText = "Seçilen Oyun: " + parent.getItemAtPosition(position).toString();
                SpannableString colortext = changeStringColor(myText, 14);
                if (frag instanceof Oyunbasla) {

                    ((TextView) frag.getView().findViewById(R.id.textView2)).setText(colortext);
                    ((TextView) frag.getView().findViewById(R.id.textView2)).setVisibility(View.VISIBLE);
                    ((LinearLayout) frag.getView().findViewById(R.id.zorluklar)).setVisibility(View.VISIBLE);
                    ((Button) frag.getView().findViewById(R.id.kolay)).setVisibility(View.VISIBLE);
                    ((Button) frag.getView().findViewById(R.id.normal)).setVisibility(View.VISIBLE);
                    ((Button) frag.getView().findViewById(R.id.zor)).setVisibility(View.VISIBLE);
                    ((Button) frag.getView().findViewById(R.id.cokzor)).setVisibility(View.VISIBLE);
                    ((TextView) frag.getView().findViewById(R.id.oyunzorluk)).setVisibility(View.GONE);
                    ((ImageButton) frag.getView().findViewById(R.id.change)).setVisibility(View.GONE);

                }
                else if (frag instanceof MainFragment){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setMessage("Eğer şimdi çıkarsan bu test geçersiz sayılacak !");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Çık",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    countDownTimer.cancel();
                                    fragment = null;
                                    fragment = new Oyunbasla();
                                    Bundle data = new Bundle();//create bundle instance
                                    data.putString("key_value", myText);//put string to pass with a key value
                                    fragment.setArguments(data);
                                    addFragment(fragment);
                                }
                            });

                    builder1.setNegativeButton(
                            "Hayır",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else if(frag instanceof profilFragment){
                    fragment = null;
                    fragment = new Oyunbasla();
                    Bundle data = new Bundle();//create bundle instance
                    data.putString("key_value", myText);//put string to pass with a key value
                    fragment.setArguments(data);
                    addFragment(fragment);
                }
                else if(frag instanceof SoruGonderFragment){
                    fragment = null;
                    fragment = new Oyunbasla();
                    Bundle data = new Bundle();
                    data.putString("key_value", myText);

                    fragment.setArguments(data);
                    addFragment(fragment);
                }
                else if(frag instanceof NasilOyna){
                    fragment = null;
                    fragment = new Oyunbasla();
                    Bundle data = new Bundle();
                    data.putString("key_value", myText);

                    fragment.setArguments(data);
                    addFragment(fragment);
                }
                else if(frag instanceof SonucEkranFragment){
                    fragment = null;
                    fragment = new Oyunbasla();
                    Bundle data = new Bundle();
                    data.putString("key_value", myText);
                    fragment.setArguments(data);
                    addFragment(fragment);
                }
                else if(frag instanceof TabbarFragment) {
                    fragment = null;
                    fragment = new Oyunbasla();
                    Bundle data = new Bundle();
                    data.putString("key_value", myText);
                    fragment.setArguments(data);
                    addFragment(fragment);
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        imageVieww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = null;
                fragment = new profilFragment();
                listView.setAdapter(listAdaptor);

                if(!(getSupportFragmentManager().findFragmentById(R.id.main_linear) instanceof profilFragment)) {
                    addFragment(fragment);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });



    }

    @Override
    public void onBackPressed() {
       Fragment fg = getSupportFragmentManager().findFragmentById(R.id.main_linear);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fg instanceof Oyunbasla){
                finishAffinity();
            }
            else if(fg instanceof MainFragment){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Eğer şimdi çıkarsan bu test geçersiz sayılacak !");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Çık",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                listView.setAdapter(listAdaptor);
                                countDownTimer.cancel();
                                getSupportFragmentManager().popBackStack();
                            }
                        });

                builder1.setNegativeButton(
                        "Hayır",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            else if(fg instanceof SonucEkranFragment){
                    fragment = null;
                    fragment = new Oyunbasla();
                    addFragment(fragment);
            }
            else if(fg instanceof TabbarFragment){
                fragment = null;
                fragment = new Oyunbasla();
                addFragment(fragment);
            }
            else {
                    getSupportFragmentManager().popBackStack();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        listView.setAdapter(listAdaptor);
        int id = item.getItemId();
        Fragment fg = getSupportFragmentManager().findFragmentById(R.id.main_linear);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rateus) {
            if(fg instanceof MainFragment) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Eğer şimdi çıkarsan bu test geçersiz sayılacak !");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Çık",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                            }
                        });

                builder1.setNegativeButton(
                        "Hayır",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
            else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" +getPackageName())));
            }
            return true;
        }
        if (id == R.id.action_share) {
            if(fg instanceof MainFragment) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Eğer şimdi çıkarsan bu test geçersiz sayılacak !");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Çık",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                countDownTimer.cancel();
                                Bitmap b = takescreenshotOfRootView(getWindow().getDecorView());
                                saveBitmap(b);
                                shareIt();
                            }
                        });

                builder1.setNegativeButton(
                        "Hayır",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
            else {
                Bitmap b = takescreenshotOfRootView(getWindow().getDecorView());
                saveBitmap(b);
                shareIt();
            }

            return true;
        }
        if(id == R.id.action_rank){
            if(fg instanceof MainFragment) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Eğer şimdi çıkarsan bu test geçersiz sayılacak !");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Çık",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                fragment = null;
                                fragment = new TabbarFragment();
                                countDownTimer.cancel();
                                addFragment(fragment);
                            }
                        });

                builder1.setNegativeButton(
                        "Hayır",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
            else {
                fragment = null;
                fragment = new TabbarFragment();
                addFragment(fragment);
            }
            return true;
        }
        if (id == R.id.action_exit) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Çıkmak İstediğinizden eminmisiniz ?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Evet",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Fragment fg = getSupportFragmentManager().findFragmentById(R.id.main_linear);
                                if(fg instanceof MainFragment) {
                                    countDownTimer.cancel();
                                }
                                FirebaseAuth.getInstance().signOut();
                                finishAffinity();
                            }
                        });

                builder1.setNegativeButton(
                        "Hayır",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            return true;
            }





        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void readFromDatabase(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    userModel = new UserModel();
                    userModel.setEmails(dataSnapshot.child("email").getValue().toString());
                    userModel.setNames(dataSnapshot.child("KullaniciAdi").getValue().toString());
                    userModel.setPhotos(dataSnapshot.child("photo").getValue().toString());
                    userModel.setSifre(dataSnapshot.child("sifre").getValue().toString());
                    userModel.setBegendigi_soru_sayi(dataSnapshot.child("begendigi_soru_sayisi").getValue().toString());
                    userModel.setBegenmedigi_soru_sayi(dataSnapshot.child("begenmedigi_soru_sayisi").getValue().toString());
                    Uname.setText(userModel.getNames());
                    Ueposta.setText(userModel.getEmails());
                    Picasso.get().load(userModel.getPhotos()).into(imageVieww);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void addFragment(Fragment fragment) {
        if (!fragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.main_linear, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public SpannableString changeStringColor(String text,int startindex){
        cString = new SpannableString(text);
        ForegroundColorSpan color = new ForegroundColorSpan(Color.parseColor("#000000"));
        cString.setSpan(color,startindex,text.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        underlineString(cString,startindex);
        return cString;
    }
    public SpannableString underlineString(SpannableString text,int startindex){
        text.setSpan(new UnderlineSpan(), startindex, text.length(), 0);
        return text;
    }
    public void readOyun(){
        reference = database.child("Oyunlar");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i =0; i<dataSnapshot.getChildrenCount(); i++){

                    oyunlar.add(dataSnapshot.child("OyunAd"+i).getValue().toString());
                    listAdaptor.notifyDataSetChanged();
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
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
                MainActivity.this,
                "com.gamer.bilgi.yarismasi.provider", //(use your app signature + ".provider" )
                imagePath);
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "İşte Oyundan bir görüntü";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bakmak ister misin ?");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
        Log.d("resim",imageUri.toString());
    }



}