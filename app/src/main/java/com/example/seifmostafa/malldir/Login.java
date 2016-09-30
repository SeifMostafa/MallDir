package com.example.seifmostafa.malldir;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class Login extends Activity {
    private SharedPreferences mSharedPreferences;
    EditText editText_fullname;
    EditText editText_mail;
    EditText editText_phonenum;
    EditText editText_password;
    EditText editText_login_password;
    EditText editText_login_mail;

    private static final String TAG = "Login";
    public static final String ANONYMOUS = "anonymous";

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    public  static String mail = "seifmostafa235@gmail.com" , password="123456";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithCredential", task.getException());
                    Toast.makeText(Login.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Login.this, "Authentication successful.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,MainActivity.class));
                }

            }
        });
    }

    public void onForgetPassword(View view) {
        Toast.makeText(this, "Mail is sent for Password Reset", Toast.LENGTH_LONG).show();
    }

    public void signup(View view) {
        setContentView(R.layout.layout_signup);
    }
    public void login(View view){
       // Toast.makeText(this, "Hello, Login Button..", Toast.LENGTH_SHORT).show();
        editText_login_password = (EditText)findViewById(R.id.editText_login_password);
        editText_login_mail = (EditText)findViewById(R.id.editText_login_mail);
        String url = "http://www.google.com";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void signupsignup(View view) {
        editText_password = (EditText) findViewById(R.id.editText_signup_password);
        editText_mail = (EditText) findViewById(R.id.editText_signup_mail);
        editText_fullname = (EditText) findViewById(R.id.editText_signup_name);
        editText_phonenum = (EditText) findViewById(R.id.editText_signup_phonenum);
        String mail = editText_mail.getText().toString(), password = editText_password.getText().toString();

    }
}
