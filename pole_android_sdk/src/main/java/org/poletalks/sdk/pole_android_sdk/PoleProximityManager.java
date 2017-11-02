package org.poletalks.sdk.pole_android_sdk;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerFactory;
import com.kontakt.sdk.android.ble.manager.listeners.EddystoneListener;
import com.kontakt.sdk.android.ble.manager.listeners.simple.SimpleEddystoneListener;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import org.poletalks.sdk.pole_android_sdk.Model.Queue;

/**
 * Created by anjal on 10/30/17.
 */

public class PoleProximityManager {

    private static Context mContext;
    public static ProximityManager proximityManager;
    public static String TASKS = "tasks-test";
    private static DatabaseReference mFirebaseHistoryReference;
    private static SharedPreferences polePref;

    public static void onCreateBeacons(Context context, String beacons_id, String uid){
        mContext= context;
        KontaktSDK.initialize(beacons_id);

        polePref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);

        if (uid != null){
            SharedPreferences.Editor editor = polePref.edit();
            editor.putString("uid", uid);
            editor.apply();
        }

        proximityManager = ProximityManagerFactory.create(mContext);
        proximityManager.setEddystoneListener(createEddystoneListener(mContext));
    }


    private static EddystoneListener createEddystoneListener(final Context mContext) {
        return new SimpleEddystoneListener() {
            @Override
            public void onEddystoneDiscovered(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.e("Pole Enter ", eddystone.getUniqueId());
                createNotification("Welcome to "+eddystone.getUniqueId(), "Hope you have an awesome time.");
                setInFirebase(eddystone.getUniqueId(), eddystone.getDistance(), true, mContext);
            }

            @Override
            public void onEddystoneLost(IEddystoneDevice eddystone, IEddystoneNamespace namespace) {
                Log.e("Pole Exit ", eddystone.getUniqueId());
                createNotification("Hope you had a great time!", "Please give us your feedback");
                setInFirebase(eddystone.getUniqueId(), eddystone.getDistance(), false, mContext);
            }
        };
    }

    private static void setInFirebase(String beacon_id, double distance, boolean isEnter, Context context) {
        SharedPreferences pref = context.getSharedPreferences("polePref", Context.MODE_PRIVATE);
        String user_id = pref.getString("uid", "none");
        mFirebaseHistoryReference = FirebaseDatabase.getInstance().getReference();
        Queue queue = new Queue(beacon_id, user_id, distance, isEnter);
        mFirebaseHistoryReference.child(TASKS).push().setValue(queue);
    }

    private static void createNotification(String title, String content) {
        try {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
            Intent resultIntent = new Intent(mContext, FeedbackActivity.class);

            Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.brandlog);
            Integer notificationId = Integer.valueOf(String.valueOf((System.currentTimeMillis() / 1000000)));

            mBuilder.setSmallIcon(R.drawable.small_icon)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setOnlyAlertOnce(true);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(content);
            mBuilder.setStyle(bigTextStyle);

            SharedPreferences pref = mContext.getSharedPreferences("notification", Context.MODE_PRIVATE);
            int totalNotificationCount = pref.getInt("totalNotificationCount",0);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(totalNotificationCount, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setVibrate(new long[]{100});
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(notificationId, mBuilder.build());
        } catch (Exception e){
        }

    }

    public static void startScanning() {
        proximityManager.connect(new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                proximityManager.startScanning();
            }
        });
    }

    public static void stopScanning() {
        proximityManager.stopScanning();
    }

    public static void destroyScanning() {
        proximityManager.disconnect();
        proximityManager = null;
    }
}
