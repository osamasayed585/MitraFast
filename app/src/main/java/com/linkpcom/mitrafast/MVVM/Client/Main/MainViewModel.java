package com.linkpcom.mitrafast.MVVM.Client.Main;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AdsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CategoriesResponse;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NotificationsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseDB;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.Classes.Utils.LocationUtils;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@Getter
@HiltViewModel
public class MainViewModel extends BaseViewModel {

    @Inject
    public MainViewModel() {
    }

    @Inject
    MutableLiveData<CategoriesResponse> categoriesResponseMutableLiveData;
    @Inject
    MutableLiveData<OrdersResponse> userCurrentOrdersResponseMutableLiveData;

    @Inject
    MutableLiveData<AdsResponse> adsResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> adClickResponseMutableLiveData;
    @Inject
    MutableLiveData<NotificationsResponse> notificationsResponseMutableLiveData;

    public MutableLiveData<AdsResponse> requestAds() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestAds())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> adsResponseMutableLiveData.setValue(response), this::onFailure));
        return adsResponseMutableLiveData;

    }

    public MutableLiveData<BaseResponse> requestAdClick(int adId) {
        getCompositeDisposable().add(Observable.just(adId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAdClick(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> adClickResponseMutableLiveData.setValue(response), this::onFailure));
        return adClickResponseMutableLiveData;
    }

    public MutableLiveData<CategoriesResponse> requestCategories() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestCategories())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> categoriesResponseMutableLiveData.setValue(response), this::onFailure));
        return categoriesResponseMutableLiveData;

    }

    public MutableLiveData<CategoriesResponse> requestCategoriesSearch(String keyword) {
        getCompositeDisposable().add(Observable.just(keyword)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestCategoriesSearch(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> categoriesResponseMutableLiveData.setValue(response), this::onFailure));
        return categoriesResponseMutableLiveData;

    }

    public MutableLiveData<OrdersResponse> requestUserCurrentOrders() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestUserCurrentOrders())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> userCurrentOrdersResponseMutableLiveData.setValue(response), this::onFailure));
        return userCurrentOrdersResponseMutableLiveData;

    }

    @Inject
    MutableLiveData<OrdersResponse> currentOrders;

    public MutableLiveData<OrdersResponse> requestCurrentOrders() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().getCurrentOrders())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> currentOrders.setValue(response), this::onFailure));
        return currentOrders;
    }

    public MutableLiveData<NotificationsResponse> requestNotifications() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestNotifications())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> notificationsResponseMutableLiveData.setValue(response), this::onFailure));
        return notificationsResponseMutableLiveData;
    }

    @Inject
    MutableLiveData<BaseResponse> seenNotificationMutableLiveData;

    public MutableLiveData<BaseResponse> requestSeenNotification() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestSeenNotifications())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> seenNotificationMutableLiveData.setValue(response), this::onFailure));
        return seenNotificationMutableLiveData;
    }

    /*
     * Fire Base Implementation
     * */
    private DatabaseReference orderStatusRef;
    private boolean orderStatusListenerWorking;

    @Inject
    MutableLiveData<String> orderStatusMutableLiveData;
    ValueEventListener orderStatusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Timber.d("data is : %s", "dataSnapshot.child(FireBaseDB.status).getValue(String.class)");
            if (dataSnapshot.getRef().equals(orderStatusRef))
                if (dataSnapshot.exists())
                    if (dataSnapshot.child(FireBaseDB.status).exists()) {
                        orderStatusMutableLiveData.setValue(dataSnapshot.child(FireBaseDB.status).getValue(String.class));
                    }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Timber.e(error.getMessage());
            orderStatusListenerWorking = false;
        }
    };


    public void startOrderStatusListening(int orderId) {
        if (orderStatusRef != null && orderStatusRef.getRef().equals(FireBaseReferences.getOrderStatusRef(String.valueOf(orderId))) && orderStatusListenerWorking)
            return;
        orderStatusListenerWorking = true;
        orderStatusRef = FireBaseReferences.getOrderStatusRef(String.valueOf(orderId));
        orderStatusRef.addValueEventListener(orderStatusListener);

    }

    public void removeOrderStatusListener() {
        orderStatusListenerWorking = false;

        if (orderStatusRef != null)
            orderStatusRef.removeEventListener(orderStatusListener);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeOrderStatusListener();
    }
}