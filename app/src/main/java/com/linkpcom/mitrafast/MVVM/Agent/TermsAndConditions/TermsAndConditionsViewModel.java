package com.linkpcom.mitrafast.MVVM.Agent.TermsAndConditions;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TermsAndConditionsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class TermsAndConditionsViewModel extends BaseViewModel {

    @Inject
    public TermsAndConditionsViewModel() {
    }


    @Inject
    MutableLiveData<TermsAndConditionsResponse> termsAndConditionsResponseMutableLiveData;


    public MutableLiveData<TermsAndConditionsResponse> requestTermsAndConditions(int id) {
        getCompositeDisposable().add(Observable.just(id)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap((userId) -> getRepository().requestTermsAndConditions(userId))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> termsAndConditionsResponseMutableLiveData.setValue(response), this::onFailure));
        return termsAndConditionsResponseMutableLiveData;

    }
}