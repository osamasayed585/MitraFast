package com.linkpcom.mitrafast.MVVM.Agent.RateClient;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.RateClientRequest;
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
public class RateClientViewModel extends BaseViewModel {

    @Inject
    public RateClientViewModel() {
    }


    @Inject
    MutableLiveData<BaseResponse> rateClientResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestRateClient(RateClientRequest clientRateRequest) {
        getCompositeDisposable().add(Observable.just(clientRateRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestRateClient(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> rateClientResponseMutableLiveData.setValue(response), this::onFailure));
        return rateClientResponseMutableLiveData;


    }
}