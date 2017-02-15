package com.example.seifmostafa.malldir;


import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.example.seifmostafa.malldir.Route.MyGlSurfaceView;

public class RouteActivity extends Activity implements SurfaceHolder.Callback{

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean cameraview = false;
    LayoutInflater inflater = null;
    MyGlSurfaceView arrow;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.cameraPreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        arrow = new MyGlSurfaceView(this);
        arrow.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        LayoutParams layoutParamsControl= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        addContentView(arrow,layoutParamsControl);

//        inflater = LayoutInflater.from(getBaseContext());
//        View view = inflater.inflate(R.layout.layout_overlay, null);
        //   LayoutParams layoutParamsControl= new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

//        this.addContentView(view, layoutParamsControl);


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

        // TODO Auto-generated method stub
        if(cameraview){
            camera.stopPreview();
            cameraview = false;
        }

        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                cameraview = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera = Camera.open();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        camera.stopPreview();
        camera.release();
        camera = null;
        cameraview = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        (arrow.getParent()).requestLayout();
        ((View)arrow.getParent()).invalidate();

    }

}