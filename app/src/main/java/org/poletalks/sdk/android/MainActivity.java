package org.poletalks.sdk.android;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
        setContentView(R.layout.activity_main_pole_sdk);

        PoleProximityManager.onCreateBeacons(this, "useridaanumone");
        context = this;
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
