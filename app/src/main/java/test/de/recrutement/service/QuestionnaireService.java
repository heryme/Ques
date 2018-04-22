package test.de.recrutement.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import test.de.recrutement.R;
import test.de.recrutement.utils.DialogUtility;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rahul Padaliya on 7/29/2016.
 */
public class QuestionnaireService extends APIService {

    private static final String TAG = QuestionnaireService.class.getSimpleName();
    //private static final String URL_QUESTIONS_ANSWER = BASE_URL + "/getquestionnaire/NThhYWRmMjg5ZmRhMjAzMzExNmRhYzM2"; //Local
    private static final String URL_QUESTIONS_ANSWER = BASE_URL + "/getquestionnaire"/*+ Constant.QUESTIONNAIRE_ID*/;//Client
    private static final String URL_SEND_ANSWER_JSON = BASE_URL + "/answer";

    private static final String DEVICE_ID = "device_id";
    private static final String QUESTIONNAIRE_ID = "questionnaire_id";
    public static void getQuestionnaire(final Context context,
                                        final String device_id,
                                        final String questionnaireId,
                                        final Success<JSONObject> successListener,
                                        final Error<JSONObject> errorListener) {

 //       final ProgressDialog dialog = DialogUtility.processDialog(context, "Please Wait", true);

        Log.d(TAG, "Get Questionnaire-->" + URL_QUESTIONS_ANSWER);
        StringRequest overviewRequest = new StringRequest(Request.Method.POST,
                URL_QUESTIONS_ANSWER /*+ questionnaireId*/, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.hide();
                    Log.d(TAG, "Questionnaire Response" + response.toString());
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
                    Log.d(TAG,"Questionnaire Error  >>"+ error.toString());
                    error.printStackTrace();
                    handleError(context,error);
                    errorListener.onError(new JSONObject(error.toString()));
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(DEVICE_ID,device_id);
                params.put(QUESTIONNAIRE_ID,questionnaireId);
                Log.d(TAG,"Device Id------------->" + device_id);
                Log.d(TAG,"Questionnaire Id------------->" + questionnaireId);
                return params;
            }
        };

        APIController.getInstance(context).addRequest(overviewRequest, TAG);
    }

    // Send answer
    public static void sendAnswer(final Context context, final HashMap<String,String> param,
                                        final Success<JSONObject> successListener,
                                        final Error<JSONObject> errorListener) {
        final ProgressDialog dialog = DialogUtility.processDialog(context, context.getString(R.string.please_wait_questionnaire), true);

        Log.d(TAG, "Get Ans-->" + URL_SEND_ANSWER_JSON);
        StringRequest sendAnsRequest = new StringRequest(Request.Method.POST, URL_SEND_ANSWER_JSON, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.hide();
                    Log.d(TAG, "Ans Response" + response.toString());
                    successListener.onSuccess(new JSONObject(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                try{
                    Log.d(TAG,"Answer Error  >>"+ error.toString());
                    handleError(context,error);
                    errorListener.onError(new JSONObject(error.toString()));
                }catch (Exception e) {
                    Log.d(TAG,""+ e.getMessage());
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG,"Param-->" + param);
                return param;
            }
        };

        APIController.getInstance(context).addRequest(sendAnsRequest, TAG);
    }
}
