package test.de.recrutement.parser;

import android.util.Log;

import test.de.recrutement.model.QuestionnaireModel;
import test.de.recrutement.model.SettingModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rahul Padaliya on 2/23/2017.
 */
public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();
    public static final String STATUS_CODE = "status_code";
    public static final String MESSAGE = "message";
    public static final String RESULT = "result";
    public static final String ID = "_id";
    public static final String QUESTIONNAIRE_NAME = "questionnaire_name";
    public static final String IS_DEMO = "is_demo";
    public static final String QUESTIONS = "questions";
    public static final String CATEGORY_ID = "category_id";
    public static final String TITLE = "title";
    public static final String QUESTION_TIME = "question_time";
    public static final String OPTIONS = "options";
    public static final String TEXT = "text";
    public static final String IS_ANSWER = "is_answer";
    public static final String OPTION_ID = "option_id";
    public static final String QUESTION_COUNT = "question_count";

    /**
     * For Setting Response
     */
    public static final String KEY = "key";
    public static final String VALUE = "value";
    public static final String IP_ADDRESS = "ip_address";
    public static final String IS_ACTIVE = "is_active";
    public static final String IS_DELETED = "is_deleted";
    public static final String UPDATED_AT = "updated_at";
    public static final String CREATED_AT = "created_at";


    public static void jsonParserQuestionnaire(JSONObject response, List<QuestionnaireModel> questionnaireList) {

        QuestionnaireModel questionnaireModel = null;
        if (response != null && response.length() > 0) {
            try {
                questionnaireModel = new QuestionnaireModel();

                questionnaireModel.setStatusCode(response.getString(STATUS_CODE));

                questionnaireModel.setMessage(response.getString(MESSAGE));
                Object object = response.get(RESULT);
                if (object instanceof JSONObject) {
                    JSONObject result = response.getJSONObject(RESULT);
                    questionnaireModel.setId(result.getString(ID));
                    questionnaireModel.setQuestionnaireName(result.getString(QUESTIONNAIRE_NAME));
                    if (result.has(IS_DEMO)) {
                        questionnaireModel.setIsDemo(result.getString(IS_DEMO));
                    }

                    JSONArray questionArr = result.getJSONArray(QUESTIONS);
                    List<QuestionnaireModel.Questions> questionsList = new ArrayList<>();

                    for (int i = 0; i < questionArr.length(); i++) {
                        QuestionnaireModel.Questions questions = questionnaireModel.new Questions();

                        JSONObject questionObject = questionArr.getJSONObject(i);
                        questions.set_id(questionObject.getString(ID));

                        questions.setCategory_id(questionObject.getString(CATEGORY_ID));

                        String title = questionObject.getString(TITLE);
                   /* Spanned spanned = Html.fromHtml(title);
                    title = spanned.toString();*/
                        questions.setQuestion(title/*questionObject.getString("title")*/);

                        questions.setQuestion_time(questionObject.getString(QUESTION_TIME));

                        JSONArray optionArray = questionObject.getJSONArray(OPTIONS);
                        List<QuestionnaireModel.Questions.Options> optionsList = new ArrayList<QuestionnaireModel.Questions.Options>();
                        for (int j = 0; j < optionArray.length(); j++) {
                            JSONObject optionObject = optionArray.getJSONObject(j);

                            QuestionnaireModel.Questions.Options options = questions.new Options();

                            options.setOpText(optionObject.getString(TEXT));

                            options.setOpIsAnswer(optionObject.getString(IS_ANSWER));

                            options.setOpOptionId(optionObject.getString(OPTION_ID));

                            optionsList.add(options);
                        }

                        questions.setOptionsList(optionsList);

                        questionsList.add(questions);

                        //questions.setQuestionsList(questionsList);

                        questionnaireModel.setQuestionCount(result.getString(QUESTION_COUNT));

                        questionnaireModel.setQuestionsList(questionsList);

                        //questionnaireModel.setQuestionAndOption(questionsList);

                        questionnaireList.add(questionnaireModel);
                    }

                } else {
                    Log.d(TAG, "This is JsonArray");
                    JSONArray result = response.getJSONArray(RESULT);
                    Log.d(TAG,"SettingModelInner-->" + result);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Parse Setting Response
     * @param response
     */
    public static void parseSettingResponse(JSONObject response,List<SettingModel> settingsList) {
        try {
            SettingModel settingModel = null;
            if (response != null && response.length() > 0) {

                settingModel = new SettingModel();
                settingModel.setStatus_code(response.getString(STATUS_CODE));

                settingModel.setMessage(response.getString(MESSAGE));
                JSONArray result = response.getJSONArray(RESULT);

                List<SettingModel.SettingModelInner> settingModelInnerList = new ArrayList<>();
                for (int i = 0; i < result.length(); i++) {

                    SettingModel.SettingModelInner settingModelInner = settingModel.new SettingModelInner();
                    JSONObject jsonObject = result.getJSONObject(i);
                    settingModelInner.setId(jsonObject.getString(ID));
                    settingModelInner.setKey(jsonObject.getString(KEY));
                    settingModelInner.setValue(jsonObject.getString(VALUE));
                    settingModelInner.setIp_address(jsonObject.getString(IP_ADDRESS));
                    settingModelInner.setIs_active(jsonObject.getString(IS_ACTIVE));
                    settingModelInner.setIs_deleted(jsonObject.getString(IS_DELETED));
                    settingModelInner.setUpdated_at(jsonObject.getString(UPDATED_AT));
                    settingModelInner.setCreated_at(jsonObject.getString(CREATED_AT));
                    settingModelInnerList.add(settingModelInner);
                    settingModel.setSettingModelInnerList(settingModelInnerList);
                }
                settingsList.add(settingModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Json Parse Term And Condition
     * @param jsonObject
     */
    public static String jsonParserTermAndCondition(JSONObject jsonObject) {

        try {
            if (jsonObject != null && jsonObject.length() > 0) {

                String status_code = jsonObject.getString(STATUS_CODE);
                String message = jsonObject.getString(MESSAGE);
                JSONArray resultArray = jsonObject.getJSONArray(RESULT);
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject termCondition = resultArray.getJSONObject(i);
                    String id = termCondition.getString(ID);
                    String key = termCondition.getString(KEY);
                    String value = termCondition.getString(VALUE);
                    //tv_term_condition.setText(Html.fromHtml(value));
                    String isActive = termCondition.getString(IS_ACTIVE);
                    return value;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
