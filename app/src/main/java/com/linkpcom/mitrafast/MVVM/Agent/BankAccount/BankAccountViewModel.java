package com.linkpcom.mitrafast.MVVM.Agent.BankAccount;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BankAccountDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BankAccountResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BanksResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@Getter
@HiltViewModel
public class BankAccountViewModel extends BaseViewModel {

    @Inject
    public BankAccountViewModel() {
    }


    @Inject
    MutableLiveData<BankAccountResponse> bankAccountResponseMutableLiveData;


    public MutableLiveData<BankAccountResponse> requestBankAccountDetails() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestBankAccountDetails())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> bankAccountResponseMutableLiveData.setValue(response), this::onFailure));
        return bankAccountResponseMutableLiveData;
    }
}