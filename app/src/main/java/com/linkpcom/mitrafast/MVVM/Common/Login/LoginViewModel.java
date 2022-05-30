package com.linkpcom.mitrafast.MVVM.Common.Login;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.LoginRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.LoginResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class LoginViewModel extends BaseViewModel {

    @Inject
    public LoginViewModel() {
    }


    @Inject
    MutableLiveData<LoginResponse> loginResponseMutableLiveData;


    public MutableLiveData<LoginResponse> requestLogin(LoginRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestLogin(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> loginResponseMutableLiveData.setValue(response), this::onFailure));
        return loginResponseMutableLiveData;


    }




}
