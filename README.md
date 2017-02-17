<h1>Android-Location-Tracker</h1>
<p>Android application which tracks device's location using a bound background service with a partial wake lock, and saves the updates into a Realm database.</p>

<h2>Device requirements</h2>
<ul>
  <li>Android 4.4+</li>
  <li>Location Feature</li>
</ul>

<h2>Technical information</h2>
<ul>
  <li>Compile SDK Version: 25</li>
  <li>Build Tools Version: 25.0.2</li>
  <li>Min SDK Version: 19</li>
  <li>Target SDK Version: 25</li>
  <li>Realm Mobile Database Version: 2.3.0</li>
  <li>IDE used for development: Android Studio (recommended)</li>
</ul>

<hr />

<p>Supported location providers:</p>
<ul>
  <li>Network</li>
  <li>GPS</li>
  <li>Passive</li>
</ul>

<hr />

<p>You are able to request single location update and periodical location updates.</p>

<p>You need to follow the steps bellow:</p>
<ol>
  <li>Start "LocationService"</li>
  <li>Bind "LocationService"</li>
  <li>Unbind "LocationService"</li>
  <li>Stop "LocationService"</li>
</ol>

<p>Note: After the "LocationService" becomes bound, you have to request single location update or periodical location updates by calling one of the its methods from the method overloading.</p>

<hr />

<p>You must request single location update or periodical location updates after the "LocationService" is successfully bound in "onServiceConnected(ComponentName name, IBinder service)" overridden method of the "ServiceConnection" interface if the required application permissions are granted (see "OptionsTabFragment.java").</p>

```java
private ServiceConnection mLocationServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LogHelper.debugLog("\"" + name.getClassName() + "\" onServiceConnected");

        mIsLocationServiceBound = true;

        LocationService.LocalBinder localBinder = (LocationService.LocalBinder) service;

        LocationService locationService = localBinder.getService();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        
        // Request single or periodical location updates
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        LogHelper.debugLog("\"" + name.getClassName() + "\" onServiceDisconnected");

        mIsLocationServiceBound = false;
    }
};
```

<hr />

<p>Here is an example for requesting single location update with specified provider:</p>
<ul>
  <li>Provider: Network</li>
</ul>

```java
locationService.requestLocationUpdate(LocationManager.NETWORK_PROVIDER);
```

<hr />

<p>Here is an example for requesting periodical location updates with specified provider and time and distance between them:</p>
<ul>
  <li>Provider: GPS</li>
  <li>Minimum time interval between location updates, in milliseconds: 60000 (60 seconds, 1 minute)</li>
  <li>Minimum distance between location updates, in meters: 20</li>
</ul>

```java
locationService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 20);
```

<hr />

<p>Here is an example for requesting single location update with criteria best provider:</p>
<ul>
  <li>Criteria: Fine accuracy</li>
  <li>Provider: true; "true" for currently enabled location provider, "false" for not. When using "true" as a value, make sure that the device location is enabled.</li>
</ul>

```java
Criteria criteria = new Criteria();
criteria.setAccuracy(Criteria.ACCURACY_FINE);

locationService.requestLocationUpdate(criteria, true);
```

<hr />

<p>Here is an example for requesting periodical location updates with criteria best provider and time and distance between them:</p>
<ul>
  <li>Criteria: Fine accuracy</li>
  <li>Provider: true; "true" for currently enabled location provider, "false" for not. When using "true" as a value, make sure that the device location is enabled.</li>
  <li>Minimum time interval between location updates, in milliseconds: 60000 (60 seconds, 1 minute)</li>
  <li>Minimum distance between location updates, in meters: 20</li>
</ul>

```java
Criteria criteria = new Criteria();
criteria.setAccuracy(Criteria.ACCURACY_FINE);

locationService.requestLocationUpdates(criteria, true, 6000, 20);
```

<hr />

<p>To stop requesting periodical location updates just simply stop "LocationService" or call the its "removeLocationUpdates()" method.</p>

<h2>Manifest required permissions</h2>
```xml
<uses-permission android:name="android.permission.WAKE_LOCK" />

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

<h2>Screenshots</h2>
<img src="https://raw.githubusercontent.com/n37bl4d3/Android-Location-Tracker/master/Screenshots/Screenshot1.png" width="320" />
<img src="https://raw.githubusercontent.com/n37bl4d3/Android-Location-Tracker/master/Screenshots/Screenshot2.png" width="320" />

<h2>APK</h2>
<a href="https://github.com/n37bl4d3/Android-Location-Tracker/raw/master/app-debug.apk" target="_blank">Download (Unsigned)</a>

<h2>Contributors</h2>
<a href="https://github.com/n37bl4d3/" target="_blank">n37bl4d3</a> (Viliyan Vasilev)

<h2>License</h2>
<p>This project is released under the The GNU General Public License v3.0. See "LICENSE" file for further information.</p>
