package com.linkpcom.mitrafast.MVVM.Shop.ShopRegister;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopRegisterRequest;
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
public class ShopRegisterViewModel extends BaseViewModel {

    @Inject
    public ShopRegisterViewModel() {
    }


    /*
     * Register Countries
     */
    @Inject
    MutableLiveData<BaseResponse> registerShopResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestShopRegistration(ShopRegisterRequest shopRegisterRequest) {
        getCompositeDisposable().add(Observable.just(shopRegisterRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestShopRegister(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> registerShopResponseMutableLiveData.setValue(response), this::onFailure));

        return registerShopResponseMutableLiveData;
    }

}