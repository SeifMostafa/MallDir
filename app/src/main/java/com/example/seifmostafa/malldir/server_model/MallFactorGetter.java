package com.example.seifmostafa.malldir.server_model;

import android.os.AsyncTask;
import android.util.Log;

import com.example.seifmostafa.malldir.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by seifmostafa on 26/11/16.
 */
public class MallFactorGetter extends AsyncTask<String, Void, Void> {
    String factor="";
    long f;
    private DatabaseReference mallfactors;

    @Override
    protected Void doInBackground(final String... params) {

        mallfactors= MainActivity.mDatabase.child("mallfactors").child(params[0]);
        mallfactors.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                factor= String.valueOf(dataSnapshot.getValue());
                Log.i("FACTORVALUE",factor);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return null;
    }


}

