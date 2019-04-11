package com.foxware.sites;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.interfaces.AlertActionListener;
import com.irozon.alertview.objects.AlertAction;
import com.squareup.picasso.Picasso;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    String sName, sLoc, sDesc, sHist, sTel, sWeb, sRat, sLat, sLong, sID, sType, sN, sP;
    TextView txt_loc, text_desc, text_hist, txtRating, txtName, txt1, txt2, txt3, txt4;
    Uri Path;
    RatingBar rating;
    ImageView Image, btn1, btn2, btn3, goton, btn4;
    private static final int MY_PERMISSIONS_REQUEST_CALL = 707;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail3);

        Sites sites = new Sites(DetailActivity.this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_loc = (TextView) findViewById(R.id.siteLocation);
        text_desc = (TextView) findViewById(R.id.description);
        text_hist = (TextView) findViewById(R.id.siteHistory);
        rating = (RatingBar) findViewById(R.id.site_ratingx);
        Image = (ImageView) findViewById(R.id.image);
        txtRating = (TextView) findViewById(R.id.textRating);
        txtName = (TextView) findViewById(R.id.site_name);

        btn1 = (ImageView) findViewById(R.id.call1);
        btn2 = (ImageView) findViewById(R.id.web1);
        btn3 = (ImageView) findViewById(R.id.share1);
        btn4 = (ImageView) findViewById(R.id.save2);

        txt1 = (TextView) findViewById(R.id.call2);
        txt2 = (TextView) findViewById(R.id.web2);
        txt3 = (TextView) findViewById(R.id.share2);
        txt4 = (TextView) findViewById(R.id.save22);
        goton = findViewById(R.id.btn_goto);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        txt1.setOnClickListener(this);
        txt2.setOnClickListener(this);
        txt3.setOnClickListener(this);
        Image.setOnClickListener(this);
        goton.setOnClickListener(this);

        sName = getIntent().getStringExtra("site_name");
        sLoc = getIntent().getStringExtra("site_location");
        sDesc = getIntent().getStringExtra("site_desc");
        sHist = getIntent().getStringExtra("site_hist");
        sTel = getIntent().getStringExtra("site_tel");
        sRat = getIntent().getStringExtra("site_rat");
        sWeb = getIntent().getStringExtra("site_web");
        Path = Uri.parse(getIntent().getStringExtra("site_path"));
        sLat = getIntent().getStringExtra("site_lat");
        sLong = getIntent().getStringExtra("site_long");
        sID= getIntent().getStringExtra("site_id");

        sType = sites.getSType();
        sN = getIntent().getStringExtra("us_name");
        sP = getIntent().getStringExtra("us_pic");

        if(sType.equals("saved")){
           btn4.setImageResource(R.drawable.ic_ibookmark);
           txt4.setText(getString(R.string.delete));
        }

        Picasso.with(DetailActivity.this)
                .load(Path)
                .into(Image);

        String Rat = sRat.replace(".", ",");
        txtName.setText(sName);
        txtRating.setText(Rat);

        txt_loc.setText(sLoc);
        text_desc.setText(sDesc);
        text_hist.setText(sHist);

        rating.setRating(Float.parseFloat(sRat));

    }

    @Override
    public void onClick(View v) {
        int CID = v.getId();

        if (CID == R.id.call1 || CID == R.id.call2) {
            if (!sTel.isEmpty()) {
                callTo(sTel);
            }else{
                Extras.makeText(DetailActivity.this, getString(R.string.detail_phone));
            }
        } else if (CID == R.id.web1 || CID == R.id.web2) {
            if (!sWeb.isEmpty() && !sWeb.equals("null")) {//TODO: Verificar que esto sirva
                Intent x = new Intent(DetailActivity.this, Web.class);
                x.putExtra("web_uri", sWeb);
                startActivity(x);
            }else{
                Extras.makeText(DetailActivity.this, getString(R.string.detail_web));
            }
        } else if (CID == R.id.share1 ||CID == R.id.share2) {
            share();
        }else if(CID == R.id.image){
            Intent photo = new Intent(DetailActivity.this, PhotoDetail.class);
            photo.setData(Path);
            startActivity(photo);
        }else if(CID == R.id.btn_goto){
            Intent Maps = new Intent(Intent.ACTION_VIEW);
            Maps.setData(Uri.parse("geo:" + sLat + " , " + sLong));
            Intent chooser = Intent.createChooser(Maps, getString(R.string.open));
            startActivity(chooser);
        } if(CID == R.id.save2 ||CID == R.id.save22){
            Intent x = new Intent(DetailActivity.this, Upload.class);
            x.putExtra("site_id", sID);
            if(sType.equals("saved")){
                x.putExtra("TYPE", "DELETE");
                finish();
            }else{
                x.putExtra("TYPE", "SAVE");
                x.putExtra("site_name", sName);
                x.putExtra("site_location", sLoc);
                x.putExtra("site_desc", sDesc);
                x.putExtra("site_hist", sHist);
                x.putExtra("site_tel", sTel);
                x.putExtra("site_web", sWeb);
                x.putExtra("site_rat", sRat);
                x.putExtra("site_path", Path.toString());
                x.putExtra("site_lat", sLat);
                x.putExtra("site_long", sLong);
                x.putExtra("us_name", sN);
                x.putExtra("us_pic", sP);

            }
            startActivity(x);
        }
    }

    public void callTo(final String num) {
        AlertView alert = new AlertView(getString(R.string.alert_av), getString(R.string.alert_call), AlertStyle.DIALOG);
        alert.addAction(new AlertAction(getString(R.string.alert_ok), AlertActionStyle.POSITIVE, new AlertActionListener() {
            @Override
            public void onActionClick(AlertAction alertAction) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:" + num));
                checkPermission();
                startActivity(i);
            }}));
        alert.addAction(new AlertAction(getString(R.string.alert_cancel), AlertActionStyle.NEGATIVE, new AlertActionListener() {
            @Override
            public void onActionClick(AlertAction alertAction) {
            }}));
        alert.show(this);
    }

    public void share(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share1)+" "+sName+" "+getString(R.string.share2)+" "+sLoc);
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent, getString(R.string.share_with));
        startActivity(chooser);
    }

    void checkPermission(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(DetailActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?    
                if (ActivityCompat.shouldShowRequestPermissionRationale(DetailActivity.this, android.Manifest.permission.CALL_PHONE)) {
                        // Show an expanation to the user *asynchronously* -- don't block        
                        // this thread waiting for the user's response! After the user        
                        // sees the explanation, try again to request the permission.
        } else {
             // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(DetailActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an        
                    // app-defined int constant. The callback method gets the        
                    // result of the request.    
                    } }

            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         switch (requestCode){
             case MY_PERMISSIONS_REQUEST_CALL:
                 if(grantResults.length  > 0
                         && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 }else{

                 }return;
         }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    //   @Override
 //   public boolean onCreateOptionsMenu(Menu menu) {
 //       getMenuInflater().inflate(R.menu.menu_detail, menu);
 //       return true;
 //   }
//
 //   @Override
 //   public boolean onOptionsItemSelected(MenuItem item) {
//
 //       int xID= item.getItemId();
 //       if(xID == R.id.Map) {
 //           Intent Maps = new Intent(Intent.ACTION_VIEW);
 //           Maps.setData(Uri.parse("geo:" + sLat + " , " + sLong));
 //           Intent chooser = Intent.createChooser(Maps, getString(R.string.open));
 //           startActivity(chooser);
 //       }else if(xID == R.id.Edit){
 //           edit();
 //       }
 //       return super.onOptionsItemSelected(item);
 //   }

    public void edit(){
        Intent x =new Intent(DetailActivity.this, AddSite2.class);
        x.putExtra("type", "Edit");
        x.putExtra("site_id", sID);
        x.putExtra("site_address", sLoc);
        x.putExtra("site_name", sName);
        x.putExtra("site_phone", sTel);
        x.putExtra("site_web",  sWeb);
        x.putExtra("site_rating", sRat);
        x.putExtra("site_lat", sLat);
        x.putExtra("site_long", sLong);
        x.putExtra("site_hist", sHist);
        x.putExtra("site_desc", sDesc);
        x.putExtra("site_path", Path.toString());
        startActivity(x);
        finish();
    }
}
