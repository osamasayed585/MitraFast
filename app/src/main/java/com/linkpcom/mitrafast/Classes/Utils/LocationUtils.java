package com.linkpcom.mitrafast.Classes.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.linkpcom.mitrafast.Classes.Others.CONSTANTS;
import com.linkpcom.mitrafast.Classes.Services.LocationUpdatesService;
import com.linkpcom.mitrafast.R;

import java.util.List;

import timber.log.Timber;

public class LocationUtils {
    Activity activity;

    //Current Location variables
    LocationRequest locationRequest;
    private LocationResponseHandler mLocationResponseHandler;



    //Sending Location variables
    public LocationUpdatesService mService;
    PreferencesManager preferencesManager;
    private boolean mBound = false;
    private ServiceConnection mServiceConnection;

    public LocationUtils(Activity activity) {
        this.activity = activity;

    }

    public void initCurrentLocationVariables() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(0)
                .setFastestInterval(1);
    }

    //Current Location variables
    public void requestCurrentLocation() {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    mLocationResponseHandler.onLoading(true);
                    onLocationPermissionGranted();
                } else {
                    //onPermissionDenied
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    private void onLocationPermissionGranted() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        LocationServices.getSettingsClient(activity)
                .checkLocationSettings(builder.build())
                .addOnFailureListener(this::onUnableRequestingLocation)
                .addOnSuccessListener(this::onAbleRequestingLocation);
    }

    @SuppressLint("MissingPermission")
    private void onAbleRequestingLocation(LocationSettingsResponse locationSettingsResponse) {
        //locationRequest
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mLocationResponseHandler.onLoading(false);
                mLocationResponseHandler.onLocationResponseHandler(locationResult.getLastLocation());


            }
        };
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    public void onUnableRequestingLocation(@NonNull Exception e) {
        int statusCode = ((ApiException) e).getStatusCode();
        switch (statusCode) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED: {
                // Location settings are not satisfied. But could be fixed by showing the
                // user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException rae = (ResolvableApiException) e;
                    startResolutionForResult(rae, CONSTANTS.INTENTS.REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException ee) {
                    // Ignore the error.
                } catch (ClassCastException ee) {
                    // Ignore, should be an impossible error.
                }
                break;
            }
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
                activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            break;
        }

    }

    public void startResolutionForResult(ResolvableApiException rae, int code) throws IntentSender.SendIntentException {

        rae.startResolutionForResult(activity, code);

    }

    public void setLocationResponseHandler(LocationResponseHandler locationResponseHandler) {
        mLocationResponseHandler = locationResponseHandler;

    }

    public interface LocationResponseHandler {
        void onLocationResponseHandler(Location location);

        void onLoading(boolean isLoading);

    }

    public void initSendingLocationVariables() {


        preferencesManager = new PreferencesManager(activity);


        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Timber.d("onServiceConnected");
                LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
                mService = binder.getService();
                mBound = true;
                if (preferencesManager.isAvailable())
                    requestPermissions();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Timber.d("onServiceDisconnected");
                mService = null;
                mBound = false;
            }
        };

    }

    //Sending Location Methods
    public void requestPermissions() {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    onPermissionGranted();
                } else {
                    onPermissionDenied();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Snackbar.make(
                        activity.findViewById(android.R.id.content).getRootView(),
                        R.string.permissionMessage,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, view -> token.continuePermissionRequest())
                        .show();
            }
        }).check();
    }

    private void onPermissionDenied() {

        // TODO: 7/2/2019
    }

    private void onPermissionGranted() {
        if (preferencesManager.isAvailable() && mService != null)
            mService.requestStartService();
    }

    public void onStop() {

        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.

            activity.unbindService(mServiceConnection);
            mBound = false;
            //       mService.removeLocationUpdates();
        }

    }

    public void onStart() {

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        activity.bindService(new Intent(activity, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

    }

    public void stopService() {
        if (mService != null)
            mService.requestStopService();

    }

}
