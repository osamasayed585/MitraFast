package com.linkpcom.mitrafast.MVVM.Agent.InProgressOrder;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseDB;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.Classes.Others.MainApplication;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CancelOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.UpdateOrderStatusRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CancelOrderReasonsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.StatusesResponse;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import timber.log.Timber;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class InProgressOrderViewModel extends BaseViewModel {

    @Inject
    public InProgressOrderViewModel() {
    }


    @Inject
    MutableLiveData<OrderDetailsResponse> orderDetailsResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> sendOfferResponseMutableLiveData;
    @Inject
    MutableLiveData<CancelOrderReasonsResponse> cancelOrderReasonsResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> cancelOrderResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> finishOrderResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> updateOrderStatusResponseMutableLiveData;
    @Inject
    MutableLiveData<StatusesResponse> orderStatusesResponseMutableLiveData;


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


    public MutableLiveData<BaseResponse> requestFinishOrder(int orderId) {
        getCompositeDisposable().add(Observable.just(orderId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestFinishOrder(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> finishOrderResponseMutableLiveData.setValue(response), this::onFailure));
        return finishOrderResponseMutableLiveData;
    }



    public MutableLiveData<CancelOrderReasonsResponse> requestCancelOrderReasons() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestAgentCancelOrderReasons())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> cancelOrderReasonsResponseMutableLiveData.setValue(response), this::onFailure));
        return cancelOrderReasonsResponseMutableLiveData;


    }

    public MutableLiveData<BaseResponse> requestCancelOrder(CancelOrderRequest cancelOrderRequest) {
        getCompositeDisposable().add(Observable.just(cancelOrderRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAgentCancelOrder(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> cancelOrderResponseMutableLiveData.setValue(response), this::onFailure));
        return cancelOrderResponseMutableLiveData;


    }

    public MutableLiveData<StatusesResponse> requestOrderStatuses(int order_id) {
        getCompositeDisposable().add(Observable.just(order_id)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestOrderStatuses(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> orderStatusesResponseMutableLiveData.setValue(response), this::onFailure));
        return orderStatusesResponseMutableLiveData;
    }


    public MutableLiveData<BaseResponse> requestUpdateOrderStatus(UpdateOrderStatusRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestUpdateOrderStatus(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> updateOrderStatusResponseMutableLiveData.setValue(response), this::onFailure));
        return updateOrderStatusResponseMutableLiveData;
    }


    /*
     * Fire Base Implementation
     * */
    private DatabaseReference orderStatusRef;
    private boolean orderStatusListenerWorking;

    @Inject
    MutableLiveData<String> orderStatusMutableLiveData ;
    ValueEventListener orderStatusListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.getRef().equals(orderStatusRef))
                if (dataSnapshot.exists())
                    if (dataSnapshot.child(FireBaseDB.status).exists())
                        orderStatusMutableLiveData.setValue(dataSnapshot.child(FireBaseDB.status).getValue(String.class));

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

   @Inject
   PreferencesManager preferencesManager;
    private DatabaseReference busyRef;

    public void requestRemoveBusyLocation() {
        DatabaseReference keyRef = getBusyRef().child(preferencesManager.getUserId());
        removeLocation(keyRef);
    }

    DatabaseReference getBusyRef() {
        if (busyRef == null)
            busyRef = FireBaseReferences.getBusyRef();
        return busyRef;
    }

    public void removeLocation(DatabaseReference reference) {
        reference.removeValue();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeOrderStatusListener();
    }
}