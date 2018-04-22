package test.de.recrutement.background_services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import test.de.recrutement.constant.Constant;
import test.de.recrutement.service.APIService;
import test.de.recrutement.service.SendAudioService;
import test.de.recrutement.service.SendZipService;
import test.de.recrutement.utils.AndroidUtils;
import test.de.recrutement.utils.UserSession;

import org.json.JSONObject;

import java.util.HashMap;

import static test.de.recrutement.utils.AndroidUtils.deleteAllFile;

/**
 * Created by Rahul Padaliya on 1/3/2017.
 */
public class UploadData extends Service {
    final static String TAG = UploadData.class.getSimpleName();
    UserSession userSession;

    public NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    int id = 1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        userSession = new UserSession(this);
        //sendImageZIPFile();
        sendZipAPI();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    /**
     * Call Send Image ZIP API
     */
    public void sendImageZIPFile() {
       /* mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Questionnaire").setTicker("")
                .setContentText("Uploading...")
                .setSmallIcon(R.drawable.animation);
        mBuilder.setProgress(100,1,true);*/

        HashMap<String, Object> params = new HashMap();
        params.put("questionnaire_id",userSession.getQuestionnaireID() /*Constant.QUESTIONNAIRE_ID*/);
        params.put("device_id",userSession.getDeviceID()/* SplashActivity.deviceId*/);
        params.put("zip_file",AndroidUtils.getImageZipFile());

        SendZipService.sendZipFile(this, params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Zip  Response-->" + response.toString());
            }
        });

       /* SendImageService.sendImageZipFile(this, params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Image Zip  Response-->" + response.toString());
                 //AndroidUtils.deleteAllFile();
                //Send Audio
                 sendAudioFile();
            }
        });*/
    }

    /**
     * Call Send Audio API
     */
    private void sendAudioFile() {

        HashMap<String, Object> params = new HashMap();
        params.put("questionnaire_id",userSession.getQuestionnaireID()/* Constant.QUESTIONNAIRE_ID*/);
        params.put("device_id",userSession.getDeviceID()/*SplashActivity.deviceId*//*randomNumber()*/);
        params.put("audio_file", AndroidUtils.getAudioFile());

        SendAudioService.sendAudioFile(this, params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Audio Response-->" + response.toString());

              /* mBuilder.setContentText("Upload complete");
                // Removes the progress bar
                mBuilder.setProgress(100,100, false);
                mNotifyManager.notify(id, mBuilder.build());*/

                AndroidUtils.deleteAllAudioFile();
                stopService();
            }
        });
    }

    public void stopService() {
        stopService(new Intent(getBaseContext(), UploadData.class));
    }

    /**
     * Call Resource Upload API For Send Zip Of The Image And Audoio File
     */
    private void sendZipAPI() {

        HashMap<String, Object> params = new HashMap();
        params.put("questionnaire_id",userSession.getQuestionnaireID() /*Constant.QUESTIONNAIRE_ID*/);
        params.put("device_id",userSession.getDeviceID()/* SplashActivity.deviceId*/);
        params.put("zip_file",AndroidUtils.getImageZipFile());

        SendZipService.sendZipFile(this, params, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Zip  Response-->" + response.toString());
                deleteAllFile(Constant.IMAGE_PATH);
                deleteAllFile(Constant.AUDIO_PATH);
                deleteAllFile(Constant.ZIP_PATH);
                stopService();
            }
        });

    }
}

