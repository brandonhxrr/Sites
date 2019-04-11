package com.foxware.sites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    EditText UserName, Password, Password2, Mail;
    Sites sites;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    //Google
    public static  final int SIGN_IN_CODE = 777;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        sites = new  Sites(SignUp.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, SIGN_IN_CODE);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();

                if(user != null){
                    NextStep();
                }
            }
        };

        UserName = (EditText) findViewById(R.id.UserName);
        Password = (EditText) findViewById(R.id.Password);
        Mail = (EditText) findViewById(R.id.UserMail);
        Password2 = (EditText) findViewById(R.id.RPassword);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("Firebase Google", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            sites.setUser_name(user.getDisplayName());
                            sites.setPhoto_uri(user.getPhotoUrl());
                            sites.setType("Google");
                            NextStep();
                        } else {
                            Extras.makeText(SignUp.this, getString(R.string.login_error));
                        }
                    }
                });
    }

    public void NextStep(){
        Intent i = new Intent(SignUp.this, Home.class);
        finish();
        startActivity(i);
    }

    public void Verify(View view){
        Pattern pattern= Patterns.EMAIL_ADDRESS;
        String user_name = UserName.getText().toString();
        String mail = Mail.getText().toString();
        String password = Password.getText().toString();
        if(user_name.isEmpty()){
            Extras.makeText(this, getString(R.string.errorUser));
        }else if(mail.isEmpty() || !pattern.matcher(mail).matches()){
            Extras.makeText(this, getString(R.string.errorMail));
        } else if(password.isEmpty() || password.length()<8 || !password.equals(Password2.getText().toString())){
            Extras.makeText(this, getString(R.string.errorSignUpP));
        }else{
            view.setEnabled(false);
           signUp(user_name, mail,password);
        }
    }

    private void signUp(final String userx, final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sites.setOldPass(password);
                           Open(userx, email);
                        } else {

                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Extras.makeText(SignUp.this,getString(R.string.errorSignUpP3));
                            }else{
                                Extras.makeText(SignUp.this,getString(R.string.login_error));
                            }
                        }
                    }
                });
    }

    void Open(String name, String email){

        Intent i = new Intent(this, SignUp2.class);
        i.putExtra("user_name",name );
        i.putExtra("user_email", email);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }
}
