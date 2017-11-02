package org.poletalks.sdk.pole_android_sdk.Model;

/**
 * Created by anjal on 11/2/17.
 */

public class UserProfile {
    private String profile_image, clientapp_name, clientapp_uid, fcm_token, device_type;

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getClientapp_name() {
        return clientapp_name;
    }

    public void setClientapp_name(String clientapp_name) {
        this.clientapp_name = clientapp_name;
    }

    public String getClientapp_uid() {
        return clientapp_uid;
    }

    public void setClientapp_uid(String clientapp_uid) {
        this.clientapp_uid = clientapp_uid;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
