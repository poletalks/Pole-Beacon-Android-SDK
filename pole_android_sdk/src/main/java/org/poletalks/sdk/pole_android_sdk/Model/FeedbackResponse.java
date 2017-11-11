package org.poletalks.sdk.pole_android_sdk.Model;

import java.util.ArrayList;

/**
 * Created by anjal on 11/10/17.
 */

public class FeedbackResponse {
    private String user_id;
    private ArrayList<QuestionResponse> responses = new ArrayList<>();

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<QuestionResponse> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<QuestionResponse> responses) {
        this.responses = responses;
    }
}
