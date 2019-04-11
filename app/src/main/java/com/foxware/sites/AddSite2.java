package com.foxware.sites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddSite2 extends AppCompatActivity {

    TextView site_name, title, mes;
    String ID, Name, Address, Phone, Website, Rating, History, Descript, Lat, Long, Type;
    EditText Desc, Hist;
    Button btn_end;
    ImageView Profile;
    Uri imageUri;
    Sites sites;
    private ProgressDialog mProgress;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add2);
        sites = new Sites(AddSite2.this);

        Type = getIntent().getStringExtra("type");


        mProgress = new ProgressDialog(AddSite2.this);

        FirebaseStorage fStorage = FirebaseStorage.getInstance();
        mStorageRef = fStorage.getReference();

        site_name = (TextView) findViewById(R.id.step_one_ins);
        Desc = (EditText) findViewById(R.id.Desc);
        Hist = (EditText) findViewById(R.id.History);
        btn_end = (Button) findViewById(R.id.btn_end);
        Profile = (ImageView) findViewById(R.id.site_picture);
        title = findViewById(R.id.edit_title);
        mes = findViewById(R.id.step_one);

        ID = getIntent().getStringExtra("site_id");
        Name = getIntent().getStringExtra("site_name");
        Address = getIntent().getStringExtra("site_address");
        Phone = getIntent().getStringExtra("site_phone");
        Website = getIntent().getStringExtra("site_web");
        Rating = getIntent().getStringExtra("site_rating");
        Lat = getIntent().getStringExtra("site_lat");
        Long = getIntent().getStringExtra("site_long");

        if(Type.equals("edit")){

            History = getIntent().getStringExtra("site_hist");
            Descript = getIntent().getStringExtra("site_desc");
            imageUri = Uri.parse(getIntent().getStringExtra("site_path"));
            sites.setCache(imageUri);

            Picasso.with(AddSite2.this)
                    .load(imageUri)
                    .into(Profile);

            site_name.setText(getString(R.string.add_mes)+" "+Name);
            mes.setVisibility(TextView.GONE);
            title.setText(getString(R.string.add_title));
            Desc.setText(Descript);
            Hist.setText(History);
        }

        site_name.setText(getString(R.string.add_step2_ins)+" "+Name);

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSite();
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                openGallery();
            }
        });

    }

    public void checkSite(){ //TODO: Verificar que la descripción y la historia no sean muy cortas
        History = Hist.getText().toString();
        Descript = Desc.getText().toString();
        if(Descript.isEmpty() || History.isEmpty()){
            Extras.makeText(AddSite2.this, getString(R.string.error_empty));
        }else if(String.valueOf(sites.getCache()).equals("") || sites.getCache()==null){
            Extras.makeText(AddSite2.this, getString(R.string.photo_error));
        }else{
            NextStep(sites.getCache());
        }
    }

    private void openGallery() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        try {
            imageUri = paramIntent.getData();
            Profile.setImageURI(this.imageUri);

            try {
                mProgress.setMessage(getString(R.string.up_message));
                mProgress.setCancelable(false);
                mProgress.show();
                final StorageReference riversRef = mStorageRef.child("Sitios").child(ID);

                UploadTask uploadTask = riversRef.putFile(imageUri);

                final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            mProgress.dismiss();
                            Extras.makeText(AddSite2.this, getString(R.string.login_error));
                        }
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            mProgress.dismiss();
                            sites.setCache(downloadUri);
                        } else {
                            mProgress.dismiss();
                            Extras.makeText(AddSite2.this, getString(R.string.login_error));
                        }
                    }
                });

            } catch (Exception e) {
                Log.e("PHOTOSUC", e.getMessage());
            }
            return;
        } catch (Exception paramIntentx) {
            imageUri = null;
        }
    }

    public void NextStep(Uri downloadUri) {
        Intent x = new Intent(AddSite2.this, Upload.class);
        x.putExtra("TYPE", "SITE");
        x.putExtra("site_id", ID);
        x.putExtra("site_address", Address);
        x.putExtra("site_name", Name);
        x.putExtra("site_phone", Phone);
        x.putExtra("site_web",  Website);
        x.putExtra("site_rating", Rating);
        x.putExtra("site_desc", Descript);
        x.putExtra("site_hist", History);
        x.putExtra("site_lat", Lat);
        x.putExtra("site_long", Long);
        x.setData(downloadUri);
        startActivity(x);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent x = new Intent(AddSite2.this, AddSite.class);
        startActivity(x);
        finish();
    }
}
