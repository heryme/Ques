package test.de.recrutement.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rahul Padaliya on 9/11/2017.
 */

public class JsonParserIntroSliderNew {
    private static final String TAG = JsonParserIntroSliderNew.class.getSimpleName();

    public String statusCode;
    public String message;
    public String totalScreens;
    public List<JsonParserIntroSliderNew.result> resultList;

    public List<result> getResultList() {
        return resultList;
    }

    public void setResultList(List<result> resultList) {
        this.resultList = resultList;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalScreens() {
        return totalScreens;
    }

    public void setTotalScreens(String totalScreens) {
        this.totalScreens = totalScreens;
    }

    public class result {
        String fontColor;
        String backgroundColor;
        String screenText;
        String buttonText;
        String screenName;
        int sortOrder;

        public String getFontColor() {
            return fontColor;
        }

        public void setFontColor(String fontColor) {
            this.fontColor = fontColor;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getScreenText() {
            return screenText;
        }

        public void setScreenText(String screenText) {
            this.screenText = screenText;
        }

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            this.buttonText = buttonText;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public int getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
        }
    }

    public static JsonParserIntroSliderNew parseIntroSliderData(JSONObject jsonObject) {
        try {

            if (jsonObject != null && jsonObject.length() > 0) {
                JsonParserIntroSliderNew jsonParserIntroSliderNew = new JsonParserIntroSliderNew();
                jsonParserIntroSliderNew.setStatusCode(jsonObject.optString("status_code"));
                jsonParserIntroSliderNew.setMessage(jsonObject.optString("message"));
                JSONObject result = jsonObject.getJSONObject("result");
                jsonParserIntroSliderNew.setTotalScreens("total_screens");
                JSONArray screenDataArray = result.getJSONArray("screen_data");
                List<JsonParserIntroSliderNew.result> tempResultList = new ArrayList<>();
                for (int i = 0; i < screenDataArray.length(); i++) {
                    JsonParserIntroSliderNew.result data = new JsonParserIntroSliderNew().new result();
                    JSONObject object = screenDataArray.getJSONObject(i);
                    data.setFontColor(object.optString("font_color"));
                    data.setBackgroundColor(object.optString("background_color"));
                    data.setScreenText(object.optString("screen_text"));
                    data.setButtonText(object.optString("button_text"));
                    data.setScreenName(object.optString("screen_name"));
                    data.setSortOrder(object.getInt("sort_order"));
                    tempResultList.add(data);
                    /**
                     * Sort result List For Sort Order Key  Data
                     */
                    sortListData(tempResultList);
                }
                jsonParserIntroSliderNew.setResultList(tempResultList);
                return jsonParserIntroSliderNew;


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sort Data Key For Sort Order Key
     * @param items
     */
    private static void sortListData(List<JsonParserIntroSliderNew.result> items) {
        Collections.sort(items, new Comparator<result>() {
            @Override
            public int compare(result o1, result o2) {
                return Integer.compare(o1.getSortOrder(),o2.getSortOrder());
            }
        });
    }
}
