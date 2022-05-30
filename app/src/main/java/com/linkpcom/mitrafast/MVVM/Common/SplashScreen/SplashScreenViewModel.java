package com.linkpcom.mitrafast.MVVM.Common.SplashScreen;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;


import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class SplashScreenViewModel extends BaseViewModel {

    @Inject
    public SplashScreenViewModel() {
    }


    @Inject
    MutableLiveData<Boolean> deleteCartItemsMutableLiveData;

    public MutableLiveData<Boolean> requestDeleteCartItems() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().deleteAllProducts())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> deleteCartItemsMutableLiveData.setValue(true), this::onFailure));
        return deleteCartItemsMutableLiveData;
    }
}
