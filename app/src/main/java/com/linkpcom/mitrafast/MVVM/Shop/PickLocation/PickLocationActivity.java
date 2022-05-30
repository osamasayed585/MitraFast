package com.linkpcom.mitrafast.MVVM.Shop.PickLocation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AddressResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityPickLocationBinding;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ADDRESS;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.LATLNG;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class PickLocationActivity extends BaseActivity implements OnMapReadyCallback {
    ActivityPickLocationBinding mBinding;
    PickLocationViewModel mViewModel;


    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private String address;
    private boolean isMoving;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPickLocationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(PickLocationViewModel.class);
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        mBinding.etAddressName.setVisibility(View.GONE);

        initMap();

        mViewModel.getCurrentLocationResponseMutableLiveData().observe(this, this::onLocationResponse);
        mViewModel.getLocationNameResponseMutableLiveData().observe(this, this::onLocationAddressResponse);

        //Setting Action Bar Title And BackClickListener
        mBinding.next.setOnClickListener(this::onNextClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.requestCurrentLocation(this);
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this::onCameraIdle);
        mMap.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
    }


    private void onLocationResponse(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        enableMapLocation();
    }

    @SuppressLint("MissingPermission")
    private void enableMapLocation() {
        mMap.setMyLocationEnabled(true);


        ImageView locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        if (locationButton != null) {
            locationButton.setBackgroundResource(R.drawable.circle);
            locationButton.setImageResource(R.drawable.ic_current_location);
            locationButton.setPadding(24, 24, 24, 24);
            ((ViewGroup) locationButton.getParent()).removeView(locationButton);
            mBinding.locationButtonContainer.addView(locationButton);
        }
    }


    private void onNextClick(View view) {
        if (!isValid())
            return;
        Intent returnIntent = new Intent();
        returnIntent.putExtra(ADDRESS, address);
        returnIntent.putExtra(LATLNG, mMap.getCameraPosition().target);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }


    //Camera stop move
    public void onCameraIdle() {
        isMoving = false;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isMoving) {
                mViewModel.requestLocationName(mMap.getCameraPosition().target, this);
            }
        }, 1000);
    }

    public void onCameraMoveStarted(int i) {
        address = null;
        isMoving = true;

    }

    private void onLocationAddressResponse(AddressResponse response) {
        address = response.getAddress().getAddressLine(0);
        mBinding.tvAddress.setText(response.getAddress().getAddressLine(0));

    }

    private boolean isValid() {



        return
                validator.isNotNull(address, mBinding.tvAddress);
    }
}