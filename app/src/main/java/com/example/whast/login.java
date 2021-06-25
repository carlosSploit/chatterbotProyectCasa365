package com.example.whast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.whast.View.busondemesseg;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSingIn;
    private FirebaseAuth mAuth;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSingIn = GoogleSignIn.getClient(this,gso);
        mAuth = FirebaseAuth.getInstance();

        Button btnGmail = (Button)findViewById(R.id.loogear);
        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logear = googleSingIn.getSignInIntent();
                startActivityForResult(logear,RC_SIGN_IN);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (accountTask.isSuccessful()){
                try {
                    GoogleSignInAccount account = accountTask.getResult(ApiException.class);
                    Log.d(TAG,"firebaseAuthWithGoogle: "+account.getId());
                    EscuchaSignInResult(account);
                }catch (ApiException e){
                    Log.d(TAG,"Google sign in failed",e);
                }
            }else{
                Log.d(TAG,"Erro, login no exitoso:"+ accountTask.getException().toString());
            }
        }else{
            Log.d(TAG,"Erro, login no exitoso code");
        }
    }

    private void EscuchaSignInResult(GoogleSignInAccount res) {
        AuthCredential credential = GoogleAuthProvider.getCredential(res.getIdToken(),null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG,"signInWithCredential:success - sabe comer pija sapeeeee");
                        Intent dash = new Intent(login.this, busondemesseg.class);
                        startActivity(dash);
                        login.this.finish();
                    }else{
                        Log.d(TAG,"signInWithCredential:failure");
                    }
                }
            });
    }

    @Override
    protected void onStart() {
        FirebaseUser use = mAuth.getCurrentUser();
        if (use!= null){
            Intent dashbi=new Intent(login.this,busondemesseg.class);
            startActivity(dashbi);
        }
        super.onStart();
    }
}