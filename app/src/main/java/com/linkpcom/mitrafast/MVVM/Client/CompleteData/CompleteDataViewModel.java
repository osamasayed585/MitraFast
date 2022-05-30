package com.linkpcom.mitrafast.MVVM.Client.CompleteData;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CompleteDataRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.RegisterResponse;
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
public class CompleteDataViewModel extends BaseViewModel {

    @Inject
    public CompleteDataViewModel() {
    }


    @Inject
    MutableLiveData<RegisterResponse> completeDataResponseMutableLiveData;
    @Inject
    MutableLiveData<TermsAndConditionsResponse> termsResponseMutableLiveData;


    public MutableLiveData<RegisterResponse> requestCompleteData(CompleteDataRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestCompleteData(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> completeDataResponseMutableLiveData.setValue(response), this::onFailure));
        return completeDataResponseMutableLiveData;


    }

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
