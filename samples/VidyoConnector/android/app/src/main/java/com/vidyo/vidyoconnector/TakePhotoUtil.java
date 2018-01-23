package com.vidyo.vidyoconnector;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by angeloferrando on 16/01/2018.
 */

public class TakePhotoUtil {

    private Camera camera;
    private Context context;
    private int cameraInfo;

    public TakePhotoUtil(Context context, int cameraInfo){
        this.context = context;
        this.cameraInfo = cameraInfo;
        //safeCameraOpen();
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpeg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }

    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        context.sendBroadcast(mediaScanIntent);
    }

    private boolean safeCameraOpen() {
        boolean qOpened = false;

        try {
            releaseCameraAndPreview();
            openCamera();
            SurfaceTexture st = new SurfaceTexture(context.MODE_PRIVATE);
            try {
                camera.setPreviewTexture(st);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resetCamera();
            qOpened = (camera != null);
        } catch (Exception e) {
            Log.e(context.getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }

        return qOpened;
    }

    private void resetCamera(){
        camera.startPreview();
    }

    public void takePicture(){
        safeCameraOpen();

        camera.takePicture(null, null, new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                new SaveImageTask().execute(data);
                resetCamera();
                releaseCameraAndPreview();
            }
        });
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void openCamera(){
        int numCams = Camera.getNumberOfCameras();
        for(int i = 0; i < numCams; i++){
            Camera.CameraInfo info =
                    new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraInfo){
                camera = Camera.open(i);
                return;
            }
        }
        throw new RuntimeException();
    }
}
