package test.de.recrutement.service;

import android.content.Context;
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

/**
 * Created by Rahul Padaliya on 3/1/2017.
 */
public class SettingService extends APIService {

    private static final String TAG = SettingService.class.getSimpleName();
    private static final String URL_SETTING = BASE_URL + "/getsettings";
    private static final String DEVICE_ID = "device_id";

    public static void getSettings(final Context context,
                                   final String device_id,
                                   final Success<JSONObject> successListener,
                                   final Error<JSONObject> errorListener) {

        //final ProgressDialog dialog = DialogUtility.processDialog(context, "Please Wait", true);

        Log.d(TAG, "Get Setting -->" + URL_SETTING);
        StringRequest overviewRequest = new StringRequest(Request.Method.POST, URL_SETTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.hide();
                    successListener.onSuccess(new JSONObject(response));
                    Log.d(TAG, " Setting Response" + response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //dialog.hide();
                try{
                    Log.d(TAG," Setting  Error  >>"+ error.toString());
                    handleError(context,error);
                    errorListener.onError(new JSONObject(error.toString()));
                }catch (Exception e) {
                    Log.d(TAG,""+ e.getMessage());
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(DEVICE_ID, device_id);
                return params;
            }
        };

        APIController.getInstance(context).addRequest(overviewRequest, TAG);
    }
}
