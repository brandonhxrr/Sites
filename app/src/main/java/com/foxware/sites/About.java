package com.foxware.sites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class About extends AppCompatActivity implements View.OnClickListener{

    LinearLayout insp, icons, instagram;
    ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about2);

      insp = findViewById(R.id.inspiration);
      icons = findViewById(R.id.icons);
      instagram = findViewById(R.id.instagram);
      back_btn = findViewById(R.id.back);

      insp.setOnClickListener(this);
      icons.setOnClickListener(this);
      instagram.setOnClickListener(this);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

        Intent xx = new Intent(About.this, Web.class);

        switch (v.getId()){
            case R.id.instagram:
                xx.putExtra("web_uri", "https://www.instagram.com/brandon_hxrr");
                break;

            case R.id.icons:
                xx.putExtra("web_uri", "https://www.icons8.com/");
                break;

            case R.id.inspiration:
                xx.putExtra("web_uri", "https://design.google/");
                break;

        }
        startActivity(xx);
    }
}
