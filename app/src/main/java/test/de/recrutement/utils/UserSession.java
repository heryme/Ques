package test.de.recrutement.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DELL on 25-11-2015.
 */
public class UserSession {

    private Context mContext;
    public static final String PREFERENCE_NAME = "QuestionnairePref";
    public static final String QUESTIONNAIRE_JSON = "questionnaire_json";
    public static String CHECK_VALUE = "check_value";
    public static String DEVICE_ID = "device_id";
    public static String QUESTIONNAIRE_ID = "questionnaire_id";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static UserSession userSession;

    /*public static UserSession getInstance(Context context) {
        if (userSession == null)
            userSession = new UserSession(context);

        return userSession;

    }*/

    public UserSession(Context context) {
        this.mContext = context;
        sharedPreferences = this.mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    /**
     * Set User Id
     *
     * @param userId
     */
    public void setQuestionnaireJson(String userId) {
        editor.putString(QUESTIONNAIRE_JSON, userId);
        editor.commit();
    }

    /**
     * Get User Id
     *
     * @return
     */
    public String getQuestionnaireJson() {
        return getSharedPreferences().getString(QUESTIONNAIRE_JSON, "");
    }

    /**
     * Set Check Box Value For Term And Condition
     *
     * @param checkBoxValue
     */
    public void setCheckBoxValue(boolean checkBoxValue) {
        editor.putBoolean(CHECK_VALUE, checkBoxValue);
        editor.commit();
    }

    /**
     * Get Check Box Value For Term And Condition
     *
     * @return
     */
    public boolean getCheckBoxValue() {
        return getSharedPreferences().getBoolean(CHECK_VALUE, false);
    }

    /**
     * Set Device_id
     *
     * @param deviceID
     */
    public void setDeviceID(String deviceID) {
        editor.putString(DEVICE_ID, deviceID);
        editor.commit();
    }

    public String getDeviceID() {
        return getSharedPreferences().getString(DEVICE_ID, "");
    }

    /**
     * Set Questionnaire ID
     *
     * @param questionnaireID
     */
    public void setQuestionnaireID(String questionnaireID) {
        editor.putString(QUESTIONNAIRE_ID, questionnaireID);
        editor.commit();
    }

    public String getQuestionnaireID() {
        return getSharedPreferences().getString(QUESTIONNAIRE_ID, "");
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return getSharedPreferences().getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
