package com.linkpcom.mitrafast.MVVM.Client.RateAgent;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.RateAgentRequest;
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
public class RateAgentViewModel extends BaseViewModel {

    @Inject
    public RateAgentViewModel() {
    }


    @Inject
    MutableLiveData<BaseResponse> rateAgentResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestRateAgent(RateAgentRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestRateAgent(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> rateAgentResponseMutableLiveData.setValue(response), this::onFailure));
        return rateAgentResponseMutableLiveData;


    }
}