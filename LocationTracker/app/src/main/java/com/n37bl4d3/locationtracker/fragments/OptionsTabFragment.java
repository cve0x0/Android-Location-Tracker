package com.n37bl4d3.locationtracker.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.n37bl4d3.locationtracker.Configuration;
import com.n37bl4d3.locationtracker.R;
import com.n37bl4d3.locationtracker.helpers.LogHelper;
import com.n37bl4d3.locationtracker.interfaces.TabLayoutInterface;
import com.n37bl4d3.locationtracker.services.LocationService;

public class OptionsTabFragment extends Fragment implements TabLayoutInterface {

    private String mNetworkProvider, mGpsProvider, mPassiveProvider;

    private RadioGroup mSpecifiedProviderRadioGroup;
    private RadioButton mSpecifiedProviderRadioGroupProviderNetworkRadioButton, mSpecifiedProviderRadioGroupProviderGpsRadioButton, mSpecifiedProviderRadioGroupProviderPassiveRadioButton;

    private RadioButton mUpdateTypeRadioGroupSingleRadioButton, mUpdateTypeRadioGroupPeriodicalRadioButton;

    private EditText mUpdateOptionsMinimumTimeBetweenUpdates, mUpdateOptionsMinimumDistanceBetweenUpdates;

    private Button mStartButton, mStopButton;

    private boolean mIsLocationServiceBound;

    public OptionsTabFragment() {
        // Required empty public constructor
    }

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

            if (mSpecifiedProviderRadioGroup.getCheckedRadioButtonId() != -1) {
                if (mSpecifiedProviderRadioGroupProviderNetworkRadioButton.isChecked()) {
                    if (mUpdateTypeRadioGroupSingleRadioButton.isChecked()) {
                        locationService.requestLocationUpdate(mNetworkProvider);
                    } else if (mUpdateTypeRadioGroupPeriodicalRadioButton.isChecked()) {
                        locationService.requestLocationUpdates(mNetworkProvider, Long.parseLong(mUpdateOptionsMinimumTimeBetweenUpdates.getText().toString().trim()), Float.parseFloat(mUpdateOptionsMinimumDistanceBetweenUpdates.getText().toString().trim()));
                    }
                } else if (mSpecifiedProviderRadioGroupProviderGpsRadioButton.isChecked()) {
                    if (mUpdateTypeRadioGroupSingleRadioButton.isChecked()) {
                        locationService.requestLocationUpdate(mGpsProvider);
                    } else if (mUpdateTypeRadioGroupPeriodicalRadioButton.isChecked()) {
                        locationService.requestLocationUpdates(mGpsProvider, Long.parseLong(mUpdateOptionsMinimumTimeBetweenUpdates.getText().toString().trim()), Float.parseFloat(mUpdateOptionsMinimumDistanceBetweenUpdates.getText().toString().trim()));
                    }
                } else if (mSpecifiedProviderRadioGroupProviderPassiveRadioButton.isChecked()) {
                    if (mUpdateTypeRadioGroupSingleRadioButton.isChecked()) {
                        locationService.requestLocationUpdate(mPassiveProvider);
                    } else if (mUpdateTypeRadioGroupPeriodicalRadioButton.isChecked()) {
                        locationService.requestLocationUpdates(mPassiveProvider, Long.parseLong(mUpdateOptionsMinimumTimeBetweenUpdates.getText().toString().trim()), Float.parseFloat(mUpdateOptionsMinimumDistanceBetweenUpdates.getText().toString().trim()));

                    }
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogHelper.debugLog("\"" + name.getClassName() + "\" onServiceDisconnected");

            mIsLocationServiceBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onCreate");

        if (Configuration.sIsFeatureLocationAvailable) {
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
            if (mIsLocationServiceBound) {
                LogHelper.debugLog("Unbinding \"" + LocationService.class.getName() + "\"");
                try {
                    getActivity().unbindService(mLocationServiceConnection);

                    mIsLocationServiceBound = false;
                } catch (Exception e) {
                    LogHelper.errorLog("Error while trying to unbind \"" + LocationService.class.getName() + "\"");
                    e.printStackTrace();
                }
            }

            LogHelper.debugLog("Stopping \"" + LocationService.class.getName() + "\"");
            try {
                getActivity().stopService(new Intent(getActivity(), LocationService.class));
            } catch (Exception e) {
                LogHelper.errorLog("Error while trying to stop \"" + LocationService.class.getName() + "\"");
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onCreateView");

        return inflater.inflate(R.layout.fragment_options_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogHelper.verboseLog("\"" + this.getClass().getName() + "\" onViewCreated");

        if (Configuration.sIsFeatureLocationAvailable) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (mSpecifiedProviderRadioGroupProviderNetworkRadioButton.isEnabled()) {
                                mSpecifiedProviderRadioGroupProviderNetworkRadioButton.setEnabled(false);
                            }
                            if (mSpecifiedProviderRadioGroupProviderGpsRadioButton.isEnabled()) {
                                mSpecifiedProviderRadioGroupProviderGpsRadioButton.setEnabled(false);
                            }
                            if (mSpecifiedProviderRadioGroupProviderPassiveRadioButton.isEnabled()) {
                                mSpecifiedProviderRadioGroupProviderPassiveRadioButton.setEnabled(false);
                            }

                            if (mUpdateTypeRadioGroupSingleRadioButton.isEnabled()) {
                                mUpdateTypeRadioGroupSingleRadioButton.setEnabled(false);
                            }
                            if (mUpdateTypeRadioGroupPeriodicalRadioButton.isEnabled()) {
                                mUpdateTypeRadioGroupPeriodicalRadioButton.setEnabled(false);
                            }

                            if (mUpdateOptionsMinimumTimeBetweenUpdates.isEnabled()) {
                                mUpdateOptionsMinimumTimeBetweenUpdates.setEnabled(false);
                            }
                            if (mUpdateOptionsMinimumDistanceBetweenUpdates.isEnabled()) {
                                mUpdateOptionsMinimumDistanceBetweenUpdates.setEnabled(false);
                            }

                            mStartButton.setVisibility(View.GONE);
                            mStopButton.setVisibility(View.VISIBLE);
                        }
                    }, new IntentFilter(LocationService.class.getName() + "LocationServiceCreateBroadcast")
            );

            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            if (mNetworkProvider != null && mNetworkProvider.equals(LocationManager.NETWORK_PROVIDER)) {
                                mSpecifiedProviderRadioGroupProviderNetworkRadioButton.setEnabled(true);
                            }
                            if (mGpsProvider != null && mGpsProvider.equals(LocationManager.GPS_PROVIDER)) {
                                mSpecifiedProviderRadioGroupProviderGpsRadioButton.setEnabled(true);
                            }
                            if (mPassiveProvider != null && mPassiveProvider.equals(LocationManager.PASSIVE_PROVIDER)) {
                                mSpecifiedProviderRadioGroupProviderPassiveRadioButton.setEnabled(true);
                            }

                            mUpdateTypeRadioGroupSingleRadioButton.setEnabled(true);
                            mUpdateTypeRadioGroupPeriodicalRadioButton.setEnabled(true);

                            if (!mUpdateTypeRadioGroupSingleRadioButton.isChecked() && mUpdateTypeRadioGroupPeriodicalRadioButton.isChecked()) {
                                mUpdateOptionsMinimumTimeBetweenUpdates.setEnabled(true);
                                mUpdateOptionsMinimumDistanceBetweenUpdates.setEnabled(true);

                            }

                            mStartButton.setVisibility(View.VISIBLE);
                            mStopButton.setVisibility(View.GONE);
                        }
                    }, new IntentFilter(LocationService.class.getName() + "LocationServiceDestroyBroadcast")
            );
        }

        mSpecifiedProviderRadioGroup = (RadioGroup) getActivity().findViewById(R.id.fragment_options_tab_specifier_provider_box_radio_group);
        mSpecifiedProviderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogHelper.debugLog("\"" + this.getClass().getName() + "\" mSpecifiedProviderRadioGroup onCheckedChanged");

                switch (checkedId) {
                    case R.id.fragment_options_tab_specifier_provider_box_radio_group_provider_network:
                        LogHelper.debugLog("\"" + this.getClass().getName() + "\" mSpecifiedProviderRadioGroup: Network");

                        break;
                    case R.id.fragment_options_tab_specifier_provider_box_radio_group_provider_gps:
                        LogHelper.debugLog("\"" + this.getClass().getName() + "\" mSpecifiedProviderRadioGroup: GPS");

                        break;
                    case R.id.fragment_options_tab_specifier_provider_box_radio_group_provider_passive:
                        LogHelper.debugLog("\"" + this.getClass().getName() + "\" mSpecifiedProviderRadioGroup: Passive");

                        break;
                }
            }
        });

        mSpecifiedProviderRadioGroupProviderNetworkRadioButton = (RadioButton) getActivity().findViewById(R.id.fragment_options_tab_specifier_provider_box_radio_group_provider_network);
        if (mNetworkProvider == null || !mNetworkProvider.equals(LocationManager.NETWORK_PROVIDER)) {
            mSpecifiedProviderRadioGroupProviderNetworkRadioButton.setEnabled(false);
        } else {
            mSpecifiedProviderRadioGroupProviderNetworkRadioButton.setChecked(true);
        }

        mSpecifiedProviderRadioGroupProviderGpsRadioButton = (RadioButton) getActivity().findViewById(R.id.fragment_options_tab_specifier_provider_box_radio_group_provider_gps);
        if (mGpsProvider == null || !mGpsProvider.equals(LocationManager.GPS_PROVIDER)) {
            mSpecifiedProviderRadioGroupProviderGpsRadioButton.setEnabled(false);
        } else {
            if (!mSpecifiedProviderRadioGroupProviderNetworkRadioButton.isChecked()) {
                mSpecifiedProviderRadioGroupProviderGpsRadioButton.setChecked(true);
            }
        }

        mSpecifiedProviderRadioGroupProviderPassiveRadioButton = (RadioButton) getActivity().findViewById(R.id.fragment_options_tab_specifier_provider_box_radio_group_provider_passive);
        if (mPassiveProvider == null || !mPassiveProvider.equals(LocationManager.PASSIVE_PROVIDER)) {
            mSpecifiedProviderRadioGroupProviderPassiveRadioButton.setEnabled(false);
        } else {
            if (!mSpecifiedProviderRadioGroupProviderNetworkRadioButton.isChecked() && !mSpecifiedProviderRadioGroupProviderGpsRadioButton.isChecked()) {
                mSpecifiedProviderRadioGroupProviderPassiveRadioButton.setChecked(true);
            }
        }

        mUpdateOptionsMinimumTimeBetweenUpdates = (EditText) getActivity().findViewById(R.id.fragment_options_tab_update_options_box_minimum_time_between_updates);

        mUpdateOptionsMinimumDistanceBetweenUpdates = (EditText) getActivity().findViewById(R.id.fragment_options_tab_update_options_box_minimum_distance_between_updates);

        RadioGroup updateTypeRadioGroup = (RadioGroup) getActivity().findViewById(R.id.fragment_options_tab_update_type_box_radio_group);
        updateTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogHelper.debugLog("\"" + this.getClass().getName() + "\" mUpdateTypeRadioGroup onCheckedChanged");

                switch (checkedId) {
                    case R.id.fragment_options_tab_update_type_box_radio_group_single:
                        LogHelper.debugLog("\"" + this.getClass().getName() + "\" mUpdateTypeRadioGroup: Single");

                        mUpdateOptionsMinimumTimeBetweenUpdates.setEnabled(false);
                        mUpdateOptionsMinimumTimeBetweenUpdates.getText().clear();
                        mUpdateOptionsMinimumTimeBetweenUpdates.setFocusableInTouchMode(false);

                        mUpdateOptionsMinimumDistanceBetweenUpdates.setEnabled(false);
                        mUpdateOptionsMinimumDistanceBetweenUpdates.getText().clear();
                        mUpdateOptionsMinimumDistanceBetweenUpdates.setFocusableInTouchMode(false);

                        break;
                    case R.id.fragment_options_tab_update_type_box_radio_group_periodical:
                        LogHelper.debugLog("\"" + this.getClass().getName() + "\" mUpdateTypeRadioGroup: Periodical");

                        mUpdateOptionsMinimumTimeBetweenUpdates.setEnabled(true);
                        mUpdateOptionsMinimumTimeBetweenUpdates.setText(String.valueOf(60000));
                        mUpdateOptionsMinimumTimeBetweenUpdates.setFocusableInTouchMode(true);

                        mUpdateOptionsMinimumDistanceBetweenUpdates.setEnabled(true);
                        mUpdateOptionsMinimumDistanceBetweenUpdates.setText(String.valueOf(100));
                        mUpdateOptionsMinimumDistanceBetweenUpdates.setFocusableInTouchMode(true);

                        break;
                }
            }
        });

        mUpdateTypeRadioGroupSingleRadioButton = (RadioButton) getActivity().findViewById(R.id.fragment_options_tab_update_type_box_radio_group_single);
        mUpdateTypeRadioGroupSingleRadioButton.setChecked(true);

        mUpdateTypeRadioGroupPeriodicalRadioButton = (RadioButton) getActivity().findViewById(R.id.fragment_options_tab_update_type_box_radio_group_periodical);

        mStartButton = (Button) getActivity().findViewById(R.id.fragment_options_tab_start_button);
        mStartButton.setVisibility(View.VISIBLE);

        mStopButton = (Button) getActivity().findViewById(R.id.fragment_options_tab_stop_button);
        mStopButton.setVisibility(View.GONE);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configuration.sIsFeatureLocationAvailable) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if ((mSpecifiedProviderRadioGroupProviderNetworkRadioButton.isChecked() || mSpecifiedProviderRadioGroupProviderGpsRadioButton.isChecked() || mSpecifiedProviderRadioGroupProviderPassiveRadioButton.isChecked()) && (mUpdateTypeRadioGroupSingleRadioButton.isChecked() && (mUpdateOptionsMinimumTimeBetweenUpdates.getText().toString().trim().isEmpty() && mUpdateOptionsMinimumDistanceBetweenUpdates.getText().toString().trim().isEmpty())) || (mUpdateTypeRadioGroupPeriodicalRadioButton.isChecked() && (!mUpdateOptionsMinimumTimeBetweenUpdates.getText().toString().trim().isEmpty() && !mUpdateOptionsMinimumDistanceBetweenUpdates.getText().toString().trim().isEmpty()))) {
                            LogHelper.debugLog("Starting \"" + LocationService.class.getName() + "\"");
                            try {
                                getActivity().startService(new Intent(getActivity(), LocationService.class));
                            } catch (Exception e) {
                                LogHelper.errorLog("Error while trying to start \"" + LocationService.class.getName() + "\"");
                                e.printStackTrace();
                            }

                            LogHelper.debugLog("Binding \"" + LocationService.class.getName() + "\"");
                            try {
                                getActivity().bindService(new Intent(getActivity(), LocationService.class), mLocationServiceConnection, Context.BIND_AUTO_CREATE);
                            } catch (Exception e) {
                                LogHelper.errorLog("Error while trying to bind \"" + LocationService.class.getName() + "\"");
                                e.printStackTrace();
                            }

                            getActivity();
                            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                            if (mSpecifiedProviderRadioGroupProviderNetworkRadioButton.isChecked()) {
                                if (!locationManager.isProviderEnabled(mNetworkProvider)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("Network location provider is not enabled. You must enable it in order to receive location updates.");
                                    builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                            } else if (mSpecifiedProviderRadioGroupProviderGpsRadioButton.isChecked()) {
                                if (!locationManager.isProviderEnabled(mGpsProvider)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("GPS location provider is not enabled. You must enable it in order to receive location updates.");
                                    builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                            } else if (mSpecifiedProviderRadioGroupProviderPassiveRadioButton.isChecked()) {
                                if (!locationManager.isProviderEnabled(mPassiveProvider)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("Passive location provider is not enabled. You must enable it in order to receive location updates.");
                                    builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Invalid specified provider or/and update type or/and update options.");
                            builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.setCancelable(false);
                            builder.show();
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            final String[] permissions = {
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            };

                            for (final String permission : permissions) {
                                if (ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
                                    requestPermissions(permissions, 1);
                                }
                            }
                        }
                    }
                }
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configuration.sIsFeatureLocationAvailable) {
                    if (mIsLocationServiceBound) {
                        LogHelper.debugLog("Unbinding \"" + LocationService.class.getName() + "\"");
                        try {
                            getActivity().unbindService(mLocationServiceConnection);

                            mIsLocationServiceBound = false;
                        } catch (Exception e) {
                            LogHelper.errorLog("Error while trying to unbind \"" + LocationService.class.getName() + "\"");
                            e.printStackTrace();
                        }
                    }

                    LogHelper.debugLog("Stopping \"" + LocationService.class.getName() + "\"");
                    try {
                        getActivity().stopService(new Intent(getActivity(), LocationService.class));
                    } catch (Exception e) {
                        LogHelper.errorLog("Error while trying to stop \"" + LocationService.class.getName() + "\"");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public Fragment getItem() {
        return this;
    }

    @Override
    public CharSequence getPageTitle() {
        return "OPTIONS";
    }
}
