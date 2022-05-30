package com.linkpcom.mitrafast.MVVM.Client.Coupons;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CouponsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class CouponsViewModel extends BaseViewModel {

    @Inject
    public CouponsViewModel() {
    }


    @Inject
    MutableLiveData<CouponsResponse> couponsResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> addCouponResponseMutableLiveData;


    public MutableLiveData<CouponsResponse> requestCoupons() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestCoupons())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> couponsResponseMutableLiveData.setValue(response), this::onFailure));
        return couponsResponseMutableLiveData;

    }


    public MutableLiveData<BaseResponse> requestAddCoupon(String code) {
        getCompositeDisposable().add(Observable.just(code)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAddCoupon(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> addCouponResponseMutableLiveData.setValue(response), this::onFailure));
        return addCouponResponseMutableLiveData;

    }

}