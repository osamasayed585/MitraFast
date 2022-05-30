package com.linkpcom.mitrafast.MVVM.Client.Offers;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OffersResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class OffersViewModel extends BaseViewModel {

    @Inject
    public OffersViewModel() {
    }


    @Inject
    MutableLiveData<OffersResponse> offersResponseMutableLiveData;


    public MutableLiveData<OffersResponse> requestOffers() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestOffers())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> offersResponseMutableLiveData.setValue(response), this::onFailure));
        return offersResponseMutableLiveData;

    }

}