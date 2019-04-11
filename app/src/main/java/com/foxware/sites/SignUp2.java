package com.foxware.sites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp2 extends AppCompatActivity {

    CircleImageView Profile;
    Uri imageUri;
    private ProgressDialog mProgress;
    private StorageReference mStorageRef;
    Button buton_next;
    String user_name, user_email;
    Sites sites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.BLACK);
        setContentView(R.layout.activity_sign_up2);

        mProgress = new ProgressDialog(SignUp2.this);
        sites = new Sites (SignUp2.this);

        FirebaseStorage fStorage = FirebaseStorage.getInstance();
        mStorageRef = fStorage.getReference();

        user_name = getIntent().getStringExtra("user_name");
        user_email = getIntent().getStringExtra("user_email");

        Profile = (CircleImageView) findViewById(R.id.profile_image);
        buton_next = findViewById(R.id.button_next);

        buton_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextStep();
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                openGallery();
            }
        });

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
                final StorageReference riversRef = mStorageRef.child("images").child(user_email);

                UploadTask uploadTask = riversRef.putFile(imageUri);

                final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            mProgress.dismiss();
                            Extras.makeText(SignUp2.this, "Fallo subir foto");
                        }
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();

                            sites.setPhoto_uri(downloadUri);
                            sites.setUser_name(user_name);
                            sites.setType("Mail");
                            mProgress.dismiss();
                        } else {
                            mProgress.dismiss();
                            Extras.makeText(SignUp2.this, "Next step fail");
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

    public void NextStep() {
        buton_next.setEnabled(false);
        Intent i = new Intent(SignUp2.this, Upload.class);
        i.putExtra("user_name", user_name);
        i.putExtra("TYPE", "UP");
        i.setData(sites.getPhoto_uri());
        startActivity(i);
        finish();
    }

}