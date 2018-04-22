package test.de.recrutement.service;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import test.de.recrutement.R;
import test.de.recrutement.activities.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Rahul Padaliya on 3/11/2017.
 */
public class SendImageService extends APIService {
    public static String TAG = MainActivity.class.getName();
    private static final String IMAGE_ZIP_URL = BASE_URL + "/uploadimagezip";

    public static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;
    public  static int id = 2;



    public static void sendImageZipFile(final Context context, HashMap<String,Object> params, final Success<JSONObject> successListener) {

        mNotifyManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getString(R.string.notification_mbuilder_title_text))
                .setContentText(context.getString(R.string.notification_mbuilder_contenttext))
                .setSmallIcon(R.drawable.ic_file_upload_black_24dp);
        mBuilder.setProgress(0,0,true);

        // final Dialog dialog = DialogUtility.processDialog(context, "Please wait...", false);
       /* Map<String, Object> params = new HashMap<>();
        params.put("step", "2");*/
        logDebug("url >> " + IMAGE_ZIP_URL);
        MultipartRequest sendImage = new MultipartRequest(IMAGE_ZIP_URL,context, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                       //dialog.hide();
                        mBuilder.setContentText(context.getString(R.string.Image_uploaded_sucessfully));
                        // Removes the progress bar
                        mBuilder.setProgress(100,100, false);
                        mNotifyManager.notify(id, mBuilder.build());
                        successListener.onSuccess(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //dialog.hide();
                        handleError(context, error);
                    }
                });

        sendImage.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        APIController.getInstance(context).addRequest(sendImage, TAG);

    }

    private static void logDebug(String message) {
        Log.d(TAG, message);
    }
}


