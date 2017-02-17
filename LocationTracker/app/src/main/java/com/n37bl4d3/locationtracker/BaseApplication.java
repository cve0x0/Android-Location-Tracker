package com.n37bl4d3.locationtracker;

import android.app.Application;
import android.content.pm.PackageManager;

import com.n37bl4d3.locationtracker.helpers.LogHelper;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onCreate");

        Configuration.sApplicationName = getString(R.string.app_name);

        PackageManager packageManager = getPackageManager();

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
            LogHelper.infoLog("Feature \"" + PackageManager.FEATURE_LOCATION + "\" is available");
            Configuration.sIsFeatureLocationAvailable = true;
        } else {
            LogHelper.warnLog("Feature \"" + PackageManager.FEATURE_LOCATION + "\" is not available");
            Configuration.sIsFeatureLocationAvailable = false;
        }

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK)) {
            LogHelper.infoLog("Feature \"" + PackageManager.FEATURE_LOCATION_NETWORK + "\" is available");
            Configuration.sIsFeatureLocationNetworkAvailable = true;
        } else {
            LogHelper.warnLog("Feature \"" + PackageManager.FEATURE_LOCATION_NETWORK + "\" is not available");
            Configuration.sIsFeatureLocationNetworkAvailable = false;
        }

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            LogHelper.infoLog("Feature \"" + PackageManager.FEATURE_LOCATION_GPS + "\" is available");
            Configuration.sIsFeatureLocationGpsAvailable = true;
        } else {
            LogHelper.warnLog("Feature \"" + PackageManager.FEATURE_LOCATION_GPS + "\" is not available");
            Configuration.sIsFeatureLocationGpsAvailable = false;
        }

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
