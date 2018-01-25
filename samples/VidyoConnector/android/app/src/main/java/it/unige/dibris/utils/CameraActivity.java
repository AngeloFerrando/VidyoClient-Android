package it.unige.dibris.utils;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.vidyo.vidyoconnector.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.logging.Handler;

public class CameraActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;

    private File myFilesDir;

    private Uri videoURI;
    private Uri photoURI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        myFilesDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmps/");
        myFilesDir.mkdirs();
    }


    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", new File(myFilesDir.toString()+"/temp.jpg"));
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);//Uri.fromFile(new File(myFilesDir.toString()+"/temp.jpg")));
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Gets the last image id from the media store
     * @return
     */
    private int getLastImageId() {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        Cursor imageCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, null, null, imageOrderBy);
        if (imageCursor.moveToFirst()) {
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //new File(fullPath).delete();
            imageCursor.close();
            return id;
        } else {
            return 0;
        }
    }

    private void removeImage(int id) {
        ContentResolver cr = getContentResolver();
        cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{ Long.toString(id) } );
    }

    private int getLastVideoId(){
        final String[] videoColumns = { MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA };
        final String videoOrderBy = MediaStore.Video.Media._ID+" DESC";
        Cursor videoCursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoColumns, null, null, videoOrderBy);
        if(videoCursor.moveToFirst()){
            int id = videoCursor.getInt(videoCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String fullPath = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            //new File(fullPath).delete();
            videoCursor.close();
            return id;
        }else{
            return 0;
        }
    }

    private void removeVideo(int id) {
        ContentResolver cr = getContentResolver();
        cr.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media._ID + "=?", new String[]{ Long.toString(id) } );
    }

    private void cleanPhotosAndVideos(){
        if (myFilesDir.isDirectory())
        {
            String[] children = myFilesDir.list();
            for (int i = 0; i < children.length; i++)
            {
                System.out.println(new File(myFilesDir, children[i]).delete());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                Bitmap cameraBitmap;
                cameraBitmap = BitmapFactory.decodeFile(myFilesDir + "/temp.jpg");
                Bitmap.createBitmap(cameraBitmap);
                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(cameraBitmap);
                /*Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ((ImageView)findViewById(R.id.imageView)).setImageBitmap(imageBitmap);*/
                removeImage(getLastImageId());
                new File(photoURI.getPath()).delete();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            //Uri videoUri = data.getData();
            ((VideoView)findViewById(R.id.videoView)).setVideoURI(videoURI);
            ((VideoView)findViewById(R.id.videoView)).start();
            removeVideo(getLastVideoId());
            new File(videoURI.getPath()).delete();
        }
        //cleanPhotosAndVideos();
        //removeImage(getLastImageId());
    }

    public void dispatchTakeVideoIntent(View view) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        videoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", new File(myFilesDir.toString()+"/temp.mp4"));
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);//Uri.fromFile(new File(myFilesDir.toString()+"/temp.jpg")));
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

}
