package com.foxware.sites;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.foxware.sites.Extras.DATABASE;

public class Upload extends AppCompatActivity {

    String user_name, type;
    String firebase_name;
    String ID, Name, Address, Phone, Website, Rating, History, Descript, Lat, Long;
    Uri uri, firebase_uri;
    DatabaseReference myRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        type = getIntent().getStringExtra("TYPE");

        if(type.equals("LOGIN")){
            
            myRef = DATABASE.getReference("Usuarios").child(Extras.getUserID());
            fill();
        }else if(type.equals("UP")){
           
            myRef = DATABASE.getReference("Usuarios").child(Extras.getUserID());
            user_name = getIntent().getStringExtra("user_name");
            uri= getIntent().getData();
            addInfo(uri);
        }else if(type.equals("SITE")){
            Sites sites = new Sites(Upload.this);
            ID = getIntent().getStringExtra("site_id");
            String xName = getIntent().getStringExtra("site_name");
            Name = Extras.convert(xName);
            Address = getIntent().getStringExtra("site_address");
            Phone = getIntent().getStringExtra("site_phone");
            Website = getIntent().getStringExtra("site_web");
            Rating = getIntent().getStringExtra("site_rating");
            Descript = getIntent().getStringExtra("site_desc");
            History = getIntent().getStringExtra("site_hist");
            Lat = getIntent().getStringExtra("site_lat");
            Long = getIntent().getStringExtra("site_long");
            String Path = String.valueOf(getIntent().getData());

            ListaSitios sitio = new ListaSitios(Name, Address, Descript, History, Phone, Website, Rating, Path,sites.getUser_name(), sites.getPhoto_uri().toString(), Lat, Long );
            addSite(sitio);
        }else if(type.equals("SAVE")){
            

            ID = getIntent().getStringExtra("site_id");
            String xName = getIntent().getStringExtra("site_name");
            Name = Extras.convert(xName);
            Log.i("PRUEBA", Name);
            Address = getIntent().getStringExtra("site_location");
            Phone = getIntent().getStringExtra("site_tel");
            Website = getIntent().getStringExtra("site_web");
            Rating = getIntent().getStringExtra("site_rat");
            Descript = getIntent().getStringExtra("site_desc");
            History = getIntent().getStringExtra("site_hist");
            Lat = getIntent().getStringExtra("site_lat");
            Long = getIntent().getStringExtra("site_long");
            String Path = getIntent().getStringExtra("site_path");
            String Us = getIntent().getStringExtra("us_name");
            String UsP = getIntent().getStringExtra("us_pic");

            ListaSitios sitio = new ListaSitios(Name, Address, Descript, History, Phone, Website, Rating, Path,Us, UsP, Lat, Long );
            SaveSite(sitio);
        }else if( type.equals("DELETE")){
            
            ID = getIntent().getStringExtra("site_id");
            DeleteSite(ID);
        }


    }

    public void DeleteSite(String siteID){
        myRef = DATABASE.getReference("Guardados") .child(Extras.getUserID()).child(ID);
        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Extras.makeText(Upload.this, getString(R.string.deleted));
                    finish();
                }
            }
        });
    }

    public void addInfo(Uri downloadUri){
        User user_info = new User(user_name, downloadUri.toString());
        Extras.makeText(Upload.this,getString(R.string.up_message));
        myRef.setValue(user_info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    NextStep();
                }else{
                    Log.e("DATABASE", task.getException().getMessage());
                }
            }
        });
    }

    public void SaveSite(ListaSitios sitio){
        myRef = DATABASE.getReference("Guardados").child(Extras.getUserID()).child(ID);
        myRef.setValue(sitio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Extras.makeText(Upload.this,getString(R.string.saved));
                    finish();
                }else{
                    Log.e("SITIO", task.getException().getMessage());
                }
            }
        });
    }

    public void NextStep() {
        Intent i = new Intent(Upload.this, Home.class);
        startActivity(i);
        finish();
    }

    public void fill(){
        Extras.makeText(Upload.this,getString(R.string.up_message));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                firebase_name = user.getUser_name();
                firebase_uri = Uri.parse(user.getPhoto_url());

                Sites sites = new Sites(Upload.this);
                sites.setPhoto_uri(firebase_uri);
                sites.setUser_name(firebase_name);
                sites.setType("Mail");

                NextStep();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addSite(ListaSitios sitio){
        myRef = DATABASE.getReference("Sitios").child(ID);
        Extras.makeText(Upload.this,getString(R.string.up_message));
        myRef.setValue(sitio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Finish();
                }else{
                    Log.e("SITIO", task.getException().getMessage());
                }
            }
        });
    }

    private  void Finish(){
        Intent i = new Intent(Upload.this, Finish.class);
        i.putExtra("site_name", Name);
        startActivity(i);
        finish();
    }


}
