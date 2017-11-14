package org.poletalks.sdk.android;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.poletalks.sdk.pole_android_sdk.PoleProximityManager;
import org.poletalks.sdk.pole_android_sdk.Utils.PoleNotificationService;


public class MainActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    private Context context;
    private static int REQUEST_ENABLE_BT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pole_sdk);

        PoleProximityManager.onCreateBeacons(this, null);
        context = this;

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                PoleProximityManager.startScanning();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        PoleProximityManager.destroyScanning();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_ENABLE_BT)
        {
            switch (resultCode)
            {
                // When the user press OK button.
                case Activity.RESULT_OK:
                {
//                    PoleProximityManager.stopScanning();
                    PoleProximityManager.startScanning();
                }
                break;

                // When the user press cancel button or back button.
                case Activity.RESULT_CANCELED:
                {
                    // TODO
                }
                break;
                default:
                    break;
            }
        }
    }
}
