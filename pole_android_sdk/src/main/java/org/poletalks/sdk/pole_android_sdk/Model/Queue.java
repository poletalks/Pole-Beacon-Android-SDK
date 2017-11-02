package org.poletalks.sdk.pole_android_sdk.Model;


/**
 * Created by anjal on 21/4/17.
 */

public class Queue {
    private String beacon_id, user_id;
    private boolean isEnter;
    private double time, distance;

    public Queue(String beacon_id, String user_id, double distance, boolean isEnter) {
        this.beacon_id = beacon_id;
        this.user_id = user_id;
        this.distance = distance;
        this.isEnter = isEnter;
        this.time = System.currentTimeMillis()/1000;
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

    public boolean is_enter() {
        return isEnter;
    }

    public void setIs_enter(boolean is_enter) {
        this.isEnter = is_enter;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
