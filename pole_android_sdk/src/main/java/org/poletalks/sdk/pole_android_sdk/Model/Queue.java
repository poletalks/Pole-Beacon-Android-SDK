package org.poletalks.sdk.pole_android_sdk.Model;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anjal on 21/4/17.
 */

public class Queue {
    private String beacon_id, user_id, timeString, fcm_token, client_id;
    private boolean enter;
    private double time, distance;

    public Queue(String beacon_id, String user_id, String client_id, double distance, boolean isEnter, String fcm_token) {
        this.beacon_id = beacon_id;
        this.client_id = client_id;
        this.user_id = user_id;
        this.distance = distance;
        this.enter = isEnter;
        this.time = System.currentTimeMillis()/1000;

        SimpleDateFormat df = new SimpleDateFormat("dd MMM hh:mm aa");
        Date date = new Date(System.currentTimeMillis());
        this.timeString = df.format(date);
        this.fcm_token = fcm_token;
    }

    public Queue(String beacon_id, String user_id, double distance, boolean isEnter, String fcm_token) {
        this.beacon_id = beacon_id;
        this.client_id = client_id;
        this.user_id = user_id;
        this.distance = distance;
        this.enter = isEnter;
        this.time = System.currentTimeMillis()/1000;

        SimpleDateFormat df = new SimpleDateFormat("dd MMM hh:mm aa");
        Date date = new Date(System.currentTimeMillis());
        this.timeString = df.format(date);
        this.fcm_token = fcm_token;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBeacon_id() {
        return beacon_id;
    }

    public void setBeacon_id(String beacon_id) {
        this.beacon_id = beacon_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
