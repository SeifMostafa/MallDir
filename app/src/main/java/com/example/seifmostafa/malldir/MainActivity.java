package com.example.seifmostafa.malldir;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seifmostafa.malldir.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends FragmentActivity {

    private DatabaseReference mDatabase;
   private StorageReference storageRef;

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
        ControlView();
        SetupUI();
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // storageRef = storage.getReferenceFromUrl("gs://malldir-580be.appspot.com");
        Aboutus =(Button)findViewById(R.id.button_aboutus);
        Contactus=(Button)findViewById(R.id.button_contactus);
        SaveNonRequired();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void SetupUI(){

    }
    public void ControlView(){
        if(NonRequired==false){
          Toast.makeText(this,"soon non required",Toast.LENGTH_SHORT).show();
        }
        else {
        }
    }
    public void SaveNonRequired(){
        // after saving set "Boolean NonRequired =true;"
            // using sharedpref
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
