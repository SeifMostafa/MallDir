package com.example.seifmostafa.malldir;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.seifmostafa.malldir.MainActivity;
import com.example.seifmostafa.malldir.R;
import com.example.seifmostafa.malldir.Route.GPSTracker;
import com.example.seifmostafa.malldir.Route.MyGlSurfaceView;
import com.example.seifmostafa.malldir.Route.POINT;

public class RouteActivity extends Activity implements SurfaceHolder.Callback{

    GLSurfaceView glSurfaceView;
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        // draw
        FrameLayout arrowPreview = (FrameLayout) findViewById(R.id.arrowPreview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
         glSurfaceView = new MyGlSurfaceView(this);
         glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            arrowPreview.addView(glSurfaceView);

       surfaceView = (SurfaceView) findViewById(R.id.cameraPreview);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
          arrowPreview.bringToFront();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ActionMeet(null);

    }
    void ActionMeet(View view) {
       /* boolean b=false;
        POINT current = getCurrentLocation(MainActivity.this);
        for(POINT P:RouteActivity)
        {
            if(POINT.getDiff(P,current).lat<0.0001 &&POINT.getDiff(P,current).lon<0.0001)
            {
                Log.i("DIFF",String.valueOf(POINT.getDiff(P,current).lon));
                Toast.makeText(MainActivity.this,"here"+current.toString(),Toast.LENGTH_SHORT).show();
                b=true;
            }
        }
        if(!b)
        {
            Toast.makeText(MainActivity.this,"uncovered area",Toast.LENGTH_SHORT).show();
        }*/
        // Toast.makeText(MainActivity.this,getCurrentLocation(MainActivity.this).toString(),Toast.LENGTH_SHORT).show();

    }
    public static POINT getCurrentLocation(Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);
        POINT point = new POINT(gpsTracker.getLatitude(),gpsTracker.getLongitude(),gpsTracker.getAltitude());
        gpsTracker.onDestroy();
        return point;
    }
    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        }

        catch (RuntimeException e) {
            System.err.println(e);
            return;
        }

        Camera.Parameters param;
        param = camera.getParameters();
        param.setPreviewSize(352, 288);
        camera.setParameters(param);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }

        catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
