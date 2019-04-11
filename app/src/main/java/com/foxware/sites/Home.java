package com.foxware.sites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.interfaces.AlertActionListener;
import com.irozon.alertview.objects.AlertAction;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    BottomNavigationViewEx BN;
    TextView Title;
    private long tiempoPrimerClick;
    private static final int INTERVALO = 2000;
    CircleImageView Profile;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        final FragmentManager fm=getSupportFragmentManager();

        Title = (TextView) findViewById(R.id.toolbar_title);
        Profile = (CircleImageView) findViewById(R.id.user_picture);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        fm.beginTransaction().replace(R.id.Contenedor, new StartFragment()).commit();

  BN = (BottomNavigationViewEx) findViewById(R.id.BottomNav);
  BN.enableAnimation(false);
  BN.enableShiftingMode(false);
  BN.enableItemShiftingMode(true);
  BN.setTextVisibility(true);
  BN.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
   switch (item.getItemId()){
         case R.id.Nav_Home:
             item.setChecked(true);
             Title.setText( getString(R.string.app_name));
             fm.beginTransaction().replace(R.id.Contenedor, new StartFragment()).commit();
             break;
         case R.id.Nav_Search:
             Intent z= new Intent(Home.this, Search.class);
             startActivity(z);
             break;
         case R.id.Nav_Notify:
             item.setChecked(true);
             Title.setText( getString(R.string.home_saved));
             fm.beginTransaction().replace(R.id.Contenedor, new Notifications()).commit();
             break;
         case R.id.Nav_User:
             item.setChecked(true);
             Title.setText( getString(R.string.home_account));
             fm.beginTransaction().replace(R.id.Contenedor, new NearMe()).commit();
             break;
     }
        return false;
    }
});

    }

    @Override
    public void onStart() {
        super.onStart();

        Sites sites = new Sites(Home.this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final Uri photoUrl = sites.getPhoto_uri();
            Picasso.with(Home.this).load(photoUrl)
                    .error(R.mipmap.ic_user)
                    .placeholder(R.mipmap.ic_user)
                    .into(Profile);

            Profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if(tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Extras.makeText(Home.this, getString(R.string.alert_back));
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

    public void Settings(){
        Intent xx = new Intent(Home.this, Settings.class);
        startActivity(xx);
    }


}