package com.example.seifmostafa.malldir;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.seifmostafa.malldir.server_model.MallFactorGetter;
import com.example.seifmostafa.malldir.server_model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends FragmentActivity {

    public static DatabaseReference mDatabase;

    FirebaseAuth firebaseAuth;
    Button Aboutus;
    Button Contactus;
    Boolean NonRequired =false;
    FirebaseUser firebaseUser = null;
    TextView textView;
    public static String TextInsideFile="";
    public static String MallPath="";
    FileInputStream fis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        UserInfoSetup();
        SetupUI();
        MallFactorGetter mallFactorGetter = new MallFactorGetter();
        mallFactorGetter.execute("dandy");
        //ControlView();
      //  SaveNonRequired();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void SetupUI(){
        Aboutus =(Button)findViewById(R.id.button_aboutus);
        Contactus=(Button)findViewById(R.id.button_contactus);
        textView =(TextView)findViewById(R.id.textView_fromfile);
        Aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                MallDataDownloader mallDataDownloader = new MallDataDownloader(MainActivity.this);
//                mallDataDownloader.execute("gnena.xml");

            }
        });
        Contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                readExternalStoragePrivateFile("gnena.xml");
//                AlgorithmClass algorithmClass = new AlgorithmClass();
//
//               // Log.i("Nodes",algorithmClass.toString());
//                //Log.i("FirstNode",algorithmClass.get(1).toString());
//                String XY = String.valueOf(algorithmClass.get(1).getx())+","+algorithmClass.get(1).gety();
//                Log.i("XY1", XY);
//                String XY2 = String.valueOf(algorithmClass.get(2).getx())+","+algorithmClass.get(2).gety();
//                Log.i("XY2", XY2);
//
//                LinkedList<Vertex> path = algorithmClass.getPathById(1,2);
//                Log.i("PATH1",String.valueOf(path.size()));
//
//                for(int i=0;i<path.size();i++){
//                    Vertex vv = path.pop();
//                    MyMapNode MMN = algorithmClass.get(Integer.valueOf(vv.getId()));
//                    Log.i("PATH1",MMN.getx()+","+MMN.gety());
//                }
            startActivity(new Intent(MainActivity.this,RouteActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
       // readExternalStoragePrivateFile("gnena.xml");
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
    void readExternalStoragePrivateFile(String mall) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File f = new File(getExternalFilesDir(null), mall);
        MallPath= getExternalFilesDir(null).getPath()+'/'+mall;
        Log.i("MallPath",MallPath);
        try{
            if (f != null) {
                fis = new FileInputStream(f);
                StringBuilder builder = new StringBuilder();
                int ch;
                while((ch = fis.read()) != -1){
                    builder.append((char)ch);
                }
                TextInsideFile=builder.toString();
                Log.i("TextInsideFile",TextInsideFile);
                //TextInsideFile
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //textView.setText(TextInsideFile);

    }
    void deleteExternalStoragePrivateFile(String mall) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(getExternalFilesDir(null), mall);
        if (file != null) {
            file.delete();
        }
    }

    boolean hasExternalStoragePrivateFile(String mall) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(getExternalFilesDir(null), mall);
        if (file != null) {
            return file.exists();
        }
        return false;
    }

    // for non req. user info
    void createExternalStoragePrivatePicture() {
//        // Create a path where we will place our picture in our own private
//        // pictures directory.  Note that we don't really need to place a
//        // picture in DIRECTORY_PICTURES, since the media scanner will see
//        // all media in these directories; this may be useful with other
//        // media types such as DIRECTORY_MUSIC however to help it classify
//        // your media for display to the user.
//        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File file = new File(path, "DemoPicture.jpg");
//
//        try {
//            // Very simple code to copy a picture from the application's
//            // resource into the external file.  Note that this code does
//            // no error checking, and assumes the picture is small (does not
//            // try to copy it in chunks).  Note that if external storage is
//            // not currently mounted this will silently fail.
//            InputStream is = getResources().openRawResource(R.drawable.balloons);
//            OutputStream os = new FileOutputStream(file);
//            byte[] data = new byte[is.available()];
//            is.read(data);
//            os.write(data);
//            is.close();
//            os.close();
//
//            // Tell the media scanner about the new file so that it is
//            // immediately available to the user.
//            MediaScannerConnection.scanFile(this,
//                    new String[] { file.toString() }, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        public void onScanCompleted(String path, Uri uri) {
//                            Log.i("ExternalStorage", "Scanned " + path + ":");
//                            Log.i("ExternalStorage", "-> uri=" + uri);
//                        }
//                    });
//        } catch (IOException e) {
//            // Unable to create file, likely because external storage is
//            // not currently mounted.
//            Log.w("ExternalStorage", "Error writing " + file, e);
//        }
    }

    void deleteExternalStoragePrivatePicture(String pic) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, pic);
            file.delete();
        }
    }

    boolean hasExternalStoragePrivatePicture(String pic) {
        // Create a path where we will place our picture in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        File path = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (path != null) {
            File file = new File(path, pic);
            return file.exists();
        }
        return false;
    }

}
