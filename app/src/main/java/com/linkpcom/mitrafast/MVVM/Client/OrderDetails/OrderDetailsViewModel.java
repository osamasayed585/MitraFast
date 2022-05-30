package com.linkpcom.mitrafast.MVVM.Client.OrderDetails;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseDB;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
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
public class OrderDetailsViewModel extends BaseViewModel {

    @Inject
    public OrderDetailsViewModel() {
    }

    @Inject
    MutableLiveData<BaseResponse> reportOrder;

    public MutableLiveData<BaseResponse> requestReportOrder(AddTicketRequest addTicketRequest) {
        getCompositeDisposable().add(Observable.just(addTicketRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestClientAddTicket(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> reportOrder.setValue(response), this::onFailure));
        return reportOrder;
    }

    @Inject
    MutableLiveData<OrderDetailsResponse> orderDetailsResponseMutableLiveData;

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

    @Override
    protected void onCleared() {
        super.onCleared();
        removeOrderStatusListener();
    }
}