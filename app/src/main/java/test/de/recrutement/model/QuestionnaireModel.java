package test.de.recrutement.model;

import java.util.List;

/**
 * Created by Rahul Padaliya on 2/22/2017.
 */
public class QuestionnaireModel {
    String statusCode;
    String message;
    String id;
    String questionnaireName;
    String isDemo;
    String queID;
    String categoryId;
    //List<Questions> questionAndOption;
    String questionTime;
    String text;
    String isAnswer;
    String optionId;
    String questionCount;


   /* public List<Questions> getQuestionAndOption() {
        return questionAndOption;
    }

    public void setQuestionAndOption(List<Questions> questionAndOption) {
        this.questionAndOption = questionAndOption;
    }*/



    List<Questions> questionsList;

    public List<Questions> getQuestionsList() {
        return questionsList;
    }

    public void setQuestionsList(List<Questions> questionsList) {
        this.questionsList = questionsList;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }

    public String getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(String isDemo) {
        this.isDemo = isDemo;
    }

    public String getQueID() {
        return queID;
    }

    public void setQueID(String queID) {
        this.queID = queID;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

 /*   public List<Questions> getQuestionAndOption() {
        return questionAndOption;
    }

    public void setQuestionAndOption(List<Questions> questionAndOption) {
        this.questionAndOption = questionAndOption;
    }*/

    public String getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(String questionTime) {
        this.questionTime = questionTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(String isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(String questionCount) {
        this.questionCount = questionCount;
    }

    // For questions

    public class Questions{
        String question;
        String _id;
        String category_id;
        String question_time;
        List<Options> optionsList;

        public List<Options> getOptionsList() {
            return optionsList;
        }

        public void setOptionsList(List<Options> optionsList) {
            this.optionsList = optionsList;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getQuestion_time() {
            return question_time;
        }

        public void setQuestion_time(String question_time) {
            this.question_time = question_time;
        }


        public class Options {
            String opText;
            String opIsAnswer;
            String opOptionId;

            public String getOpIsAnswer() {
                return opIsAnswer;
            }

            public void setOpIsAnswer(String opIsAnswer) {
                this.opIsAnswer = opIsAnswer;
            }

            public String getOpText() {
                return opText;
            }

            public void setOpText(String opText) {
                this.opText = opText;
            }

            public String getOpOptionId() {
                return opOptionId;
            }

            public void setOpOptionId(String opOptionId) {
                this.opOptionId = opOptionId;
            }
        }
    }
}
