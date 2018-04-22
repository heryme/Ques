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
 * Created by Rahul Padaliya on 7/29/2016.
 */
public class DemoTestQuestionnaireService extends APIService {

    private static final String TAG = DemoTestQuestionnaireService.class.getSimpleName();
    private static final String URL_QUESTIONS_DEMO_TEST = BASE_URL + "/getdemoquestionnaire";
    private static final String DEVICE_ID = "device_id";

    public static void getDemoTestQuestionnaireService(final Context context, final String device_id,
                                    final Success<JSONObject> successListener,
                                    final Error<JSONObject> errorListener) {

        final ProgressDialog dialog = DialogUtility.processDialog(context, "Please Wait", true);

        Log.d(TAG, "Get Demo Test -->" + URL_QUESTIONS_DEMO_TEST);
        StringRequest overviewRequest = new StringRequest(Request.Method.POST, URL_QUESTIONS_DEMO_TEST,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.hide();
                    Log.d(TAG, "Get Demo Test Response" + response.toString());
                    successListener.onSuccess(new JSONObject(response));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    //dialog.hide();
                try{
                    Log.d(TAG,"Get Demo Test Error  >>"+ error.toString());
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
