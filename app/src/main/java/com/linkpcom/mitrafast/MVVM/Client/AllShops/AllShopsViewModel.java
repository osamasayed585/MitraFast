package com.linkpcom.mitrafast.MVVM.Client.AllShops;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AllShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

@Getter
@HiltViewModel
public class AllShopsViewModel extends BaseViewModel  {
    @Inject
    public AllShopsViewModel() {
    }


    @Inject
    MutableLiveData<ShopsResponse> shopsResponseMutableLiveData;


    public MutableLiveData<ShopsResponse> requestAllShops(AllShopsRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAllShops(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> shopsResponseMutableLiveData.setValue(response), this::onFailure));
        return shopsResponseMutableLiveData;


    }

}
