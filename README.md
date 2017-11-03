<p align="center">
  <img src="https://poletalks.com/assets/images/logo.png">
</p>

Pole Proximity Android SDK
===========

The Pole Proximity Android SDK is a library for Android OS that provides developers with components to help build applications.



## Quickstart
1. Add Gradle dependency into your app's `build.config`
``` jitpack
dependencies {
    compile 'com.github.poletalks:Pole-Beacon-Android-SDK:0.1'
}
```

2. Add it in your root build.gradle at the end of repositories:
``` jitpack
allprojects {
    repositories {
	    ...
	    maven { url 'https://jitpack.io' }
    }
}
```

3. Add necessary permissions into your `AndroidManifest`
``` XML
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
//For Android 6.0+
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
```

4. Make sure Bluetooth is enabled on your device. 

5. [If you are running Android 6.0 or higher make sure LOCATION_PERMISSION is granted](https://developer.android.com/training/permissions/requesting.html)

6. Create simple activity that will scan for both iBeacons and Eddystones
``` Java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    PoleProximityManager.onCreateBeacons(this, user_id);
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
```

7. Project build.config

```
buildscript {
    dependencies {
        classpath 'com.google.gms:google-services:3.0.0'
    }
}
```

8. If Firebase is already using in project for cloud messaging. else ignore.

    [Receiving messages from multiple senders](https://firebase.google.com/docs/cloud-messaging/concept-options#receiving-messages-from-multiple-senders). When requesting registration 
send [senderId](https://firebase.google.com/docs/cloud-messaging/concept-options#senderid)
    

    a) Add this in `FirebaseInstanceIdService`

    ```
    refreshedToken = FirebaseInstanceId.getInstance().getToken("YOUR_SENDERID", "FCM");
    PoleNotificationService.getToken(FirebaseInstanceId.getInstance(), mContext);

    ```

    b) Add this in `FirebaseMessagingService`

    ```
    @Override
    public void onMessageReceived(RemoteMessage message) {

     if (PoleNotificationService.onMessageReceived(message, mContext)){
       //call notification generate function here. basically this function will generate if its pole notification and return false else return true,
     }
        
    }
    ```



Pole Contact:
===========
email : anjalsaneen123@gmail.com



MIT License
===========

Copyright (c) 2017 poletalks

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
