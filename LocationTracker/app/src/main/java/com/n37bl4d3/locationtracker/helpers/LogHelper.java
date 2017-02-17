package com.n37bl4d3.locationtracker.helpers;

import android.util.Log;

import com.n37bl4d3.locationtracker.Configuration;

public class LogHelper {

    public static void verboseLog(String message) {
        if (Configuration.LOGGING_VERBOSE) {
            try {
                Log.v(Configuration.sApplicationName, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void debugLog(String message) {
        if (Configuration.LOGGING_DEBUG) {
            try {
                Log.v(Configuration.sApplicationName, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void infoLog(String message) {
        if (Configuration.LOGGING_INFO) {
            try {
                Log.i(Configuration.sApplicationName, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void warnLog(String message) {
        if (Configuration.LOGGING_WARN) {
            try {
                Log.w(Configuration.sApplicationName, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void errorLog(String message) {
        if (Configuration.LOGGING_ERROR) {
            try {
                Log.e(Configuration.sApplicationName, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
