package com.linkpcom.mitrafast.MVVM.Client.PickLocationData;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.view.View;


import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.tapadoo.alerter.Alerter;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddFavoritePlacesRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityPickLocationDataBinding;

import timber.log.Timber;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.ADDRESS;
import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.LATLNG;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class PickLocationDataActivity extends BaseActivity {
    ActivityPickLocationDataBinding mBinding;
    PickLocationDataViewModel mViewModel;


    LatLng latLng;
    Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPickLocationDataBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


        latLng = getIntent().getParcelableExtra(LATLNG);
        address = getIntent().getParcelableExtra(ADDRESS);
        if (address != null)
            fillData();
        Timber.d("Shady latLng: %s", latLng);

        //ViewModel Setup
        mViewModel = new ViewModelProvider(this).get(PickLocationDataViewModel.class);
        //Common Between Activities
        mViewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        mViewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);


        //Request And Observer
        mViewModel.getPickLocationResponseMutableLiveData().observe(this, this::onAddFavoritePlace);

        mBinding.addFavoritePlaceButton.setOnClickListener(this::onAddFavoritePlaceClick);
    }

    private void fillData() {
        mBinding.address.setText(address.getAddressLine(0));
    }

    private void onAddFavoritePlaceClick(View view) {
        if (!validation())
            return;
        mViewModel.requestAddAddress(getData());
    }

    private void onAddFavoritePlace(BaseResponse response) {
        if (response.isSuccess()) {
            Alerter.create(this)
                    .setIcon(R.drawable.alerter_ic_notifications)
                    .setText(response.getMessage())
                    .setBackgroundColorRes(R.color.colorPrimaryDark)
                    .show();

            setResult(Activity.RESULT_OK);
            finish();

        }
    }


    private boolean validation() {



        return
                validator.isNotEmpty(mBinding.locationName);
    }

    private AddFavoritePlacesRequest getData() {
        return AddFavoritePlacesRequest.builder()
                .lat(String.valueOf(latLng.latitude))
                .lng(String.valueOf(latLng.longitude))
                .address(mBinding.address.getText().toString())
                .name(mBinding.locationName.getText().toString())
                .Locality(address.getLocality())
                .SubLocality(address.getSubLocality())
                .AdminArea(address.getAdminArea())
                .build();
    }





}