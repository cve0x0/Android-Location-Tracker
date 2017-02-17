package com.n37bl4d3.locationtracker.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.n37bl4d3.locationtracker.Configuration;
import com.n37bl4d3.locationtracker.R;
import com.n37bl4d3.locationtracker.helpers.LogHelper;
import com.n37bl4d3.locationtracker.interfaces.TabLayoutInterface;
import com.n37bl4d3.locationtracker.services.LocationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdatesTabFragment extends Fragment implements TabLayoutInterface {

    public UpdatesTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onCreateView");

        return inflater.inflate(R.layout.fragment_updates_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onViewCreated");

        final TextView textView = (TextView) getActivity().findViewById(R.id.fragment_updates_tab_text_view);

        if (Configuration.sIsFeatureLocationAvailable) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String provider = intent.getStringExtra(LocationService.EXTRA_PROVIDER);
                            double latitude = intent.getDoubleExtra(LocationService.EXTRA_LATITUDE, 0);
                            double longitude = intent.getDoubleExtra(LocationService.EXTRA_LONGITUDE, 0);
                            long time = intent.getLongExtra(LocationService.EXTRA_TIME, 0);
                            long elapsedRealtimeNanos = intent.getLongExtra(LocationService.EXTRA_ELAPSED_REALTIME_NANOS, 0);
                            Bundle extras = intent.getBundleExtra(LocationService.EXTRA_EXTRAS);
                            float accuracy = intent.getFloatExtra(LocationService.EXTRA_ACCURACY, 0);
                            double altitude = intent.getDoubleExtra(LocationService.EXTRA_ALTITUDE, 0);
                            float bearing = intent.getFloatExtra(LocationService.EXTRA_BEARING, 0);
                            float speed = intent.getFloatExtra(LocationService.EXTRA_SPEED, 0);

                            Date date = new Date(time);
                            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd G, HH:mm:ss z");
                            String formattedTime = dateFormat.format(date);

                            textView.append("Provider: " + provider + "; Latitude: " + latitude + "; Longitude: " + longitude + "; Time: " + formattedTime + "; Elapsed realtime nanos: " + elapsedRealtimeNanos + "; Extras: " + extras + "; Accuracy: " + accuracy + "; Altitude: " + altitude + "; Bearing: " + bearing + "; Speed: " + speed + "; \n\n");
                        }
                    }, new IntentFilter(LocationService.ACTION_LOCATION_BROADCAST)
            );
        }
    }

    @Override
    public Fragment getItem() {
        return this;
    }

    @Override
    public CharSequence getPageTitle() {
        return "UPDATES";
    }
}
