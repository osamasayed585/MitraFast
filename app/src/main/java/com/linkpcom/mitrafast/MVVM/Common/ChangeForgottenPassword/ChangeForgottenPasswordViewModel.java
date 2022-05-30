package com.linkpcom.mitrafast.MVVM.Common.ChangeForgottenPassword;

import androidx.lifecycle.MutableLiveData;

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
public class ChangeForgottenPasswordViewModel extends BaseViewModel {

    @Inject
    public ChangeForgottenPasswordViewModel() {
    }

    @Inject
    MutableLiveData<BaseResponse> changeForgottenPasswordResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestChangeForgottenPassword(String password) {
        getCompositeDisposable().add(Observable.just(password)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestChangeForgottenPassword(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> changeForgottenPasswordResponseMutableLiveData.setValue(response), this::onFailure))
        ;
        return changeForgottenPasswordResponseMutableLiveData;


    }


}
