package test.de.recrutement.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;


import org.json.JSONObject;

import test.de.recrutement.R;

/**
 * Created by Ankur on 24-11-2015.
 */
public abstract class APIService {

    private static final String TAG = APIService.class.getSimpleName();

     // public static final String BASE_URL = "http://192.168.1.27/questionnaire/public/api"; //Local URL
     //public  static final String BASE_URL = "http://103.59.59.166:89/questionnaire/public/api"; //webs
     //public  static final String BASE_URL = "https://questionnairesapi.voicedigit.com/public/api"; //for Client
     public static final String BASE_URL = "http://local.websoptimization.com:89/questionnaire/public/api";
     //public  static final String BASE_URL = "http://192.168.1.43/questionnaire/public/api/questionnaire";
      //public static final String BASE_URL = "http://192.168.1.43/questionnaire/public/api";



    public interface Success<T> {
        public void onSuccess(T response);
    }

    public interface Error<T>
    {
        public void onError(T error);
    }

    protected static void handleError(final Context context, VolleyError error) {
        Log.d(TAG, "Error :: " + error);

        if (error instanceof NoConnectionError) {

            Toast.makeText(context, context.getString(R.string.internet_connection), Toast.LENGTH_LONG).show();

        } else if (error.networkResponse != null) {

            Log.d(TAG, "CODE =" + error.networkResponse.statusCode);
            Log.d(TAG, new String(error.networkResponse.data));

            if (error.networkResponse.statusCode == 401) {
                Toast.makeText(context,context.getString(R.string.Error_401),Toast.LENGTH_LONG).show();

            } else {
                //Read Error Response and Display Error Dialog
                //DialogUtility.(context, getErrorMessage(error.networkResponse.data));
                Toast.makeText(context,getErrorMessage(error.networkResponse.data,context),Toast.LENGTH_LONG).show();
            }
        }else{
            Log.d(TAG,"Server is not responding");
            Toast.makeText(context,context.getString(R.string.Server_is_not_responding),Toast.LENGTH_LONG).show();
        }
    }

    private static String getErrorMessage(byte[] responseData,Context context) {
        String message = null;
        String code = null;
        try {
            JSONObject jsonObject = new JSONObject(new String(responseData));
            JSONObject subJsonObj = jsonObject.getJSONObject("error");
            code = subJsonObj.getString("code");
            message = subJsonObj.getString("message");
        } catch (Exception e) {
            Log.d(TAG, "getErrorMessage" + e.getMessage());
            message = context.getString(R.string.something_wrong);
        }
        return message;
    }

}
