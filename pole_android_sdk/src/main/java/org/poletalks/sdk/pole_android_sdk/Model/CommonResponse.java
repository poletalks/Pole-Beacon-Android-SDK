package org.poletalks.sdk.pole_android_sdk.Model;

import java.util.ArrayList;

/**
 * Created by anjal on 11/2/17.
 */

public class CommonResponse {
    private ArrayList<FeedbackQuestion> feedbackQuestions = new ArrayList<>();
    private ProgramDetails item = new ProgramDetails();
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<FeedbackQuestion> getFeedbackQuestions() {
        return feedbackQuestions;
    }

    public void setFeedbackQuestions(ArrayList<FeedbackQuestion> feedbackQuestions) {
        this.feedbackQuestions = feedbackQuestions;
    }

    public ProgramDetails getItem() {
        return item;
    }

    public void setItem(ProgramDetails item) {
        this.item = item;
    }
}
