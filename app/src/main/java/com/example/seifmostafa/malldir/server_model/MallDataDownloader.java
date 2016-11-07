package com.example.seifmostafa.malldir.server_model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by seifmostafa on 26/10/16.
 */
public class MallDataDownloader extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        Context context;
        FirebaseStorage storage;
        private StorageReference storageRef;
        File localFile ;


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
            @Override
            public void onSuccess(byte[] bytes) {
                // Use the bytes to display the image
                Log.i("MallDataDownloader","Downloaded");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream("assets/"+params.toString());
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
        return "";
    }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation

        }


        @Override
        protected void onPreExecute() {
            ProgressDialog.show(this.context,"mall package downloadind","please wait till package downloaded",false,false);
        }
    }

