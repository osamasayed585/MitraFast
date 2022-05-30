package com.linkpcom.mitrafast.MVVM.Client.FavoritePlaces;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FavoritePlacesResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class FavoritePlacesViewModel extends BaseViewModel {

    @Inject
    public FavoritePlacesViewModel() {
    }


    @Inject
    MutableLiveData<FavoritePlacesResponse> favoritePlacesResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> removeFavoritePlaceResponseMutableLiveData;


    public MutableLiveData<FavoritePlacesResponse> requestFavoritePlaces(int page) {
        getCompositeDisposable().add(Observable.just(page)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestFavoritePlaces(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> favoritePlacesResponseMutableLiveData.setValue(response), this::onFailure));
        return favoritePlacesResponseMutableLiveData;

    }

    public MutableLiveData<BaseResponse> requestRemoveFavoritePlace(int location_id) {
        getCompositeDisposable().add(Observable.just(location_id)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(request -> getRepository().requestRemoveFavoritePlace(request))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> removeFavoritePlaceResponseMutableLiveData.setValue(response), this::onFailure));
        return removeFavoritePlaceResponseMutableLiveData;

    }
}