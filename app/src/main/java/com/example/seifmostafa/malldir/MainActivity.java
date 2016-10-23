package com.example.seifmostafa.malldir;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seifmostafa.malldir.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends FragmentActivity {

    private DatabaseReference mDatabase;
    FirebaseStorage storage;
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
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://malldir-580be.appspot.com/mallpackages/");
       StorageReference  fileRef = storageRef.child("dandy.txt");
        //StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/malldir-580be.appspot/o/images%20ok.png");
       // StorageReference islandRef = httpsReference.child("ok.png");
//        final long ONE_MEGABYTE = 1024 * 1024;
//        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Data for "images/island.jpg" is returns, use this as needed
//                Toast.makeText(MainActivity.this,"Downloaded",Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(MainActivity.this,"failure",Toast.LENGTH_LONG).show();
//
//            }
//        });
        File localFile = null;
        try {
            localFile = File.createTempFile("mallpackages", "txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File finalLocalFile = localFile;
        fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Toast.makeText(MainActivity.this,"Downloaded",Toast.LENGTH_LONG).show();
                try {
                    setTextFromFile(finalLocalFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(MainActivity.this,"failure",Toast.LENGTH_LONG).show();
            }
        });
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
