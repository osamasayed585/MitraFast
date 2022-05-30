package com.linkpcom.mitrafast.MVVM.Client.Tracking;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.linkpcom.mitrafast.Classes.Utils.CarAnimator;
import com.linkpcom.mitrafast.Classes.Utils.LocationUtils;
import com.linkpcom.mitrafast.Classes.Utils.MarkersGenerator;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityTrackingBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;


import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class TrackingActivity extends BaseActivity implements OnMapReadyCallback, LocationUtils.LocationResponseHandler {
    ActivityTrackingBinding mBinding;
    TrackingViewModel mViewModel;


    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    LocationUtils locationUtils;
    CarAnimator carAnimator;
    MarkersGenerator markersGenerator;

    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        mViewModel = new ViewModelProvider(this).get(TrackingViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
//        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        mViewModel.getDriverLocationMutableLiveData().observe(this, this::onDriverLocationResponse);

        order = getIntent().getParcelableExtra(OBJECT);
        carAnimator = new CarAnimator();
        markersGenerator = new MarkersGenerator(this);
        carAnimator.onCreate(BitmapDescriptorFactory.fromBitmap(markersGenerator.getViewDriver()));

        initMap();


        locationUtils = new LocationUtils(this);
        locationUtils.initCurrentLocationVariables();
        locationUtils.setLocationResponseHandler(this);

        mViewModel.startDriverLocationListening(String.valueOf(order.getDriver().getId()));

    }


    private void onDriverLocationResponse(LatLng latLng) {
        carAnimator.onLocationUpdate(latLng);
    }

    @Override
    public void onLocationResponseHandler(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
        enableMapLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationUtils.requestCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    private void enableMapLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickuo_pin)).position(new LatLng(Double.parseDouble(order.getShop().getLat()), Double.parseDouble(order.getShop().getLng()))));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_drop_off_pin)).position(new LatLng(Double.parseDouble(order.getLocation().getLat()), Double.parseDouble(order.getLocation().getLng()))));

        carAnimator.onMapReady(mMap);

        ImageView locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        if (locationButton != null) {
            locationButton.setBackgroundResource(R.drawable.circle);
            locationButton.setImageResource(R.drawable.ic_current_location);
            locationButton.setPadding(24, 24, 24, 24);
            ((ViewGroup) locationButton.getParent()).removeView(locationButton);
            mBinding.locationButtonContainer.addView(locationButton);
        }

    }


}