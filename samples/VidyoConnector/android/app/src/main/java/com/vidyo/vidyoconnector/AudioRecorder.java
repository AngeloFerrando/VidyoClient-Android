package com.vidyo.vidyoconnector;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class AudioRecorder {

    private MediaRecorder recorder;

    private File outfile = null;

    public AudioRecorder(){}

    public void startRecording(String audioFile) throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
            throw new IOException("SD Card is not mounted.  It is " + state + ".");
        }

        try{
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/audio");
            dir.mkdirs();

            outfile=File.createTempFile(audioFile, ".3gp", dir);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(outfile.getAbsolutePath());
        }catch(IOException e){
            e.printStackTrace();
        }

        try{
            recorder.prepare();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }

        try {
            recorder.start();
        } catch(IllegalStateException e){
            Log.e(TAG, "MediaRecorder start failed.");
            recorder.release();
            return;
        }
    }

    public void stop() throws IOException {
        recorder.stop();
        recorder.release();
    }
}