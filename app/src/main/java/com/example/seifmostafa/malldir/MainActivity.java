package com.example.seifmostafa.malldir;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.seifmostafa.malldir.server_model.MallDataDownloader;
import com.example.seifmostafa.malldir.server_model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends FragmentActivity {

    private DatabaseReference mDatabase;

    FirebaseAuth firebaseAuth;
    Button Aboutus;
    Button Contactus;
    Boolean NonRequired =false;
    FirebaseUser firebaseUser = null;


    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserInfoSetup();
        //ControlView();
        SetupUI();
        mDatabase = FirebaseDatabase.getInstance().getReference();


      //  SaveNonRequired();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void SetupUI(){
        Aboutus =(Button)findViewById(R.id.button_aboutus);
        Contactus=(Button)findViewById(R.id.button_contactus);
        Aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MallDataDownloader mallDataDownloader = new MallDataDownloader(MainActivity.this);
                mallDataDownloader.execute("gnena.xml");
            }
        });
    }
    public void setTextFromFile(File filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            TextView textView = (TextView)findViewById(R.id.textView_fromfile);
            textView.setText(everything);
        } finally {
            br.close();
        }

    }
    public void ControlView(){
        if(NonRequired==false){
          Toast.makeText(this,"soon non required",Toast.LENGTH_SHORT).show();
        }
        else {
        }
    }
    public void SaveNonRequired(){
        writeNewUser("seifmostafa",firebaseUser.getEmail(),"Sport","En","dandy",firebaseUser.getUid());
    }
    public void UserInfoSetup(){
        textView =(TextView)findViewById(R.id.textView_hello);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        textView.setText((CharSequence) firebaseUser.getUid());
    }
    private void writeNewUser(String username, String email, String interests, String lang, String mall, String id) {
        User user = new User(username,email,interests,lang,mall,id);

        mDatabase.child("users").child(user.getId()).setValue(user);
        mDatabase.child("users").child(user.getId()).child("username").setValue(user.getUsername());
        mDatabase.child("users").child(user.getId()).child("email").setValue(user.getEmail());
        mDatabase.child("users").child(user.getId()).child("interests").setValue(user.getInterests());
        mDatabase.child("users").child(user.getId()).child("mall").setValue(user.getMall());
    }


    public void downloadfile(String mallname){}

}
