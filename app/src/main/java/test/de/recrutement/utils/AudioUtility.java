package test.de.recrutement.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import test.de.recrutement.background_services.UploadData;
import test.de.recrutement.constant.Constant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rahul Padaliya on 3/9/2017.
 */
public class AudioUtility {

    final static String TAG = AudioUtility.class.getSimpleName();

    //Audio manager
    public static MediaRecorder mediaRecorder ;
    public static MediaPlayer mediaPlayer;
    public static String audioSavePathInDevice;


    /**
     * Start Audio Recording
     */
    public static void startAudioRec(Context context) {

        //Delete All Audio File
        AndroidUtils.deleteAllAudioFile();
        audioSavePathInDevice = getOutputMediaAudioFile().getAbsolutePath();
        MediaRecorderReady();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // Toast.makeText(context, "Recording started", Toast.LENGTH_LONG).show();
    }

    /**
     * Stop Audio Recording
     */
    public static void stopAudioRec(Context context) {

       /* if(mediaPlayer != null){
            mediaPlayer.stop();
           // Toast.makeText(context,"Stop Recording",Toast.LENGTH_LONG).show();
        }*/
        //Toast.makeText(getApplicationContext(), "Recording Completed", Toast.LENGTH_LONG).show();
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            addRecordingToMediaLibrary(context);
        }catch (Exception e){
           e.printStackTrace();
        }

    }

    /**
     * Make Media Recorder Ready
     */
    private static void MediaRecorderReady(){

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(audioSavePathInDevice);
        //mediaRecorder.setAudioChannels(1);
    }

    /**
     * Store Audio In Sd Card
     * @return
     */
    private static File getOutputMediaAudioFile() {

        File mediaStorageDir = new File(Constant.APP_DIR+ Constant.SUB_DIR+Constant.AUDIO_DIR);
       // File mediaStorageDir = new File(Constant.APP_DIR + Constant.SUB_DIR + Constant.IMAGES_DIR);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String path = mediaStorageDir.getPath() + File.separator + timeStamp + ".mp3";
        File tempFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".mp3");

        // initiate media scan and put the new things into the path array to
        // make the scanner aware of the location and the files you want to see
        //MediaScannerConnection.scanFile(context, new String[] {path.toString()}, null, null);

        return tempFile;
    }

    /**
     * Delete All Audio File From The Audio Directory When Recording Start
     *//*
    private static void deleteAllAudioFile() {
        String path = Constant.AUDIO_PATH;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(files != null && files.length > 0) {
            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++) {
                Log.d("Test Files", "--->FileName:" + files[i].getName());
                files[i].getAbsoluteFile().delete();
                Log.d("getZipFile", "--->getZipFile:" + files[i].getAbsoluteFile());
            }
        }
    }*/

    /**
     * Audio Store In Mp3 Format
     * @param context
     */
    public static void addRecordingToMediaLibrary(Context context) {
        //creating content values of size 4
        ContentValues values = new ContentValues(3);
        long current = System.currentTimeMillis();
        //values.put(MediaStore.Audio.Media.TITLE, "audio" + getOutputMediaAudioFile().getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audioSavePathInDevice);

        //creating content resolver and storing it in the external content uri
        ContentResolver contentResolver = context.getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);
    }
}


