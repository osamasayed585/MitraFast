package com.linkpcom.mitrafast.MVVM.Client.Support3;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.SupportResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class Support3ViewModel extends BaseViewModel {

    @Inject
    public Support3ViewModel() {
    }


    @Inject
    MutableLiveData<SupportResponse> supportResponseMutableLiveData;


    public MutableLiveData<SupportResponse> requestSupport() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestSupport())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> supportResponseMutableLiveData.setValue(response), this::onFailure));
        return supportResponseMutableLiveData;

    }
}
