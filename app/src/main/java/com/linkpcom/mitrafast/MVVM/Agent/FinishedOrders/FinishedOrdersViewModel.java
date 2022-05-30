package com.linkpcom.mitrafast.MVVM.Agent.FinishedOrders;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrdersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class FinishedOrdersViewModel extends BaseViewModel {

    @Inject
    public FinishedOrdersViewModel() {
    }


    @Inject
    MutableLiveData<OrdersResponse> finishedOrdersResponseMutableLiveData;


    public MutableLiveData<OrdersResponse> requestFinishedOrders() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestFinishedOrders())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> finishedOrdersResponseMutableLiveData.setValue(response), this::onFailure));
        return finishedOrdersResponseMutableLiveData;


    }


}
