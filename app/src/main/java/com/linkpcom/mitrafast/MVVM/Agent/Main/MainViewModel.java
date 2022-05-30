package com.linkpcom.mitrafast.MVVM.Agent.Main;

import android.app.Activity;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AcceptedForOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.WalletResponse;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseDB;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.Classes.Utils.LocationUtils;
import com.linkpcom.mitrafast.Classes.Others.MainApplication;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AgentProfileResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NotificationsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.Classes.Services.TrackBean;

import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import timber.log.Timber;

@Getter
@HiltViewModel
public class MainViewModel extends BaseViewModel implements GeoFire.CompletionListener, DatabaseReference.CompletionListener, LocationUtils.LocationResponseHandler {

    @Inject
    public MainViewModel() {
    }

    @Inject
    MutableLiveData<AgentProfileResponse> profileResponseMutableLiveData;
    @Inject
    MutableLiveData<OrdersResponse> currentOrdersResponseMutableLiveData;

    @Inject
    MutableLiveData<OrderDetailsResponse> orderMiniDetailsResponseMutableLiveData;

    public MutableLiveData<AgentProfileResponse> requestProfile() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestAgentProfile())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> profileResponseMutableLiveData.setValue(response), this::onFailure));
        return profileResponseMutableLiveData;

    }

    public MutableLiveData<OrdersResponse> requestAgentCurrentOrders() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestAgentCurrentOrders())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> currentOrdersResponseMutableLiveData.setValue(response), this::onFailure));
        return currentOrdersResponseMutableLiveData;

    }

    public MutableLiveData<OrderDetailsResponse> requestOrderDetails(int orderId) {
        getCompositeDisposable().add(Observable.just(orderId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestOrderDetails(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> orderMiniDetailsResponseMutableLiveData.setValue(response), this::onFailure));
        return orderMiniDetailsResponseMutableLiveData;
    }

    @Inject
    MutableLiveData<WalletResponse> walletBalanceResponseMutableLiveData;

    public MutableLiveData<WalletResponse> requestWalletBalance() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestWalletBalance())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> walletBalanceResponseMutableLiveData.setValue(response), this::onFailure));
        return walletBalanceResponseMutableLiveData;


    }


    private DatabaseReference offlineRef;
    private DatabaseReference availableRef;
    private DatabaseReference busyRef;

    @Inject
    PreferencesManager preferencesManager;

    public void requestAddOfflineLocation(Location location) {

        GeoLocation geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
        GeoHash geoHash = new GeoHash(geoLocation);
        TrackBean trackBean = TrackBean.builder()
//                .b(location.getBearing())
                .g(geoHash.getGeoHashString())
//                .time(String.valueOf(Calendar.getInstance().getTime().getTime()))
//                .lat_lng(geoLocation.latitude + "," + geoLocation.longitude)
                .build();


        //Delivery Service = 6
        String serviceId = "6";

        DatabaseReference keyRef = getOfflineRef().child(preferencesManager.getUserId());
        setLocation(
                keyRef,
                trackBean,
                geoLocation);


    }

    public void requestRemoveOfflineLocation() {


        DatabaseReference keyRef = getOfflineRef().child(preferencesManager.getUserId());
        removeLocation(keyRef);


    }

    public void requestRemoveAvailableLocation() {
        DatabaseReference keyRef = getAvailableRef().child(preferencesManager.getUserId());
        removeLocation(keyRef);


    }

    public void requestRemoveBusyLocation() {
        DatabaseReference keyRef = getBusyRef().child(preferencesManager.getUserId());
        removeLocation(keyRef);
    }

    DatabaseReference getAvailableRef() {
        if (availableRef == null)
            availableRef = FireBaseReferences.getAvailableRef();
        return availableRef;
    }

    DatabaseReference getOfflineRef() {
        if (offlineRef == null)
            offlineRef = FireBaseReferences.getOfflineRef();
        return offlineRef;
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

    public void setLocation(DatabaseReference reference, TrackBean trackBean, final GeoLocation location) {
        Timber.d("Shady getG: %s", trackBean.getG());
        Timber.d("Shady location: %s %s", location.latitude, location.longitude);
        Timber.d("Shady reference: %s", reference);

        Map<String, Object> updates = new HashMap<>();
//        updates.put("time", trackBean.getTime());
        updates.put("g", trackBean.getG());
//        updates.put("b", trackBean.getB());
        updates.put("l", Arrays.asList(location.latitude, location.longitude));
        reference.setValue(updates, this);
    }

    public void removeLocation(DatabaseReference reference) {
        reference.removeValue();
    }

    @Override
    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
        Timber.d("onComplete");

    }

    /*
     * Request Current Location
     */
    @Inject
    MutableLiveData<Location> currentLocationResponseMutableLiveData;
    private LocationUtils locationUtils;


    public void initLocationUtils(Activity activity) {
        if (locationUtils == null)
            locationUtils = new LocationUtils(activity);

        locationUtils.initSendingLocationVariables();

    }

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
     * Fire Base Implementation
     * */
    private DatabaseReference ordersRef;
    ChildEventListener ordersListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

            orderAddedMutableLiveData.setValue(dataSnapshot.getKey());

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            orderRemovedMutableLiveData.setValue(snapshot.getKey());

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            ordersListenerWorking = false;

        }
    };
    private boolean ordersListenerWorking;
    @Inject
    MutableLiveData<String> orderAddedMutableLiveData;
    @Inject
    MutableLiveData<String> orderRemovedMutableLiveData;


    public void removeOrdersListener() {
        ordersListenerWorking = false;
        if (ordersRef != null)
            ordersRef.removeEventListener(ordersListener);

    }

    public void startOrdersListening(String agentId) {
        if (ordersRef != null && ordersRef.getRef().equals(FireBaseReferences.getAgentOrdersRef(agentId)) && ordersListenerWorking)
            return;

        ordersListenerWorking = true;
        ordersRef = FireBaseReferences.getAgentOrdersRef(agentId);
        ordersRef.addChildEventListener(ordersListener);


        Timber.d("Shady agentId %s + ordersRef %s ", agentId, ordersRef);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeOrdersListener();
    }


    /*
     * Fire Base Implementation
     * */
    @Inject
    MutableLiveData<AcceptedForOrderRequest> acceptedForOrderMutableLiveData;

    private DatabaseReference orderRef;
    private boolean orderListenerWorking;

    ValueEventListener orderStatusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getRef().equals(orderRef))
                if (dataSnapshot.exists())
                    acceptedForOrderMutableLiveData.setValue(AcceptedForOrderRequest.builder()
                            .is_agent_exist(dataSnapshot.child(FireBaseDB.drivers).child(preferencesManager.getUserId()).exists())
                            .order_status(dataSnapshot.child(FireBaseDB.status).getValue(String.class))
                            .build());

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Timber.e(error.getMessage());
            orderListenerWorking = false;
        }
    };

    public void startOrderListening(String orderId) {
        if (orderRef != null && orderRef.getRef().equals(FireBaseReferences.getOrderStatusRef(orderId)) && orderListenerWorking)
            return;


        orderListenerWorking = true;
        orderRef = FireBaseReferences.getOrderStatusRef(orderId);
        orderRef.addValueEventListener(orderStatusListener);
    }

    public void removeOrderListener() {
        orderListenerWorking = false;

        if (orderRef != null)
            orderRef.removeEventListener(orderStatusListener);
    }


}