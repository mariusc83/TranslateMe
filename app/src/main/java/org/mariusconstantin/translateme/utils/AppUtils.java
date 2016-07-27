package org.mariusconstantin.translateme.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.GoogleApiAvailability;

import org.mariusconstantin.translateme.inject.AppContext;

/**
 * Created by MConstantin on 7/4/2016.
 */
public class AppUtils {

    public int isGooglePlayServiceAvailable(@NonNull @AppContext Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        return googleApiAvailability.isGooglePlayServicesAvailable(context);
    }

    public boolean isRecoverable(int status) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        return googleApiAvailability.isUserResolvableError(status);
    }

    public Dialog getErrorDialog(int status, @NonNull Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        return googleApiAvailability.getErrorDialog(activity, status, 2404);
    }
}
