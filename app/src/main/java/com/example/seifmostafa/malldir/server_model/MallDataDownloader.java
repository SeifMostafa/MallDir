package com.example.seifmostafa.malldir.server_model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.seifmostafa.malldir.MainActivity;
import com.example.seifmostafa.malldir.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by seifmostafa on 26/10/16.
 */
public class MallDataDownloader extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        Context context;
        FirebaseStorage storage;
        private StorageReference storageRef;
        File localFile ;

    String serverAddr ;
    Integer serverPort ,threadCnt ;

    public MallDataDownloader(final Context C) {
        this.context = C;
        progressDialog = new ProgressDialog(this.context,ProgressDialog.STYLE_SPINNER);
        storage = FirebaseStorage.getInstance();
        localFile = null;
        storageRef = storage.getReferenceFromUrl("gs://malldir-580be.appspot.com/mallpackages/");
    }

    @Override
        protected String doInBackground(final String... params) {
        storageRef.child(params[0]).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            String textinsidefile="";

            @Override
            public void onSuccess(byte[] bytes) {
                Log.i("MallDataDownloader","Downloaded");
                File file = new File(context.getExternalFilesDir(null), params[0]);

                FileOutputStream fos = null;
                try {
                   /* textinsidefile= new String(bytes, "UTF-8");
                    MainActivity.TextInsideFile=textinsidefile;*/
                    fos = new FileOutputStream(file);
                    fos.write(bytes);
                    fos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("MallDataDownloader","Not Downloaded");
                progressDialog.dismiss();

            }

        });
        loadParams();
        saveParamChanges();
        saveParamChangesAsXML();
        return "";
    }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();

        }


        @Override
        protected void onPreExecute() {
            progressDialog.show(this.context,"mall package downloadind","please wait till package downloaded",false,true);
        }
    public static boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // if storage is mounted return true
            Log.v("sTag", "Yes, can write to external storage.");
            return true;
        }
        return false;
    }
    public void loadParams() {

        Properties props = new Properties();
        InputStream is = null;

        // First try loading from the current directory
        try {
            File f = new File("server.properties");
            is = new FileInputStream( f );
        }
        catch ( Exception e ) { is = null; }

        try {
            if ( is == null ) {
                // Try loading from classpath
                is = getClass().getResourceAsStream("server.properties");
            }

            // Try loading properties from the file (if found)
            props.load( is );
        }
        catch ( Exception e ) { }

        String serverAddr = props.getProperty("ServerAddress", "192.168.0.1");
       Integer serverPort = new Integer(props.getProperty("ServerPort", "8080"));
        Integer threadCnt  = new Integer(props.getProperty("ThreadCount", "5"));
    }
    public void saveParamChanges() {
        try {
            Properties props = new Properties();
            props.setProperty("ServerAddress", serverAddr);
            props.setProperty("ServerPort", ""+serverPort);
            props.setProperty("ThreadCount", ""+threadCnt);
            File f = new File("server.properties");
            OutputStream out = new FileOutputStream( f );
            props.store(out, "This is an optional header comment string");
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
    }
    public void saveParamChangesAsXML() {
        try {
            Properties props = new Properties();
            props.setProperty("ServerAddress", serverAddr);
            props.setProperty("ServerPort", ""+serverPort);
            props.setProperty("ThreadCount", ""+threadCnt);
            File f = new File("server.xml");
            OutputStream out = new FileOutputStream( f );
            props.storeToXML(out, "This is an optional header comment string");
        }
        catch (Exception e ) {
            e.printStackTrace();
        }
    }

}

