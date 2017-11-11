package org.poletalks.sdk.android;

import android.*;
import android.Manifest;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.poletalks.sdk.pole_android_sdk.PoleProximityManager;


public class MainActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PoleProximityManager.onCreateBeacons(this, "useridaanumone");

        context = this;
        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("key1", "value1");
                    jsonObject.put("key2", "value2");
                    jsonObject.put("key3", "value3");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PoleProximityManager.setUserInfo(jsonObject, context, "clientuseridaanumone");
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        PoleProximityManager.startScanning();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PoleProximityManager.destroyScanning();
    }
}
