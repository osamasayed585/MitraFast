package com.linkpcom.mitrafast.MVVM.Common.FAQ;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FAQResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class FAQViewModel extends BaseViewModel {

    @Inject
    public FAQViewModel() {
    }


    @Inject
    MutableLiveData<FAQResponse> FAQResponseMutableLiveData;


    public MutableLiveData<FAQResponse> requestFAQ() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestFAQ())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> FAQResponseMutableLiveData.setValue(response), this::onFailure));
        return FAQResponseMutableLiveData;

    }
}