package com.foxware.sites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

public class Update extends AppCompatActivity implements View.OnClickListener{

    String user_name, user_mail,old_pass;
    Uri photo_url;
    EditText user, mail, pass, pass2, old;
    CircleImageView image;
    Button finish;
    FirebaseUser Fuser;
    Sites sites;
    Uri imageUri;
    private ProgressDialog mProgress;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        sites = new Sites(Update.this);
        Fuser = FirebaseAuth.getInstance().getCurrentUser();

        mProgress = new ProgressDialog(Update.this);

        FirebaseStorage fStorage = FirebaseStorage.getInstance();
        mStorageRef = fStorage.getReference();

        user_name = sites.getUser_name();
        user_mail = Fuser.getEmail();
        photo_url = sites.getPhoto_uri();
        old_pass = sites.getOldPass();

        user = findViewById(R.id.user_name);
        mail = findViewById(R.id.user_mail);
        pass = findViewById(R.id.user_password);
        pass2 = findViewById(R.id.user_password2);
        image = findViewById(R.id.user_picture);
        finish = findViewById(R.id.btn_finish);
        old = findViewById(R.id.old_pass);

        image.setOnClickListener(this);
        finish.setOnClickListener(this);

        user.setText(user_name);
        mail.setText(user_mail);

        Picasso.with(Update.this)
                .load(photo_url)
                .error(R.mipmap.ic_user)
                .into(image);
    }

    public void Verify(){
        Pattern pattern= Patterns.EMAIL_ADDRESS;
        final String user_n = user.getText().toString();
        final String user_m = mail.getText().toString();
        final String password = pass.getText().toString();
        String oldP = old.getText().toString();

        if(user_n.isEmpty()){
            Extras.makeText(this, getString(R.string.errorUser));
        }else if(user_m.isEmpty() || !pattern.matcher(user_m).matches()){
            Extras.makeText(this, getString(R.string.errorMail));
        }else if(password.isEmpty() || password.length() <8 || !password.equals(pass2.getText().toString())){
            Extras.makeText(this, getString(R.string.errorSignUpP));
        }else if(oldP.isEmpty() || !oldP.equals(old_pass)){
            Extras.makeText(this, getString(R.string.old_error));
        }else{
            finish.setEnabled(false);
            AuthCredential credential = EmailAuthProvider.getCredential(user_mail, old_pass);
            Fuser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Fuser.updateEmail(user_m).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Fuser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sites.setUser_name(user_n);
                                                sites.setOldPass(password);
                                                NextStep();
                                            }else{
                                                error();
                                            }
                                        }
                                    });
                                }else{
                                    error();
                                }
                            }
                        });
                    }else{
                        error();
                    }
                }
            });
        }
    }

    public void NextStep() {
        Intent i = new Intent(Update.this, Upload.class);
        i.putExtra("user_name", user_name);
        i.putExtra("TYPE", "UP");
        i.setData(sites.getPhoto_uri());
        startActivity(i);
        finish();
    }

    private void openGallery() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.INTERNAL_CONTENT_URI), 100);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        try {
            imageUri = paramIntent.getData();
            Picasso.with(Update.this)
                    .load(imageUri)
                    .into(image);

            try {
                mProgress.setMessage(getString(R.string.up_message));
                mProgress.setCancelable(false);
                mProgress.show();
                final StorageReference riversRef = mStorageRef.child("images").child(Fuser.getUid());

                UploadTask uploadTask = riversRef.putFile(imageUri);

                final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            mProgress.dismiss();
                            Extras.makeText(Update.this, getString(R.string.login_error));
                        }
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUri = task.getResult();
                            sites.setPhoto_uri(downloadUri);
                            mProgress.dismiss();
                        } else {
                            mProgress.dismiss();
                            Extras.makeText(Update.this, getString(R.string.login_error));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_picture:
                openGallery();
                break;
            case R.id.btn_finish:
                Verify();
                break;
        }
    }

    public void error(){
        Extras.makeText(Update.this, getString(R.string.login_error));
    }
}
