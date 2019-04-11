package com.foxware.sites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    ImageView Start;
    Sites sites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sites =new Sites(Splash.this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash);

        Start= (ImageView) findViewById(R.id.StartImage);
        Start();
    }

    void Open(){
        String Estado= sites.getEstado();
        Intent intent=null;
        if(Estado.equals("")){
            intent = new Intent(this , Intro.class);
            sites.setEstado("Used");
        }else if(Estado.equals("Used")){
            intent = new Intent(this , Login.class);
        }
        startActivity(intent);
        finish();

    }

    void Start(){
       String Estado=sites.getFirst_use();

       if(Estado.equals("Noche")){
           Start.setImageResource(R.drawable.night);
           sites.setFirst_use("Dia");

       }else if(Estado.equals("Dia")){
           Start.setImageResource(R.drawable.day);
           sites.setFirst_use("Noche");

       }else{
           sites.setFirst_use("Dia");
           Start.setImageResource(R.drawable.day);
       }

        time_splash();

    }

    public void time_splash(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if(currentUser == null ){
                   Open();
                }else{
                    Intent xx = new Intent(Splash.this, Home.class);
                    startActivity(xx);
                }

            }
        }, 3000);
    }

}
