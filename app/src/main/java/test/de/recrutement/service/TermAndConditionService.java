package test.de.recrutement.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import test.de.recrutement.utils.DialogUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul Padaliya on 3/1/2017.
 */
public class TermAndConditionService extends APIService {

    private static final String TAG = TermAndConditionService.class.getSimpleName();
    private static final String URL_TERM_AND_CONDITION = BASE_URL + "/getsettings";
    private static final String DEVICE_ID = "device_id";
    private static final String KEY = "key";

    public static void getTermAndCondition(final Context context,final String key,
                                           final String device_id,
                                           final APIService.Success<JSONObject> successListener,
                                           final APIService.Error<JSONObject> errorListener) {

        final ProgressDialog dialog = DialogUtility.processDialog(context, "Please Wait", true);

        Log.d(TAG, "Get Term And Condition-->" + URL_TERM_AND_CONDITION);
        StringRequest overviewRequest = new StringRequest(Request.Method.POST, URL_TERM_AND_CONDITION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.hide();
                    successListener.onSuccess(new JSONObject(response));
                    Log.d(TAG, " Term And Condition Response" + response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                try{
                    Log.d(TAG,"Questionnaire Error  >>"+ error.toString());
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
                params.put(KEY,key);
                params.put(DEVICE_ID, device_id);
                return params;
            }
        };

        APIController.getInstance(context).addRequest(overviewRequest, TAG);
    }
}
