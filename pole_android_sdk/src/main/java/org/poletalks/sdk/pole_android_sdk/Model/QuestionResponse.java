package org.poletalks.sdk.pole_android_sdk.Model;

/**
 * Created by anjal on 11/10/17.
 */

public class QuestionResponse {
    private String feedback_question_id, type, response;

    public String getFeedback_question_id() {
        return feedback_question_id;
    }

    public void setFeedback_question_id(String feedback_question_id) {
        this.feedback_question_id = feedback_question_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
