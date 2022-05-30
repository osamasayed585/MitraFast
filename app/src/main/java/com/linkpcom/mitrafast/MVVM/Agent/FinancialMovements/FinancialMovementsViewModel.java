package com.linkpcom.mitrafast.MVVM.Agent.FinancialMovements;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FinancialMovementsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.WalletResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class FinancialMovementsViewModel extends BaseViewModel {

    @Inject
    public FinancialMovementsViewModel() {
    }

    @Inject
    MutableLiveData<WalletResponse> financialMovementsResponseMutableLiveData;
    @Inject
    MutableLiveData<PaymentResponse> paymentResponseMutableLiveData;


    public MutableLiveData<WalletResponse> requestWalletBalance() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestWalletBalance())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> financialMovementsResponseMutableLiveData.setValue(response), this::onFailure));
        return financialMovementsResponseMutableLiveData;


    }




    public MutableLiveData<PaymentResponse> requestPayment(PaymentRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestPayment(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> paymentResponseMutableLiveData.setValue(response), this::onFailure));
        return paymentResponseMutableLiveData;

    }



}