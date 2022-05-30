package com.linkpcom.mitrafast.MVVM.Client.ChargeBalance;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

@Getter
@HiltViewModel
public class ChargeBalanceViewModel extends BaseViewModel {

    @Inject
    public ChargeBalanceViewModel() {
    }

    @Inject
    MutableLiveData<BaseResponse> chargeResponseMutableLiveData;
    public MutableLiveData<BaseResponse> requestCharge(ChargeRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestCharge(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> chargeResponseMutableLiveData.setValue(response), this::onFailure));
        return chargeResponseMutableLiveData;
    }
}
