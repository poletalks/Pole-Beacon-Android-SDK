<p align="center">
  <img src="https://poletalks.com/assets/images/logo.png">
</p>

pole Android SDK
===========

The pole Android SDK is a library for Android OS that provides developers with components to help build applications.



## Quickstart
1. Add Gradle dependency into your app's `build.config`
``` jitpack
dependencies {
    compile 'com.github.anjalsan:pole-sdk:0.0'
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

    PoleProximityManager.onCreateBeacons(this, api_key);
}


@Override
protected void onStart() {
    super.onStart();

    PoleProximityManager.startScanning();
}

@Override
protected void onStop() {
    super.onStop();

    PoleProximityManager.stopScanning();
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

8. Some configurations may also need an additional exclusion (if you are getting errors during Gradle sync):

```
 packagingOptions {
     exclude 'main/AndroidManifest.xml'
 }
```

Samples App
===========
For SDK usage samples see the [Samples App](https://github.com/anjalsan/pole-sdk)

Pole Contact:
===========
email : anjalsaneen123@gmail.com
