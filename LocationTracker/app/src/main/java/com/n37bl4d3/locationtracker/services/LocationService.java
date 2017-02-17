package com.n37bl4d3.locationtracker.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.n37bl4d3.locationtracker.Configuration;
import com.n37bl4d3.locationtracker.helpers.LogHelper;
import com.n37bl4d3.locationtracker.objects.LocationRealmObject;

import io.realm.Realm;

public class LocationService extends Service implements LocationListener {

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {

        public LocationService getService() {
            return LocationService.this;
        }
    }

    private PowerManager.WakeLock mWakeLock;

    private Realm mRealm;

    private LocationManager mLocationManager;

    private String mNetworkProvider, mGpsProvider, mPassiveProvider;

    private boolean mIsRequestingLocationUpdates;

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void requestLocationUpdate(String provider) {
        if (Configuration.sIsFeatureLocationAvailable) {
            if (!mIsRequestingLocationUpdates) {
                if (provider != null) {
                    if ((Configuration.sIsFeatureLocationNetworkAvailable && provider.equals(mNetworkProvider)) || (Configuration.sIsFeatureLocationGpsAvailable && provider.equals(mGpsProvider)) || provider.equals(mPassiveProvider)) {
                        LogHelper.debugLog("Requesting location update with specified provider...");
                        try {
                            mLocationManager.requestSingleUpdate(provider, this, null);
                        } catch (SecurityException e) {
                            LogHelper.errorLog("Error while trying to request location update with specified provider");
                            e.printStackTrace();
                        }
                    } else {
                        LogHelper.warnLog("Will not request location update with specified provider, selected provider is not compatible");
                    }
                } else {
                    LogHelper.warnLog("Will not request location update with specified provider, no selected provider found");
                }
            } else {
                LogHelper.warnLog("Will not request location update with specified provider, already requesting location updates");
            }
        } else {
            LogHelper.warnLog("Will not request location update with specified provider, location feature is not available");

            LogHelper.debugLog("Stopping \"" + LocationService.class.getName() + "\"");
            try {
                stopSelf();
            } catch (Exception e) {
                LogHelper.errorLog("Error while trying to stop \"" + LocationService.class.getName() + "\"");
                e.printStackTrace();
            }
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void requestLocationUpdates(String provider, long minTime, float minDistance) {
        if (Configuration.sIsFeatureLocationAvailable) {
            if (!mIsRequestingLocationUpdates) {
                if (provider != null) {
                    if ((Configuration.sIsFeatureLocationNetworkAvailable && provider.equals(mNetworkProvider)) || (Configuration.sIsFeatureLocationGpsAvailable && provider.equals(mGpsProvider)) || provider.equals(mPassiveProvider)) {
                        LogHelper.debugLog("Requesting location updates with specified provider and time and distance between them...");
                        try {
                            mLocationManager.requestLocationUpdates(provider, minTime, minDistance, this);

                            mIsRequestingLocationUpdates = true;
                        } catch (SecurityException e) {
                            LogHelper.errorLog("Error while trying to request location updates with specified provider and time and distance between them");
                            e.printStackTrace();
                        }
                    } else {
                        LogHelper.warnLog("Will not request location updates with specified provider and time and distance between them, selected provider is not compatible");
                    }
                } else {
                    LogHelper.warnLog("Will not request location updates with specified provider and time and distance between them, no selected provider found");
                }
            } else {
                LogHelper.warnLog("Will not request location updates with specified provider and time and distance between them, already requesting location updates");
            }
        } else {
            LogHelper.warnLog("Will not request location updates with specified provider and time and distance between them, location feature is not available");

            LogHelper.debugLog("Stopping \"" + LocationService.class.getName() + "\"");
            try {
                stopSelf();
            } catch (Exception e) {
                LogHelper.errorLog("Error while trying to stop \"" + LocationService.class.getName() + "\"");
                e.printStackTrace();
            }
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void requestLocationUpdate(Criteria criteria, boolean enabledOnly) {
        if (Configuration.sIsFeatureLocationAvailable) {
            if (!mIsRequestingLocationUpdates) {
                String provider = mLocationManager.getBestProvider(criteria, enabledOnly);
                if (provider != null) {
                    if ((Configuration.sIsFeatureLocationNetworkAvailable && provider.equals(mNetworkProvider)) || (Configuration.sIsFeatureLocationGpsAvailable && provider.equals(mGpsProvider)) || provider.equals(mPassiveProvider)) {
                        LogHelper.debugLog("Requesting location update with criteria best provider...");
                        try {
                            mLocationManager.requestSingleUpdate(provider, this, null);
                        } catch (SecurityException e) {
                            LogHelper.errorLog("Error while trying to request location update with criteria best provider");
                            e.printStackTrace();
                        }
                    } else {
                        LogHelper.warnLog("Will not request location update with criteria best provider, selected provider is not compatible");
                    }
                } else {
                    LogHelper.warnLog("Will not request location update with criteria best provider, no selected provider found");
                }
            } else {
                LogHelper.warnLog("Will not request location update with criteria best provider, already requesting location updates");
            }
        } else {
            LogHelper.warnLog("Will not request location update with criteria best provider, location feature is not available");

            LogHelper.debugLog("Stopping \"" + LocationService.class.getName() + "\"");
            try {
                stopSelf();
            } catch (Exception e) {
                LogHelper.errorLog("Error while trying to stop \"" + LocationService.class.getName() + "\"");
                e.printStackTrace();
            }
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void requestLocationUpdates(Criteria criteria, boolean enabledOnly, long minTime, float minDistance) {
        if (Configuration.sIsFeatureLocationAvailable) {
            if (!mIsRequestingLocationUpdates) {
                String provider = mLocationManager.getBestProvider(criteria, enabledOnly);
                if (provider != null) {
                    if ((Configuration.sIsFeatureLocationNetworkAvailable && provider.equals(mNetworkProvider)) || (Configuration.sIsFeatureLocationGpsAvailable && provider.equals(mGpsProvider)) || provider.equals(mPassiveProvider)) {
                        LogHelper.debugLog("Requesting location updates with criteria best provider and time and distance between them...");
                        try {
                            mLocationManager.requestLocationUpdates(provider, minTime, minDistance, this);

                            mIsRequestingLocationUpdates = true;
                        } catch (SecurityException e) {
                            LogHelper.errorLog("Error while trying to request location updates with criteria best provider and time and distance between them");
                            e.printStackTrace();
                        }
                    } else {
                        LogHelper.warnLog("Will not request location updates with criteria best provider and time and distance between them, selected provider is not compatible");
                    }
                } else {
                    LogHelper.warnLog("Will not request location updates with criteria best provider and time and distance between them, no selected provider found");
                }
            } else {
                LogHelper.warnLog("Will not request location updates with criteria best provider and time and distance between them, already requesting location updates");
            }
        } else {
            LogHelper.warnLog("Will not request location updates with criteria best provider and time and distance between them, location feature is not available");

            LogHelper.debugLog("Stopping \"" + LocationService.class.getName() + "\"");
            try {
                stopSelf();
            } catch (Exception e) {
                LogHelper.errorLog("Error while trying to stop \"" + LocationService.class.getName() + "\"");
                e.printStackTrace();
            }
        }
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void removeLocationUpdates() {
        if (Configuration.sIsFeatureLocationAvailable) {
            LogHelper.debugLog("Removing location updates...");
            try {
                mLocationManager.removeUpdates(this);

                mIsRequestingLocationUpdates = false;
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            LogHelper.warnLog("Will not remove location updates, location feature is not available");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogHelper.debugLog("\"" + this.getClass().getName() + "\" onBind");

        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onCreate");

        Intent intent = new Intent(LocationService.class.getName() + "LocationServiceCreateBroadcast");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeLockTag");

        LogHelper.debugLog("Acquiring wake lock...");
        try {
            mWakeLock.acquire();
        } catch (Exception e) {
            LogHelper.errorLog("Error while trying to acquire wake lock");
            e.printStackTrace();
        }

        mRealm = Realm.getDefaultInstance();

        if (Configuration.sIsFeatureLocationAvailable) {
            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (Configuration.sIsFeatureLocationNetworkAvailable) {
                mNetworkProvider = LocationManager.NETWORK_PROVIDER;
            }

            if (Configuration.sIsFeatureLocationGpsAvailable) {
                mGpsProvider = LocationManager.GPS_PROVIDER;
            }

            mPassiveProvider = LocationManager.PASSIVE_PROVIDER;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onDestroy");

        if (Configuration.sIsFeatureLocationAvailable) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                removeLocationUpdates();
            }
        }
        
        mRealm.close();
        
        if (mWakeLock.isHeld()) {
            LogHelper.debugLog("Releasing wake lock...");
            try {
                mWakeLock.release();
            } catch (Exception e) {
                LogHelper.errorLog("Error while trying to release wake lock");
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(LocationService.class.getName() + "LocationServiceDestroyBroadcast");

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationServiceBroadcast",
            EXTRA_PROVIDER = "extra_provider",
            EXTRA_LATITUDE = "extra_latitude",
            EXTRA_LONGITUDE = "extra_longitude",
            EXTRA_TIME = "extra_time",
            EXTRA_ELAPSED_REALTIME_NANOS = "extra_elapsed_realtime_nanos",
            EXTRA_EXTRAS = "extra_extras",
            EXTRA_ACCURACY = "extra_accuracy",
            EXTRA_ALTITUDE = "extra_altitude",
            EXTRA_BEARING = "extra_bearing",
            EXTRA_SPEED = "extra_speed";

    @Override
    public void onLocationChanged(final Location location) {
        LogHelper.debugLog("\"" + this.getClass().getName() + "\" onLocationChanged");

        LogHelper.infoLog("\"" + this.getClass().getName() + "\" location changed: " + location);

        LogHelper.infoLog("Changed location provider: " + location.getProvider());

        LogHelper.infoLog("Changed location latitude: " + location.getLatitude());

        LogHelper.infoLog("Changed location longitude: " + location.getLongitude());

        LogHelper.infoLog("Changed location time: " + location.getTime());

        LogHelper.infoLog("Changed location elapsed realtime nanos: " + location.getElapsedRealtimeNanos());

        LogHelper.infoLog("Changed location extras: " + location.getExtras());

        if (location.hasAccuracy()) {
            LogHelper.infoLog("Changed location has accuracy: " + location.getAccuracy() + " m");
        }

        if (location.hasAltitude()) {
            LogHelper.infoLog("Changed location has altitude: " + location.getAltitude() + " m");
        }

        if (location.hasBearing()) {
            LogHelper.infoLog("Changed location has bearing: " + location.getBearing() + " °");
        }

        if (location.hasSpeed()) {
            LogHelper.infoLog("Changed location has speed: " + location.getSpeed() + " m/s");
        }

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_PROVIDER, location.getProvider());
        intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
        intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
        intent.putExtra(EXTRA_TIME, location.getTime());
        intent.putExtra(EXTRA_ELAPSED_REALTIME_NANOS, location.getElapsedRealtimeNanos());
        intent.putExtra(EXTRA_EXTRAS, location.getExtras());
        intent.putExtra(EXTRA_ACCURACY, location.getAccuracy());
        intent.putExtra(EXTRA_ALTITUDE, location.getAltitude());
        intent.putExtra(EXTRA_BEARING, location.getBearing());
        intent.putExtra(EXTRA_SPEED, location.getSpeed());

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                LocationRealmObject locationRealmObject = realm.createObject(LocationRealmObject.class);
                locationRealmObject.setProvider(location.getProvider());
                locationRealmObject.setLatitude(location.getLatitude());
                locationRealmObject.setLongitude(location.getLongitude());
                locationRealmObject.setTime(location.getTime());
                locationRealmObject.setElapsedRealtimeNanos(location.getElapsedRealtimeNanos());
                locationRealmObject.setAccuracy(location.getAccuracy());
                locationRealmObject.setAltitude(location.getAltitude());
                locationRealmObject.setBearing(location.getBearing());
                locationRealmObject.setSpeed(location.getSpeed());
            }
        });
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LogHelper.debugLog("\"" + this.getClass().getName() + "\" onStatusChanged");

        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                LogHelper.infoLog("\"" + provider + "\" provider status changed: Out of service");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                LogHelper.infoLog("\"" + provider + "\" provider status changed: Temporarily unavailable");
                break;
            case LocationProvider.AVAILABLE:
                LogHelper.infoLog("\"" + provider + "\" provider status changed: Available");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        LogHelper.debugLog("\"" + this.getClass().getName() + "\" onProviderEnabled");

        LogHelper.infoLog("\"" + this.getClass().getName() + "\" provider enabled: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        LogHelper.debugLog("\"" + this.getClass().getName() + "\" onProviderDisabled");

        LogHelper.infoLog("\"" + this.getClass().getName() + "\" provider disabled: " + provider);
    }
}
