package org.poletalks.sdk.pole_android_sdk.Model;

import java.util.ArrayList;

/**
 * Created by anjal on 11/9/17.
 */

public class FeedbackQuestion {
    private String _id, item_id, item_type, type, title, response;
    private ArrayList<String> options = new ArrayList<>();
    private ArrayList<DummyOption> dummyOptions = new ArrayList<>();

    public String getResponse() {
        if (type.equals("SINGLE") || type.equals("MULTIPLE")){
            response = null;
            for (DummyOption item : dummyOptions){
                if (response == null){
                    if (item.isSelected()){
                        response = item.getDummyOption();
                    }
                } else {
                    if (item.isSelected()){
                        response = response + ", " +item.getDummyOption();
                    }
                }
            }
            return response;
        } else {
            return response;
        }
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<DummyOption> getDummyOptions() {
        return dummyOptions;
    }

    public void setDummyOptions(ArrayList<DummyOption> dummyOptions) {
        this.dummyOptions = dummyOptions;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
