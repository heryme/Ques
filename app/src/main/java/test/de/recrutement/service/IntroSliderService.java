package test.de.recrutement.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import test.de.recrutement.utils.UserSession;

import static test.de.recrutement.R.string.device_id;
import static test.de.recrutement.utils.UserSession.DEVICE_ID;

/**
 * Created by Rahul Padaliya on 9/11/2017.
 */

public class IntroSliderService extends APIService {
    private static final String TAG = IntroSliderService.class.getSimpleName();
    static  UserSession userSession;
    private static final String URL_GET_SCREEN = BASE_URL + "/screen/getScreens";
    // private static final String DEVICE_ID = "device_id";
    //private static final String URL_INTRO_SLIDER = "http://api.androidhive.info/contacts/";

    public static void getIntroSlider(final Context context,
                                      final Success<JSONObject>
                                              successListener,
                                      final Error<JSONObject> errorListener) {
        userSession = new UserSession(context);

        //final ProgressDialog dialog = DialogUtility.processDialog(context, "Please Wait", true);
        Log.d(TAG, "Get Intro Slider Response" + URL_GET_SCREEN);
        StringRequest introSliderRequest = new StringRequest(Request.Method.POST, URL_GET_SCREEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.hide();
                    successListener.onSuccess(new JSONObject(response));
                    Log.d(TAG, "Get Intro Slider Response" + response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog.hide();
                try {
                    Log.d(TAG, " Get Intro Slider Response  Error  >>" + error.toString());
                    handleError(context, error);
                    errorListener.onError(new JSONObject(error.toString()));
                } catch (Exception e) {
                    Log.d(TAG, "" + e.getMessage());
                }
            }
        }){
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<String, String>();
               params.put(DEVICE_ID,userSession.getDeviceID());
               return params;
            }
          };
        APIController.getInstance(context).addRequest(introSliderRequest, TAG);
    }
}