package com.example.seifmostafa.malldir.server_model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by seifmostafa on 26/10/16.
 */
public class MallDataDownloader extends AsyncTask<String, String, String>{

        private String resp;
        ProgressDialog progressDialog;
        Context context;
        FirebaseStorage storage;
        private StorageReference storageRef;
         int time;
        File localFile ;


    public MallDataDownloader(final Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(this.context);
        storage = FirebaseStorage.getInstance();
        localFile = null;
        storageRef = storage.getReferenceFromUrl("gs://malldir-580be.appspot.com/mallpackages/");
    }

    @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
                StorageReference fileRef = storageRef.child(params[0]);
//                fileRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
//                    @Override
//                    public void onSuccess(StorageMetadata storageMetadata) {
//                     long size =  storageMetadata.getSizeBytes();
//                        Toast.makeText(context,String.valueOf(size),Toast.LENGTH_LONG).show();
//                       time = (int) (size);
//                        Log.i("HOLDTIME",String.valueOf(time));
//                        try {
//                            Thread.sleep(time);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

                try {
                    localFile = File.createTempFile("mallpackages", null, context.getCacheDir());
                } catch (IOException e) {
                    e.printStackTrace();
                }

             //   final File finalLocalFile = localFile;
                fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Log.i("MallDataDownloader","MALL DOWNLOADED");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.i("MallDataDownloader","MALL NOT_DOWNLOADED");
                    }
                });
                resp = "Slept for " + time+ " seconds";
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }
        @Override
        protected void onProgressUpdate(String... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }

