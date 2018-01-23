package com.vidyo.vidyoconnector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MakePhotoActivity extends Activity {
    private static final String TAG = "CamTestActivity";
    //Preview preview;
    //Button buttonClick;
    Camera camera;
    Activity act;
    Context ctx;
    Context videoChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_make_photo);
/*
        preview = new Preview(this, (SurfaceView)findViewById(R.id.surfaceView));
        preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);
*/

        //camera.takePicture(null, null, jpegCallback);

        findViewById(R.id.captureFront).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                camera.takePicture(null, null, jpegCallback);
            }
        });

       // Toast.makeText(ctx, "take photo help", Toast.LENGTH_LONG).show();

        //		buttonClick = (Button) findViewById(R.id.btnCapture);
        //
        //		buttonClick.setOnClickListener(new OnClickListener() {
        //			public void onClick(View v) {
        ////				preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        //				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        //			}
        //		});
        //
        //		buttonClick.setOnLongClickListener(new OnLongClickListener(){
        //			@Override
        //			public boolean onLongClick(View arg0) {
        //				camera.autoFocus(new AutoFocusCallback(){
        //					@Override
        //					public void onAutoFocus(boolean arg0, Camera arg1) {
        //						//camera.takePicture(shutterCallback, rawCallback, jpegCallback);
        //					}
        //				});
        //				return true;
        //			}
        //		});


    }

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            boolean done = false;
            do {
                try {
                    camera = Camera.open(0);

                    SurfaceTexture st = new SurfaceTexture(MODE_PRIVATE);
                    try {
                        camera.setPreviewTexture(st);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    camera.startPreview();
                    done = true;
                    //preview.setCamera(camera);
                } catch (RuntimeException ex) {
                    //Toast.makeText(ctx, "camera not found", Toast.LENGTH_LONG).show();
                }
            } while(!done);
        }
    }

    @Override
    protected void onPause() {
        if(camera != null) {
            camera.stopPreview();
            //preview.setCamera(null);
            camera.release();
            camera = null;
        }
        super.onPause();
    }

    private void resetCam() {
        camera.startPreview();
        //preview.setCamera(camera);
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        public void onShutter() {
            //			 Log.d(TAG, "onShutter'd");
        }
    };

    PictureCallback rawCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            //			 Log.d(TAG, "onPictureTaken - raw");
        }
    };

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            new SaveImageTask().execute(data);
            resetCam();
            Intent intent = new Intent();
            if (getParent() == null) {
                setResult(Activity.RESULT_OK, intent);
            } else {
                getParent().setResult(Activity.RESULT_OK, intent);
            }
            finish();
            Log.d(TAG, "onPictureTaken - jpeg");
        }
    };

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                Display display = getWindowManager().getDefaultDisplay();
                int rotation = 0;
                switch (display.getRotation()) {
                    case Surface.ROTATION_0: // This is display orientation
                        rotation = 90;
                        break;
                    case Surface.ROTATION_90:
                        rotation = 0;
                        break;
                    case Surface.ROTATION_180:
                        rotation = 270;
                        break;
                    case Surface.ROTATION_270:
                        rotation = 180;
                        break;
                }

                Bitmap bmp = BitmapFactory.decodeByteArray(data[0], 0, data[0].length);
                bmp = BitmapTools.rotate(bmp, rotation);

                try {
                    outStream = new FileOutputStream(outFile);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outStream != null) {
                            outStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();*/

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } finally {
            }
            return null;
        }

    }

    public static class BitmapTools {

        public static Bitmap toBitmap(byte[] data) {
            return BitmapFactory.decodeByteArray(data , 0, data.length);
        }

        public static Bitmap rotate(Bitmap in, int angle) {
            Matrix mat = new Matrix();
            mat.postRotate(angle);
            return Bitmap.createBitmap(in, 0, 0, in.getWidth(), in.getHeight(), mat, true);
        }
    }
}