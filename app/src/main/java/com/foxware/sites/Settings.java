package com.foxware.sites;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.irozon.alertview.AlertActionStyle;
import com.irozon.alertview.AlertStyle;
import com.irozon.alertview.AlertView;
import com.irozon.alertview.interfaces.AlertActionListener;
import com.irozon.alertview.objects.AlertAction;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity implements View.OnClickListener{


    TextView user_name, user_mail;
    LinearLayout  contact, edit, about, close;
    CircleImageView user_picture;
    Sites sites;
    ImageView back_btn;

    String user_nameS, user_mailS , user_uidS;
    Uri photo_url;

    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        sites = new Sites (Settings.this);
        String Type = sites.getType();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        user_name = (TextView) findViewById(R.id.user_alias);
        user_mail = (TextView) findViewById(R.id.user_mail);
        contact = findViewById(R.id.contact);
        edit = findViewById(R.id.edit);
        about = findViewById(R.id.instagram);
        close = findViewById(R.id.close);
        back_btn = findViewById(R.id.back);


        user_picture = (CircleImageView) findViewById(R.id.user_picture);

        user_nameS = sites.getUser_name();
        user_mailS = currentUser.getEmail();
        user_uidS = currentUser.getUid();
        photo_url = sites.getPhoto_uri();

        if(Type.equals("Mail")){
            user_picture.setOnClickListener(this);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Update();
                }
            });
        }else{
            edit.setOnClickListener(listener);
        }

        close.setOnClickListener(listener);
        contact.setOnClickListener(listener);
        about.setOnClickListener(listener);
        back_btn.setOnClickListener(listener);

        fill();


    }

    private void fill(){
        Picasso.with(Settings.this).load(photo_url)
                .error(R.mipmap.ic_user)
                .into(user_picture);

        user_name.setText(user_nameS);
        user_mail.setText(user_mailS);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.user_picture){
            photo_selected();
        }else{
            Update();
        }
    }

    public void photo_selected(){

        AlertView alert=new AlertView( getString(R.string.alert_av), getString(R.string.alert_mes), AlertStyle.IOS);
        alert.addAction(new AlertAction(getString(R.string.alert_watch), AlertActionStyle.DEFAULT, new AlertActionListener() {
            @Override
            public void onActionClick(AlertAction alertAction) {

                Uri photoUrl = sites.getPhoto_uri();

                Intent photo = new Intent(Settings.this, PhotoDetail.class);
                photo.setData(photoUrl);
                startActivity(photo);
            }}));
        alert.addAction(new AlertAction(getString(R.string.alert_change), AlertActionStyle.DEFAULT, new AlertActionListener() {
            @Override
            public void onActionClick(AlertAction alertAction) {
                Update();
            }}));

        alert.show(this);
    }



    public void Update(){
        Intent x = new Intent (Settings.this, Update.class);
        startActivity(x);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.contact){
                AlertView alert=new AlertView( getString(R.string.alert_av), getString(R.string.alert_mail), AlertStyle.DIALOG);
                alert.addAction(new AlertAction(getString(R.string.alert_ok), AlertActionStyle.POSITIVE, new AlertActionListener() {
                    @Override
                    public void onActionClick(AlertAction alertAction) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setData(Uri.parse("email"));
                        String[] s = {"brandondh769@gmail.com"};
                        i.putExtra(Intent.EXTRA_EMAIL, s);
                        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
                        i.setType("message/rfc822");
                        Intent chooser=Intent.createChooser(i,getString(R.string.send_mail) );
                        startActivity(chooser);
                    }}));
                alert.addAction(new AlertAction(getString(R.string.alert_cancel), AlertActionStyle.NEGATIVE, new AlertActionListener() {
                    @Override
                    public void onActionClick(AlertAction alertAction) {
                    }}));
                alert.show(Settings.this);
            }else if(v.getId() == R.id.edit){
                Extras.makeText(Settings.this, getString(R.string.notsupported));
            }else if(v.getId() == R.id.instagram){
                Intent xx = new Intent(Settings.this, About.class);
                startActivity(xx);
            }else if(v.getId() == R.id.close){
                FirebaseAuth.getInstance().signOut();
                Intent xx = new Intent(Settings.this, Login.class);
                startActivity(xx);
            }else if(v.getId() == R.id.back){
                finish();
            }
        }
    };
}
