package com.linkpcom.mitrafast.MVVM.Common.ActivateAccount;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ClientProfileResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import dagger.hilt.android.lifecycle.HiltViewModel;

@Getter
@HiltViewModel
public class ActivateAccountViewModel extends BaseViewModel {

    @Inject
    public ActivateAccountViewModel() {
    }

    @Inject
    MutableLiveData<ClientProfileResponse> activateAccountResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> resendCodeResponseMutableLiveData;


    public MutableLiveData<ClientProfileResponse> requestActivateAccount() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestActivateAccount())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> activateAccountResponseMutableLiveData.setValue(response), this::onFailure))
        ;
        return activateAccountResponseMutableLiveData;


    }


}
