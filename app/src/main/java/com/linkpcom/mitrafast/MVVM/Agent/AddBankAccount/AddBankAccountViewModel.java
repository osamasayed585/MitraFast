package com.linkpcom.mitrafast.MVVM.Agent.AddBankAccount;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddBankAccountRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditBankAccountRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BanksResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@Getter
@HiltViewModel
public class AddBankAccountViewModel extends BaseViewModel {

    @Inject
    public AddBankAccountViewModel() {
    }

    @Inject
    MutableLiveData<BanksResponse> banksResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> editBankAccountResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> addBnkAccountResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestEditBankAccount(EditBankAccountRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestEditBankAccount(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> editBankAccountResponseMutableLiveData.setValue(response), this::onFailure));
        return editBankAccountResponseMutableLiveData;


    }

    public MutableLiveData<BanksResponse> requestBanks() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestBanks())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> banksResponseMutableLiveData.setValue(response), this::onFailure));
        return banksResponseMutableLiveData;
    }


    public MutableLiveData<BaseResponse> requestAddBankAccount(AddBankAccountRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAddBankAccount(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> addBnkAccountResponseMutableLiveData.setValue(response), this::onFailure));
        return addBnkAccountResponseMutableLiveData;

    }

}
