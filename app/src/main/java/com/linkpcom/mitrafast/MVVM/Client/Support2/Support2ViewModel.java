package com.linkpcom.mitrafast.MVVM.Client.Support2;

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
public class Support2ViewModel extends BaseViewModel {

    @Inject
    public Support2ViewModel() {
    }


    @Inject
    MutableLiveData<TermsAndConditionsResponse> termsResponseMutableLiveData;




    public MutableLiveData<TermsAndConditionsResponse> requestTerms(int userTypeId) {
        getCompositeDisposable().add(Observable.just(userTypeId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestTerms(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> termsResponseMutableLiveData.setValue(response), this::onFailure));
        return termsResponseMutableLiveData;


    }

}
