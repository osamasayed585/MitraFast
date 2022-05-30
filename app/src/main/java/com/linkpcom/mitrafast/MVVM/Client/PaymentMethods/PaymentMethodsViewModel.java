package com.linkpcom.mitrafast.MVVM.Client.PaymentMethods;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentCardsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentMethodsResponse;
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
public class PaymentMethodsViewModel extends BaseViewModel {

    @Inject
    public PaymentMethodsViewModel() {
    }


    @Inject
    MutableLiveData<PaymentMethodsResponse> paymentMethodsResponseMutableLiveData;
    @Inject
    MutableLiveData<WalletResponse> walletResponseMutableLiveData;


    public MutableLiveData<PaymentMethodsResponse> requestPaymentMethods() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestPaymentMethods())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> paymentMethodsResponseMutableLiveData.setValue(response), this::onFailure));
        return paymentMethodsResponseMutableLiveData;

    }

    public MutableLiveData<WalletResponse> requestWallet() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestWallet())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> walletResponseMutableLiveData.setValue(response), this::onFailure));
        return walletResponseMutableLiveData;


    }

    @Inject
    MutableLiveData<PaymentCardsResponse> paymentCardsResponseMutableLiveData;
    public MutableLiveData<PaymentCardsResponse> requestPaymentCards() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestPaymentCards())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> paymentCardsResponseMutableLiveData.setValue(response), this::onFailure));
        return paymentCardsResponseMutableLiveData;
    }
}