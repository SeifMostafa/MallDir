package com.example.seifmostafa.malldir;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.seifmostafa.malldir.Route.GPSTracker;
import com.example.seifmostafa.malldir.Route.Guide;
import com.example.seifmostafa.malldir.Route.POINT;
import com.example.seifmostafa.malldir.Route.Translator;
import com.example.seifmostafa.malldir.algorithm.AlgorithmClass;
import com.example.seifmostafa.malldir.algorithm.Vertex;

import com.example.seifmostafa.malldir.data.XReader;
import com.example.seifmostafa.malldir.data.XmlQuerys;
import com.example.seifmostafa.malldir.model.Floor;
import com.example.seifmostafa.malldir.model.MyMapNode;
import com.example.seifmostafa.malldir.server_model.MallDataDownloader;
import com.example.seifmostafa.malldir.server_model.MallFactorGetter;
import com.example.seifmostafa.malldir.server_model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends FragmentActivity {

    public static DatabaseReference mDatabase;
    public static String Mall = "logica.xml";
    public static final POINT Start_GPS = new POINT(31.2002523, 30.0407407,0);
    public static final POINT End_GPS = new POINT(31.2009232, 30.0400363,0);
    public static final POINT Start_Mall = new POINT(3260.1980198019805, 528.9149560117302,0);
    public static final POINT End_Mall = new POINT(1042.376237623762,782.2873900293255,0);
    public static Translator translator;
    FirebaseAuth firebaseAuth;
    Button Aboutus;
    Button Contactus;
    Boolean NonRequired = false;
    FirebaseUser firebaseUser = null;
    TextView textView;
    Spinner spinner_src, spinner_dest, spinner_floors;

    ArrayList<Floor> floors;
    List<String> list_floors;
    List<String> list_nodes_names;
    List<MyMapNode> list_nodes;

    public static String TextInsideFile = "";
    public static String MallPath = "";
    public static String MallFactor = "";
    FileInputStream fis;
    MallDataDownloader mallDataDownloader;

    AlgorithmClass algorithmClass;
    XmlQuerys xmlQuerys;
    ArrayList<ArrayList<MyMapNode>> nodes_by_floor;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    GPSTracker gpsTracker;
    TextView textView_gps_info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("MainActivity", Context.MODE_PRIVATE);

        translator= new Translator(Start_GPS.getLon(),Start_GPS.getLat(),
        End_GPS.getLon(),End_GPS.getLat(),
        Start_Mall.getLon(),Start_Mall.getLat(),
        End_Mall.getLon(),End_Mall.getLat());

        Log.i("translator",""+translator.factor_x+","+translator.factor_y);

        editor = sharedPreferences.edit();

        if(sharedPreferences.getString("MallPath","").equals("")){
            try {
                mallDataDownloader = new MallDataDownloader(MainActivity.this,Mall);
                SaveOnSharedPref("MallPath",MallPath);
            } catch (Exception e){
                Log.i("MallDataDownloader",e.toString());
            }
       }else Log.i("MallPathExists:",sharedPreferences.getString("MallPath",""));
        // readExternalStoragePrivateFile(Mall);


        if(sharedPreferences.getString("MallFactor","").equals("")){
            try {
                MallFactorGetter mallFactorGetter = new MallFactorGetter();
                mallFactorGetter.execute("logica").get();
                SaveOnSharedPref("MallFactor",MallFactor);
            } catch (Exception e){
                Log.i("MallFactorDownloader",e.toString());
            }
        }else Log.i("MallFactorExists:",sharedPreferences.getString("MallFactor",""));
        Log.i("MallPath", MallPath);

        SetupUI();

        UserInfoSetup();
        openHTMLpages();

       algorithmClass = new AlgorithmClass();


        setupData();
        fillSpinners();
        ControlView();
          SaveNonRequired();
         gpsTracker = new GPSTracker(this);
        gpsTracker.getLocation();
        Log.i("GPSTracker",""+gpsTracker.getLongitude()+","+gpsTracker.getLatitude());
    }
    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("logica.xml");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }
        return null;
    }
    public void setupData() {
        floors = new XReader().getFloors();
        Log.i("setupData+++", "" + floors.size());
        nodes_by_floor = new ArrayList<ArrayList<MyMapNode>>();
        for (Floor floor : floors) {
            nodes_by_floor.add(new XReader().getNodesByFloor(floor.getId()));
        }
        list_floors = new ArrayList<String>();
        list_nodes = new ArrayList<MyMapNode>();
        list_nodes_names = new ArrayList<String>();

        for (int i = 0; i < nodes_by_floor.size(); i++) {
            ArrayList<MyMapNode> myMapNodes = nodes_by_floor.get(i);
            list_floors.add("" + (i + 1));
            for (MyMapNode node : myMapNodes) {
                list_nodes.add(node);
                list_nodes_names.add(node.getName());
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void fillSpinners() {

        ArrayAdapter<String> floorsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_floors);
        floorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_floors.setAdapter(floorsAdapter);

//        ArrayAdapter<MyMapNode> srcNodes = new ArrayAdapter<MyMapNode>(this, android.R.layout.simple_spinner_item,list_nodes);
//        srcNodes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_src.setAdapter(srcNodes);

        ArrayAdapter<String> nodesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_nodes_names);
        nodesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_src.setAdapter(nodesAdapter);
        spinner_dest.setAdapter(nodesAdapter);

    }

    public void SetupUI() {
        Aboutus = (Button) findViewById(R.id.button_aboutus);
        Contactus = (Button) findViewById(R.id.button_contactus);
        spinner_src = (Spinner) findViewById(R.id.spinner_src);
        spinner_dest = (Spinner) findViewById(R.id.spinner_dest);
        spinner_floors = (Spinner) findViewById(R.id.spinner_floors);
        textView_gps_info = (TextView)findViewById(R.id.textView_gpsinfo);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public String setTextFromFile(String filename) throws IOException {
        String everything="";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
             everything = sb.toString();
        } finally {
            br.close();
        }
        return everything;
    }

    public void ControlView() {
        if (NonRequired == false) {
            Toast.makeText(this, "soon non required", Toast.LENGTH_SHORT).show();
        } else {
        }

//        spinner_src.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                spinner_dest.removeViewAt(position);
//            }
//        });


    }

    public void SaveNonRequired() {
        writeNewUser("seifmostafa", firebaseUser.getEmail(), "Sport", "En", "dandy", firebaseUser.getUid());
    }

    public void UserInfoSetup() {
        textView = (TextView) findViewById(R.id.textView_hello);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.i("UserInfoSetup", firebaseUser.getUid());
    }

    private void writeNewUser(String username, String email, String interests, String lang, String mall, String id) {
        User user = new User(username, email, interests, lang, mall, id);

        mDatabase.child("users").child(user.getId()).setValue(user);
        mDatabase.child("users").child(user.getId()).child("username").setValue(user.getUsername());
        mDatabase.child("users").child(user.getId()).child("email").setValue(user.getEmail());
        mDatabase.child("users").child(user.getId()).child("interests").setValue(user.getInterests());
        mDatabase.child("users").child(user.getId()).child("mall").setValue(user.getMall());
    }


    void readExternalStoragePrivateFile(String mall) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        Log.i("MallPath", MallPath);

//        File f = new File(mallDataDownloader.localFile.getAbsolutePath());
        //MallPath = f.getPath();
//        try{
//            if (f != null) {
//                fis = new FileInputStream(f);
//                StringBuilder builder = new StringBuilder();
//                int ch;
//                while((ch = fis.read()) != -1){
//                    builder.append((char)ch);
//                }
//                TextInsideFile=builder.toString();
//                Log.i("TextInsideFile",TextInsideFile);
//                //TextInsideFile
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
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

    private void openHTMLpages() {
        Contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.logica-itech.com/contact%20us.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        Aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.logica-itech.com/contact%20us.html";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void routeme(View view) {
        MyMapNode start = list_nodes.get(spinner_src.getSelectedItemPosition());
        MyMapNode dest = list_nodes.get(spinner_dest.getSelectedItemPosition());
       Guide guide = new Guide(new POINT(new GPSTracker(this).getLongitude(),new GPSTracker(this).getLatitude(),0.0),dest,this,new AlgorithmClass());
        //Guide guide = new Guide(start,dest,this,new AlgorithmClass());

        // LinkedList<Vertex> path = algorithmClass.getPathById(start.getId(), dest.getId());
        ArrayList<MyMapNode> path = new ArrayList<>();
        path = guide.getPath(1,true);
     //   Log.i("GPSTracker",""+gpsTracker.getLongitude()+","+gpsTracker.getLatitude());
        String Path = "You are in : "+ path.get(0).getName()+" & you are going to :"+path.get(path.size()-1).getName() +"\n using the path as following: ";
        for (MyMapNode node : path) {
           Path+=node.getName();
            Path+=" , ";
            //+ ", " + node.getId() +"       -       " +new XReader().getMapNode(node.getId()).getx()+","+new XReader().getMapNode((node.getId())).gety());
        }
        textView_gps_info.setText(Path);
    }

    public void SaveOnSharedPref(String key, String value) {
        editor.putString(key, value).apply();
        editor.commit();
    }
    public String writefile(String fileName,String content){

        File file;
        FileOutputStream outputStream;
        try {
            // file = File.createTempFile("MyCache", null, getCacheDir());
            file = new File(getCacheDir(), fileName);

            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

