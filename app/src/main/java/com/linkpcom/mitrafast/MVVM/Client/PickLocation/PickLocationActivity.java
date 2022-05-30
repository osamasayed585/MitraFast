package com.linkpcom.mitrafast.MVVM.Client.PickLocation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
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

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class PickLocationActivity extends BaseActivity implements OnMapReadyCallback {

    private ActivityPickLocationBinding mBinding;
    private PickLocationViewModel mViewModel;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private boolean isMoving;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPickLocationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(PickLocationViewModel.class);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);

        initMap();

        mViewModel.getLocationNameResponseMutableLiveData().observe(this, this::onLocationAddressResponse);
        mViewModel.getCurrentLocationResponseMutableLiveData().observe(this, this::onLocationResponse);

        mBinding.next.setOnClickListener(this::onNextClick);
    }

    //Made Request On onResume To Make Sure That User Gave Permission For GPS
    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.requestCurrentLocation(this);
    }

    private void onNextClick(View view) {
        if (!isValid())
            return;
        preferencesManager.setLat(String.valueOf(mMap.getCameraPosition().target.latitude));
        preferencesManager.setLng(String.valueOf(mMap.getCameraPosition().target.longitude));
        preferencesManager.setAddress(address.getAddressLine(0));
        preferencesManager.setAddressName(mBinding.etAddressName.getText().toString());
        preferencesManager.setAddressId(null);

        setResult(Activity.RESULT_OK);
        finish();
    }

    private boolean isValid() {
        return
                validator.isNotNull(address, mBinding.tvAddress) &&
                        validator.isNotEmpty(mBinding.etAddressName);
    }

    private void initMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void onLocationResponse(Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
        enableMapLocation();
    }

    private void onLocationAddressResponse(AddressResponse response) {
        address = response.getAddress();
        mBinding.tvAddress.setText(response.getAddress().getAddressLine(0));
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this::onCameraIdle);
        mMap.setOnCameraMoveStartedListener(this::onCameraMoveStarted);
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
}
