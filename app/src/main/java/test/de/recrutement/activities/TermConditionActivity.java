package test.de.recrutement.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import test.de.recrutement.R;
import test.de.recrutement.parser.JsonParser;
import test.de.recrutement.service.APIService;
import test.de.recrutement.service.TermAndConditionService;
import test.de.recrutement.utils.AndroidUtils;
import test.de.recrutement.utils.UlTagHandler;
import test.de.recrutement.utils.UserSession;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@EActivity(R.layout.content_term_condition)
public class TermConditionActivity extends AppCompatActivity {

    private static final String TAG = TermConditionActivity.class.getSimpleName();

    @ViewById(R.id.tv_term_condition)
    TextView tv_term_condition;

    @ViewById(R.id.btn_term_condition_start)
    Button btn_term_condition_start;

    /**
     * User Session
     */
    private UserSession userSession;

    @AfterViews
    public void init() {

        userSession = new UserSession(TermConditionActivity.this);
        if(AndroidUtils.isNetworkAvailable(TermConditionActivity.this)) {
            //Call API
            getTermAndCondition();
        }else {
            Toast.makeText(TermConditionActivity.this,
                    getApplicationContext().getString(R.string.internet_connection),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Click
    public void btn_term_condition_start() {
        userSession.setCheckBoxValue(true);
        //Intent intent = new Intent(getApplicationContext(), MainActivity_.class);
        Intent intent = new Intent(getApplicationContext(), TestTypeActivity_.class);
        finish();
        startActivity(intent);
    }

    /**
     * Call API For Term And Condition
     */
    private void getTermAndCondition(){
        TermAndConditionService.getTermAndCondition(TermConditionActivity.this,
                getString(R.string.key),getString(R.string.device_id),
                new APIService.Success<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d(TAG, " Get Term And Condition Data" + response.toString());
                        //jsonParserTermAndCondition(response);
                        tv_term_condition.setText(Html.fromHtml(JsonParser.jsonParserTermAndCondition(response),null,new UlTagHandler()));

                    }
                }, new APIService.Error<JSONObject>() {
                    @Override
                    public void onError(JSONObject error) {
                        Log.d(TAG, " Not Get Term And Condition Data" + error.toString());
                    }
                });
    }



    /**
     * Json Parse Term And Condition
     * @param jsonObject
     */
    private  void jsonParserTermAndCondition(JSONObject jsonObject) {

        try {
            String status_code = jsonObject.getString("status_code");
            String message = jsonObject.getString("message");
            JSONArray resultArray = jsonObject.getJSONArray("result");
            for(int i = 0; i < resultArray.length(); i++) {
                JSONObject termCondition =  resultArray.getJSONObject(i);
                String id = termCondition.getString("_id");
                String key = termCondition.getString("key");
                String value = termCondition.getString("value");
                //tv_term_condition.setText(Html.fromHtml(value));
                String isActive = termCondition.getString("is_active");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
