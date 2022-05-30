package com.linkpcom.mitrafast.MVVM.Client.PickLocation;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.linkpcom.mitrafast.Classes.Utils.LocationUtils;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AddressResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

@Getter
@HiltViewModel
public class PickLocationViewModel extends BaseViewModel implements LocationUtils.LocationResponseHandler {


    @Inject
    public PickLocationViewModel() {
    }

    /*
     * Request Current Location
     */
    @Inject
    MutableLiveData<Location> currentLocationResponseMutableLiveData;
    LocationUtils locationUtils;

    public void requestCurrentLocation(Activity activity) {
        if (locationUtils == null)
            locationUtils = new LocationUtils(activity);

        locationUtils.initCurrentLocationVariables();
        locationUtils.setLocationResponseHandler(this);
        locationUtils.requestCurrentLocation();
    }

    @Override
    public void onLocationResponseHandler(Location location) {
        currentLocationResponseMutableLiveData.setValue(location);
    }

    @Override
    public void onLoading(boolean isLoading) {
        getOnLoadingMutableLiveData().setValue(isLoading);

    }


    /*
     * Request Current Location Name
     */
    @Inject
    MutableLiveData<AddressResponse> locationNameResponseMutableLiveData;

    public MutableLiveData<AddressResponse> requestLocationName(LatLng latLng, Context context) {
        getCompositeDisposable().add(Observable.just(latLng)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(req -> getRepository().requestAddressName(req, context))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> locationNameResponseMutableLiveData.setValue(response), this::onFailure));

        return locationNameResponseMutableLiveData;
    }
}