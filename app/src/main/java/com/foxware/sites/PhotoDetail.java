package com.foxware.sites;

import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class PhotoDetail extends AppCompatActivity {

    ImageView photo;
    Uri photo_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        photo_url = getIntent().getData();
        photo = findViewById(R.id.user_picture);

        Glide.with(PhotoDetail.this).load(photo_url).into(photo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
