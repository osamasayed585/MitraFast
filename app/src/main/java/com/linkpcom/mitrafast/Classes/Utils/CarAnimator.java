package com.linkpcom.mitrafast.Classes.Utils;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.location.Location;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class CarAnimator {


    private GoogleMap mMap;
    private Marker driverMarker;
    private Queue<LatLng> locations;
    private AnimatorSet animatorSet;
    private float previousZoomLevel;
    private boolean isZooming;
    private BitmapDescriptor icon;
    private ValueAnimator rotationAnimator;
    float rotateTo;
    float rotateFrom;
    LatLng moveTo;
    LatLng moveFrom;
    private ValueAnimator movingAnimator;

    public void onCreate(BitmapDescriptor icon) {
        this.icon = icon;
        animatorSet = new AnimatorSet();
        locations = new LinkedList<>();
        previousZoomLevel = 16f;
        isZooming = false;
        animatorSet = new AnimatorSet();
    }

    public void onLocationUpdate(LatLng latLng) {
        locations.add(latLng);
    }

    private void moveMap(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.moveCamera(cameraUpdate);
    }


    public void onMapReady(GoogleMap mMap) {
        this.mMap = mMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnCameraChangeListener(getCameraChangeListener());

        subscribeToTimer();
    }

    private void SendNextPoints() {

        if (!animatorSet.isRunning() && !locations.isEmpty()) {
            updateMarker(locations.poll()); // taking the points f rom head of the queue.
        }
    }

    private void updateMarker(LatLng newLatLng) {
        if (driverMarker == null) {
            addMarker(newLatLng);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(newLatLng).zoom(previousZoomLevel).build()));

        }

        float bearingAngle = calculateBearingAngle(newLatLng);
        driverMarker.setAnchor(0.5f, 0.5f);
        rotateTo = Float.isNaN(bearingAngle) ? -1 : bearingAngle;
        rotateFrom = driverMarker.getRotation();
        moveTo = newLatLng;
        moveFrom = driverMarker.getPosition();
        if (!animatorSet.isStarted()) {
            animatorSet.playTogether(rotateMarker(), moveVehicle());
            animatorSet.start();
        }
        if (isZooming)
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition
                    (new CameraPosition.Builder().target(newLatLng)
                            .zoom(previousZoomLevel).build()));


    }

    private void addMarker(LatLng initialPos) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(initialPos);
        if (icon != null)
            markerOptions.icon(icon);
        driverMarker = mMap.addMarker(markerOptions);
    }

    private float calculateBearingAngle(LatLng newLatLng) {
        Location destinationLoc = new Location("service Provider");
        Location userLoc = new Location("service Provider");

        userLoc.setLatitude(driverMarker.getPosition().latitude);
        userLoc.setLongitude(driverMarker.getPosition().longitude);

        destinationLoc.setLatitude(newLatLng.latitude);
        destinationLoc.setLongitude(newLatLng.longitude);

        return userLoc.bearingTo(destinationLoc);

    }

    private ValueAnimator rotateMarker() {
        if (rotationAnimator == null) {
            rotationAnimator = ValueAnimator.ofFloat(0, 1);
            rotationAnimator.setInterpolator(new LinearInterpolator());
            rotationAnimator.setDuration(1555);
            rotationAnimator.addUpdateListener(valueAnimator -> {
                float t = Float.parseFloat(valueAnimator.getAnimatedValue().toString());
                float rot = t * rotateTo + (1 - t) * rotateFrom;
                driverMarker.setRotation(-rot > 180 ? rot / 2 : rot);
            });
        }

        return rotationAnimator;
    }

    private ValueAnimator moveVehicle() {
        if (movingAnimator == null) {
            movingAnimator = ValueAnimator.ofFloat(0, 1);
            movingAnimator.setInterpolator(new LinearInterpolator());
            movingAnimator.setDuration(3000);
            movingAnimator.addUpdateListener(valueAnimator -> {
                float t = Float.parseFloat(valueAnimator.getAnimatedValue().toString());
                LatLng currentPosition = new LatLng(
                        moveFrom.latitude * (1 - t) + (moveTo.latitude) * t,
                        moveFrom.longitude * (1 - t) + (moveTo.longitude) * t);
                driverMarker.setPosition(currentPosition);
            });
        }
        return movingAnimator;
    }


    private void subscribeToTimer() {

        Observable.interval(5, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> SendNextPoints(), throwable -> Timber.d("Shady subscribeToTimer: %s", throwable.getMessage()));


    }

    private GoogleMap.OnCameraChangeListener getCameraChangeListener() {
        return new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                if (previousZoomLevel != position.zoom) {
                    isZooming = true;
                }

                previousZoomLevel = position.zoom;
            }
        };
    }
}
