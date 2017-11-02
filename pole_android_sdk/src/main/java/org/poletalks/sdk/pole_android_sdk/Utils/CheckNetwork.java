package org.poletalks.sdk.pole_android_sdk.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


//IS INTERNET AVAILABLE FOR THE USER
public class CheckNetwork
{
    private static final String TAG = CheckNetwork.class.getSimpleName();

    public static boolean isInternetAvailable(Context context)
    {
        try{
            NetworkInfo info = ((ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (info == null)
            {
                Log.d(TAG, "no internet connection");

                return false;
            }
            else
            {
                if (info.isConnected())
                {
                    Log.d(TAG, " internet connection available...");
                    return true;
                }
                else
                {
                    Log.d(TAG, " internet connection");
                    return true;
                }

            }

        }
        catch (Exception e){
            Log.e(TAG, String.valueOf(e));
            return false;
        }
    }
}
