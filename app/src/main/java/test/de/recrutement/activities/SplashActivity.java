package test.de.recrutement.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import test.de.recrutement.R;
import test.de.recrutement.constant.Constant;
import test.de.recrutement.interfaces.INTFAlertOk;
import test.de.recrutement.model.QuestionnaireModel;
import test.de.recrutement.model.SettingModel;
import test.de.recrutement.parser.JsonParser;
import test.de.recrutement.service.APIService;
import test.de.recrutement.service.QuestionnaireService;
import test.de.recrutement.service.SettingService;
import test.de.recrutement.utils.AndroidUtils;
import test.de.recrutement.utils.DialogUtility;
import test.de.recrutement.utils.UserSession;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @ViewById(R.id.btn_splash_start)
    TextView btn_splash_start;

    @ViewById(R.id.tv_pls_wait)
    TextView tv_pls_wait;

    /**
     * User Session
     */
    private UserSession userSession;

    /**
     * Questionnaire List For Real Test And  Demo Test
     */
    public static List<QuestionnaireModel> questionnaireList, demoQuestionnaireList;

    /**
     * Setting List
     */

    public static List<SettingModel> settingsList;

    /**
     * QuestionnaireModel
     */
    public static QuestionnaireModel questionnaireModel;

    /**
     * Device ID
     */
    public static String deviceId;

    @AfterViews
    public void init() {
        //getDeepLinkingData();
        setUpcomponent();

    }

    @Click
    public void btn_splash_start() {
    }

    /**
     * Initialization Basic Components
     */
    private void setUpcomponent() {

        deviceId = /*AndroidUtils.getDeviceId(SplashActivity.this)*/ randomNumber();
        Log.d(TAG, "DeviceId-->" + deviceId);
        userSession = new UserSession(SplashActivity.this);
        userSession.setDeviceID(deviceId);
        userSession.setQuestionnaireID(/*getDeepLinkingData()*/Constant.QUESTIONNAIRE_ID);
        questionnaireList = new ArrayList<>();
        demoQuestionnaireList = new ArrayList<>();
        settingsList = new ArrayList<>();

        if (AndroidUtils.isNetworkAvailable(SplashActivity.this)) {

            //Call Setting API
            getSettings();
            //Call Real Test Questionnaire API
            getQuestionnaire();

            Log.d(TAG, "User Session-->" + userSession.getQuestionnaireJson());

          /* if (userSession.getCheckBoxValue()) {
                //handlerDelayTestType();
                handlerDelay(true);
            } else {
                // TODO: Open terms and condition activity
                handlerDelay(false);
            }*/

        } else {
            Toast.makeText(SplashActivity.this, getApplicationContext().getString(R.string.internet_connection), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * Handler
     */
    private void handlerDelay(final Boolean check) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check) {
                    Intent intent = new Intent(getApplicationContext(), TestTypeActivity_.class);
                    finish();
                    startActivity(intent);
                } else {
                   /* Intent intent = new Intent(getApplicationContext(), TermConditionActivity_.class);
                    finish();
                    startActivity(intent);*/
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity_.class);
                    finish();
                    startActivity(intent);
                }
            }
        }, 2000);
    }

    /**
     * Call Real Test  Questionnaire Service
     */
    private void getQuestionnaire() {
        tv_pls_wait.setVisibility(View.VISIBLE);
        QuestionnaireService.getQuestionnaire(SplashActivity.this,
                deviceId, userSession.getQuestionnaireID(),
                new APIService.Success<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d(TAG, " Get Questionnaire Data" + response.toString());
                        String status, message;
                        //Response parse
                        getListValue();

                        if (response != null && response.length() > 0) {
                            try {
                                status = response.getString(JsonParser.STATUS_CODE);
                                Log.d(TAG, "Status-->" + response.optString(JsonParser.STATUS_CODE));
                                /**
                                 * Here status code 101 for failure if already given exam only for this API
                                 */
                                if (status.equalsIgnoreCase(Constant.CODE_ALREADY_GIVEN_EXAM/*"101"*/)) {
                                    message = response.optString(JsonParser.MESSAGE);
                                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
                                    finish();
                                } else if (status.equalsIgnoreCase(Constant.CODE_SUCCESS/*"1"*/)) {
                                    JsonParser.jsonParserQuestionnaire(response, questionnaireList);
                                    userSession.setQuestionnaireJson(response.toString());
                                    tv_pls_wait.setVisibility(View.GONE);

                                    if (userSession.getCheckBoxValue()) {
                                        //handlerDelayTestType();
                                        handlerDelay(true);
                                    } else {
                                        // TODO: Open terms and condition activity
                                        handlerDelay(false);
                                    }
                                } else if (status.equalsIgnoreCase(Constant.CODE_INVALID_QUESTIONNAIRE_ID/*"100"*/)) {
                                    message = response.optString(JsonParser.MESSAGE);
                                    DialogUtility.alertOk(SplashActivity.this,
                                            String.valueOf(Html.fromHtml(message)),
                                            getString(R.string.exit),
                                            new INTFAlertOk() {
                                                @Override
                                                public void alertOk() {
                                                    finish();
                                                }
                                            });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

               /* JsonParser.jsonParserQuestionnaire(response, questionnaireList);
                userSession.setQuestionnaireJson(response.toString());
                tv_pls_wait.setVisibility(View.GONE);

                if (userSession.getCheckBoxValue()) {
                    //handlerDelayTestType();
                    handlerDelay(true);
                } else {
                    // TODO: Open terms and condition activity
                    handlerDelay(false);
                }*/

                    }
                }, new APIService.Error<JSONObject>() {
                    @Override
                    public void onError(JSONObject error) {
                        Log.d(TAG, " Not Get Questionnaire Data" + error.toString());
                    }
                });
    }

    /**
     * For Testing Value In questionnaireList Or Not
     */
    private void getListValue() {
        for (int i = 0; i < questionnaireList.size(); i++) {
            QuestionnaireModel.Questions qo = questionnaireList.get(i).getQuestionsList().get(i);
            Log.d(TAG, "qo.getQuestion() >> " + qo.getQuestion());

            for (int j = 0; j < qo.getOptionsList().size(); j++) {
                Log.d(TAG, "qo.getOptionsList().get(j) >> " + qo.getOptionsList().get(j).getOpText());
            }
        }
    }

    /**
     * Call Setting API
     */
    private void getSettings() {
        SettingService.getSettings(SplashActivity.this,userSession.getDeviceID()/* deviceId*/, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "Settings API Response-->" + response.toString());
                JsonParser.parseSettingResponse(response, settingsList);
                //parseSettingResponse(response);
            }
        }, new APIService.Error<JSONObject>() {
            @Override
            public void onError(JSONObject error) {

            }
        });
    }

    /**
     * Generate Random Device Id
     *
     * @return
     */
    private String randomNumber() {
        final Random rand = new Random();
        String random = String.valueOf(100000 + rand.nextInt(900000));
        return random;
    }

    /**
     * Get Intent Data  From The Deep Linking(Like ex Download Link)
     *
     * @return
     */
    private String getDeepLinkingData() {
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.d(TAG,"Intent Data--->" + data);
        AndroidUtils.generateNoteOnSD(SplashActivity.this,"Intent.txt","Outer If Data-->" + data);
        String str;
        if (data != null) {
            Log.d("Intent Data if--->", " " + data);
            Log.d(TAG, "Intent Data--->" + data.toString());
            Toast.makeText(SplashActivity.this,"Data IF-->" + data,Toast.LENGTH_LONG).show();

            AndroidUtils.generateNoteOnSD(SplashActivity.this,"Intent","If Data-->" + data);
            String QuestionnaireId = data.toString();
            String[] separated = QuestionnaireId.split("/");
            str = separated[5];
            Log.d(TAG, "str6-->" + str);
            Log.d(TAG, "str6-->" + separated[1]);
            Log.d(TAG, "str6-->" + separated[2]);
            Log.d(TAG, "str6-->" + separated[3]);
            Log.d(TAG, "str6-->" + separated[4]);
            Log.d(TAG, "str5-->" + separated[5]);
            return str;
        } else {
            if (intent.getStringExtra("data") != null) {
                str = intent.getStringExtra("data");
                Log.d(TAG, "Str--->" + str);
                AndroidUtils.generateNoteOnSD(SplashActivity.this,"Intent","else Data-->" + str);
                Toast.makeText(SplashActivity.this,"Data Else -->" + str,Toast.LENGTH_LONG).show();
                String[] separated = str.split("%");
                Log.d(TAG, "Osm-->" + separated[1]);
                return separated[1];
            }


        }
        return null;
    }


}
