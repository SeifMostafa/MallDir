package com.example.seifmostafa.malldir.Route;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements GLSurfaceView.Renderer {
                private Triangle mCube ;
    private Context context;
    private float mCubeRotation;
    int currentTextureFilter = 0;  // Texture filter (NEW)
    // For controlling cube's z-position, x and y angles and speeds (NEW)
    float angleX = 0;   // (NEW)
    float angleY = 0;   // (NEW)
    float speedX = 0;   // (NEW)
    float speedY = 0;   // (NEW)
    float z = -6.0f;    // (NEW)
    GL10 Mygl10;
    // Constructor
    public OpenGLRenderer(Context context) {
        this.context = context;
        mCube = new Triangle();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.Mygl10 =gl;
       // gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearColor(0, 0, 0, 0);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glRotatef(mCubeRotation, 1.0f, 1.0f, 1.0f);
        mCube.draw(gl);

        gl.glClearColor(0, 0, 0, 0);

        gl.glLoadIdentity();
     // mCubeRotation -= 0.35f;
        // Update the rotational angle after each refresh (NEW)
        angleX += speedX;  // (NEW)
        angleY += speedY;  // (NEW)
    }

    public void RotateLeft(double R) {
        mCubeRotation -= R;
        Mygl10.glRotatef(mCubeRotation, 1.0f, 1.0f, 1.0f);
        mCube.draw(Mygl10);
        Mygl10.glLoadIdentity();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 35.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glClearColor(0, 0, 0, 0);
    }
}
