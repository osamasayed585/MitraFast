package com.linkpcom.mitrafast.MVVM.Common.Intro;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.IntroSlidersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

@Getter
@HiltViewModel
public class IntroViewModel extends BaseViewModel {


    @Inject
    public IntroViewModel() {
    }


    @Inject
    MutableLiveData<IntroSlidersResponse> introSliderResponseMutableLiveData;


    public MutableLiveData<IntroSlidersResponse> requestSliders() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestSliders())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> introSliderResponseMutableLiveData.setValue(response), this::onFailure));
        return introSliderResponseMutableLiveData;


    }

}
