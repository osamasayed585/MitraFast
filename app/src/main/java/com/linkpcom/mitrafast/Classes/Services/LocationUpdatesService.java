package com.linkpcom.mitrafast.Classes.Services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.Classes.Utils.Utils;
import com.linkpcom.mitrafast.MVVM.Common.SplashScreen.SplashScreenActivity;
import com.linkpcom.mitrafast.R;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.qualifiers.ApplicationContext;
import timber.log.Timber;

@AndroidEntryPoint
public class LocationUpdatesService extends BaseService implements GeoFire.CompletionListener, DatabaseReference.CompletionListener {

    @ApplicationContext
    @Inject
    Context context;
    public static final String ORDER_STATUS_BROADCAST = PACKAGE_NAME + "orderStatusUpdate.broadcast";
    public static final String ACTION_BROADCAST = PACKAGE_NAME + ".locationUpdatesForegroundService.broadcast";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String TAG = LocationUpdatesService.class.getSimpleName();
    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME + ".started_from_notification";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000; // 1 s
    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = //0.5 s
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;
    private final IBinder mBinder = new LocalBinder();
    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;
    private NotificationManager mNotificationManager;
    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;
    private Handler mServiceHandler;
    /**
     * The current location.
     */
    private Location mLocation;
    private DatabaseReference availableRef;
    private DatabaseReference busyRef;


    public LocationUpdatesService() {
    }

    @Nullable
    public Location getmLocation() {
        return mLocation;
    }

    @Nullable
    public LatLng getLatLng() {
        return mLocation == null ? null : new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult.getLastLocation());
            }
        };

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.i("Service started");
        if (intent == null)
            return preferencesManager.isAvailable() ? START_STICKY : START_NOT_STICKY;
        boolean startedFromNotification = intent.getBooleanExtra(EXTRA_STARTED_FROM_NOTIFICATION,
                false);

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification)
            stopSelf();
        // Tells the system to not try to recreate the service after it has been killed.
        return preferencesManager.isAvailable() ? START_STICKY : START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);

        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Timber.i("in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Timber.i("in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;

    }

    @Override
    public boolean onUnbind(Intent intent) {

        Timber.i("Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Timber.i("Starting foreground service");

            startForeground(NOTIFICATION_ID, getNotification());
        }
        return super.onUnbind(intent); // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    //    /**
//     * Makes a request for location updates. Note that in this sample we merely Timber the
//     * {@link SecurityException}.
//     */
    private void requestLocationUpdates() {
        Timber.i("Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Timber.e("Lost location permission. Could not request updates. " + unlikely);
            // TODO: 12/9/2019 request location permission
        }
    }

    //    /**
//     * Removes location updates. Note that in this sample we merely Timber the
//     * {@link SecurityException}.
//     */
    public void removeLocationUpdates() {
        Timber.i("Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();

        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Timber.e("Lost location permission. Could not remove updates. " + unlikely);
            // TODO: 12/9/2019 request location permission
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent(this, LocationUpdatesService.class);

//        CharSequence text = Utils.getLocationText(mLocation);
        CharSequence title = preferencesManager.isAvailable() ? getApplicationContext().getResources().getString(R.string.working) : getApplicationContext().getResources().getString(R.string.available);
        CharSequence content = getApplicationContext().getResources().getString(R.string.locationUpdateMessage);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a startCalling to onStartCommand() in this service.

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashScreenActivity.class), PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(activityPendingIntent)
//                .addAction(R.drawable.ic_logo, getString(R.string.home),
//                        activityPendingIntent)
//                .addAction(R.drawable.ic_back, getString(R.string.reject),
//                        servicePendingIntent)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.ic_mobile_notifications)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setWhen(System.currentTimeMillis());

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();
                        } else {
                            Timber.w("Failed to get location.");
                        }
                    });
        } catch (SecurityException unlikely) {
            Timber.e("Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        mLocation = location;
//        if (!modelUser.getUser().isOnline()) { // if offline no need update again
//            requestStopService();
//            return;
//        }
        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            // TODO: 7/7/2019
//            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }

        requestAddNewLocation(mLocation);
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    //Shady
    /*
     * Fire Base Implementation For Sending Location Updates
     * */

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    public void requestStopService() {
        removeLocationUpdates();
        stopSelf();

        //TODO: Shady See What This Should Do

//        fireBaseHelper.requestRemoveRef();
    }

    public void requestStartService() {
        requestLocationUpdates();

        //TODO: Shady See What This Should Do
//        startOrderListener();
    }

    public void requestAddNewLocation(Location location) {

        Timber.i("New location: " + location);


        // Notify anyone listening for broadcasts about the new location.
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        GeoLocation geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
        GeoHash geoHash = new GeoHash(geoLocation);
        TrackBean trackBean = TrackBean.builder()
                .g(geoHash.getGeoHashString())
                .build();

        DatabaseReference keyRef;
        if (preferencesManager.isBusy()) {
            keyRef = getBusyRef().child(preferencesManager.getUserId());
        } else {
            keyRef = getAvailableRef().child(preferencesManager.getUserId());
        }
        setLocation(
                keyRef,
                trackBean,
                geoLocation);


    }

    public void setLocation(DatabaseReference reference, TrackBean trackBean, final GeoLocation geoLocation) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("g", trackBean.getG());
        updates.put("l", Arrays.asList(geoLocation.latitude, geoLocation.longitude));
        reference.setValue(updates, this);
    }


    DatabaseReference getAvailableRef() {
        if (availableRef == null)
            availableRef = FireBaseReferences.getAvailableRef();
        return availableRef;
    }

    DatabaseReference getBusyRef() {
        if (busyRef == null)
            busyRef = FireBaseReferences.getBusyRef();
        return busyRef;
    }

    @Override
    public void onComplete(String key, DatabaseError error) {
        if (error != null)
            Timber.e(error.toException().getCause());

    }

    @Override
    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
        Timber.d("onComplete");

    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocationUpdatesService getService() {
            return LocationUpdatesService.this;
        }
    }
}
