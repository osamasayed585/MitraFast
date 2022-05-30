package com.linkpcom.mitrafast.MVVM.Client.Wallet;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TransactionsResponse;
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
public class WalletViewModel extends BaseViewModel {

    @Inject
    public WalletViewModel() {
    }


    @Inject
    MutableLiveData<TransactionsResponse> transactionsResponseMutableLiveData;
    @Inject
    MutableLiveData<WalletResponse> walletResponseMutableLiveData;


    public MutableLiveData<TransactionsResponse> requestTransactions() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestTransactions())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> transactionsResponseMutableLiveData.setValue(response), this::onFailure));
        return transactionsResponseMutableLiveData;


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
}
