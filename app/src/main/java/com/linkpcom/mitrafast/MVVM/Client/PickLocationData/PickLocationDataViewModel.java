package com.linkpcom.mitrafast.MVVM.Client.PickLocationData;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddFavoritePlacesRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class PickLocationDataViewModel extends BaseViewModel {

    @Inject
    public PickLocationDataViewModel() {
    }


    @Inject
    MutableLiveData<BaseResponse> pickLocationResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestAddAddress(AddFavoritePlacesRequest favoritePlacesRequest) {
        getCompositeDisposable().add(Observable.just(favoritePlacesRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(request -> getRepository().requestAddAddress(request))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> pickLocationResponseMutableLiveData.setValue(response), this::onFailure));
        return pickLocationResponseMutableLiveData;


    }


}
