package com.linkpcom.mitrafast.MVVM.Agent.OrderDetails;

import android.app.Activity;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AcceptedForOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FirebaseOrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseDB;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.Classes.Utils.LocationUtils;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import timber.log.Timber;

@Getter
@HiltViewModel
public class OrderDetailsViewModel extends BaseViewModel implements LocationUtils.LocationResponseHandler {

    @Inject
    public OrderDetailsViewModel() {
    }

    @Inject
    PreferencesManager preferencesManager;
    LocationUtils locationUtils;

    @Inject
    MutableLiveData<OrderDetailsResponse> orderDetailsResponseMutableLiveData;
    @Inject
    MutableLiveData<FirebaseOrderDetailsResponse> firebaseOrderDetailsResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> agentAcceptOrderResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> ignoreOrderResponseMutableLiveData;
    @Inject
    MutableLiveData<AcceptedForOrderRequest> acceptedForOrderMutableLiveData;
    @Inject
    MutableLiveData<Location> currentLocationResponseMutableLiveData;

    public MutableLiveData<OrderDetailsResponse> requestOrderDetails(int orderId) {
        getCompositeDisposable().add(Observable.just(orderId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestOrderDetails(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> orderDetailsResponseMutableLiveData.setValue(response), this::onFailure));
        return orderDetailsResponseMutableLiveData;
    }


    public MutableLiveData<BaseResponse> requestAgentAcceptOrder(int orderId) {
        getCompositeDisposable().add(Observable.just(orderId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAgentAcceptOrder(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> agentAcceptOrderResponseMutableLiveData.setValue(response), this::onFailure));
        return agentAcceptOrderResponseMutableLiveData;
    }


    public MutableLiveData<BaseResponse> requestIgnoreOrder(int orderId) {
        getCompositeDisposable().add(Observable.just(orderId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestIgnoreOrder(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> ignoreOrderResponseMutableLiveData.setValue(response), this::onFailure));
        return ignoreOrderResponseMutableLiveData;
    }

    public void requestCurrentLocation(Activity activity) {
        if (locationUtils == null)
            locationUtils = new LocationUtils(activity);

        locationUtils.initCurrentLocationVariables();

        locationUtils.setLocationResponseHandler(this);
        locationUtils.requestCurrentLocation();
    }


    private DatabaseReference orderRef;
    private DatabaseReference orderDetailsRef;

    ValueEventListener orderStatusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getRef().equals(orderRef))
                if (dataSnapshot.exists()) {
                    acceptedForOrderMutableLiveData.setValue(AcceptedForOrderRequest.builder()
                            .is_agent_exist(dataSnapshot.child(FireBaseDB.drivers).child(preferencesManager.getUserId()).exists())
                            .order_status(dataSnapshot.child(FireBaseDB.status).getValue(String.class))
                            .build());
                }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Timber.e(error.getMessage());
        }
    };

    public void startOrderListening(String orderId) {
        if (orderRef != null && orderRef.getRef().equals(FireBaseReferences.getOrderStatusRef(orderId)))
            return;


        orderRef = FireBaseReferences.getOrderStatusRef(orderId);
        orderRef.addValueEventListener(orderStatusListener);
    }

    public void removeOrderListener() {
        if (orderRef != null)
            orderRef.removeEventListener(orderStatusListener);
    }




    ValueEventListener orderDetailsListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getRef().equals(orderDetailsRef))
                if (dataSnapshot.exists()) {
                    firebaseOrderDetailsResponseMutableLiveData.setValue(FirebaseOrderDetailsResponse.builder()
                            .delivery_cost(dataSnapshot.child(FireBaseDB.delivery_cost).getValue(String.class))
                            .distance_text(dataSnapshot.child(FireBaseDB.distance_text).getValue(String.class))
                            .duration_text(dataSnapshot.child(FireBaseDB.duration_text).getValue(String.class))
                            .duration_value(dataSnapshot.child(FireBaseDB.duration_value).getValue(String.class))
                            .location(dataSnapshot.child(FireBaseDB.location).getValue(String.class))
                            .total_cost(dataSnapshot.child(FireBaseDB.total_cost).getValue(String.class))
                            .build());
                }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Timber.e(error.getMessage());
        }
    };

    public void startOrderDetailsListening(String orderId) {
        if (orderDetailsRef != null && orderDetailsRef.getRef().equals(FireBaseReferences.getOrderDetailsRef(orderId)))
            return;


        orderDetailsRef = FireBaseReferences.getOrderDetailsRef(orderId);
        orderDetailsRef.addValueEventListener(orderDetailsListener);
    }

    public void removeOrderDetailsListener() {
        if (orderDetailsRef != null)
            orderDetailsRef.removeEventListener(orderDetailsListener);
    }

    public void requestRemoveAvailableLocation() {
        FireBaseReferences.getAvailableRef().child(preferencesManager.getUserId()).removeValue();
    }


    @Override
    public void onLocationResponseHandler(Location location) {
        currentLocationResponseMutableLiveData.setValue(location);
    }

    @Override
    public void onLoading(boolean isLoading) {
        getOnLoadingMutableLiveData().setValue(isLoading);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        removeOrderListener();
        removeOrderDetailsListener();
    }
}