package test.de.recrutement.activities;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;


import test.de.recrutement.R;
import test.de.recrutement.constant.Constant;
import test.de.recrutement.parser.JsonParser;
import test.de.recrutement.service.APIService;
import test.de.recrutement.service.DemoTestQuestionnaireService;
import test.de.recrutement.utils.AndroidUtils;
import test.de.recrutement.utils.UserSession;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity(R.layout.activity_test_type)
public class TestTypeActivity extends AppCompatActivity {

    private static final String TAG = TestTypeActivity.class.getSimpleName();

    /**
     * Permission Array
     */
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    @ViewById(R.id.btn_activity_test_type_start_real_test)
    Button btn_activity_test_type_start_real_test;

    @ViewById(R.id.btn_activity_test_type_start_demo_test)
    Button btn_activity_test_type_start_demo_test;

    private UserSession userSession;

    @AfterViews
    public void init() {
        userSession = new UserSession(TestTypeActivity.this);
    }

    @Click
    public void btn_activity_test_type_start_real_test() {
        //Check Permissions
        if(!AndroidUtils.hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        }
        else {

            if(SplashActivity.questionnaireList.size() != 0) {

                Intent intent = new Intent(TestTypeActivity.this,MainActivity_.class);
                intent.putExtra(Constant.EXAM_TYPE,true);
                finish();
                startActivity(intent);

            }else {

                Toast.makeText(TestTypeActivity.this,
                        getApplicationContext().getString(R.string.real_test_no_question),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Click
    public void btn_activity_test_type_start_demo_test () {

        if(AndroidUtils.isNetworkAvailable(TestTypeActivity.this)) {
            getDemoTestQuestionnaire();
        }else {
            Toast.makeText(TestTypeActivity.this,
                    getApplicationContext().getString(R.string.internet_connection),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Call Demo Test  Questionnaire Service
     */
    private void getDemoTestQuestionnaire() {

        SplashActivity.demoQuestionnaireList.clear();
        DemoTestQuestionnaireService.getDemoTestQuestionnaireService(TestTypeActivity.this,
                /*SplashActivity.deviceId*/userSession.getDeviceID(),
                new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {

                Log.d(TAG, "Get Demo Test Data" + response.toString());
                JsonParser.jsonParserQuestionnaire(response,SplashActivity.demoQuestionnaireList);
                Log.d(TAG,"demoQuestionnaireList Size--> " + SplashActivity.demoQuestionnaireList.size());
                //Redirect TestTypeActivity
                if(SplashActivity.demoQuestionnaireList.size() !=0 ){

                    Intent intent = new Intent(TestTypeActivity.this,MainActivity_.class);
                    intent.putExtra(Constant.EXAM_TYPE,false);
                    finish();
                    startActivity(intent);

                }else {

                    Toast.makeText(TestTypeActivity.this,
                            getApplicationContext().getString(R.string.demo_test_no_question),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new APIService.Error<JSONObject>() {
            @Override
            public void onError(JSONObject error) {
                Log.d(TAG, " Not Get Demo Test Data" + error.toString());
            }
        });
    }
}
