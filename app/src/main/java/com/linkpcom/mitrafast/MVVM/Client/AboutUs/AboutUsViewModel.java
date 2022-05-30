package com.linkpcom.mitrafast.MVVM.Client.AboutUs;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AboutUsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class AboutUsViewModel extends BaseViewModel {

    @Inject
    public AboutUsViewModel() {
    }


    @Inject
    MutableLiveData<AboutUsResponse> aboutUsResponseMutableLiveData;


    public MutableLiveData<AboutUsResponse> requestAboutUs() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestAboutUs())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> aboutUsResponseMutableLiveData.setValue(response), this::onFailure));
        return aboutUsResponseMutableLiveData;

    }
}