package com.foxware.sites;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity{

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    //Google
    public static  final int SIGN_IN_CODE = 777;
    GoogleSignInClient mGoogleSignInClient;

    Button login_button;
    EditText User, Password;
    Sites sites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        sites = new Sites(Login.this);
        User = (EditText) findViewById(R.id.textUser);
        Password = (EditText) findViewById(R.id.textPassword);
        login_button = findViewById(R.id.start_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
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

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
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

    public void NextStep(){
        Intent i = new Intent(Login.this, Home.class);
        finish();
        startActivity(i);
    }

    public void Register(View v){
        Intent i = new Intent(Login.this, SignUp.class);
        startActivity(i);
        finish();
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
                        Extras.makeText(Login.this, getString(R.string.login_error));
        }
}
});
}

    private void verify(){
        String user_name = User.getText().toString();
        String password = Password.getText().toString();

        if(!user_name.isEmpty() && !password.isEmpty()){
            login(user_name, password);
            login_button.setEnabled(false);
        }else{
            Extras.makeText(Login.this, getString(R.string.error_empty));
        }
    }

    private void login(String email, final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sites.setOldPass(password);
                          datos();
                        } else {
                            login_button.setEnabled(true);
                            Extras.makeText(Login.this, getString(R.string.login_error2));
                        }
                    }
                });
    }

    public void datos(){
        Intent i = new Intent(Login.this, Upload.class);
        i.putExtra("TYPE", "LOGIN" );
        finish();
        startActivity(i);
    }
}
