package com.linkpcom.mitrafast.MVVM.Client.SearchShops;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SearchShopsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
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
public class SearchShopsViewModel extends BaseViewModel {


    @Inject
    public SearchShopsViewModel() {
    }


    @Inject
    MutableLiveData<ShopsResponse> shopsResponseMutableLiveData;


    public MutableLiveData<ShopsResponse> requestSearchShops(SearchShopsRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestSearchShops(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> shopsResponseMutableLiveData.setValue(response), this::onFailure));
        return shopsResponseMutableLiveData;
    }

    @Inject
    public MutableLiveData<BaseResponse> responseAddShopFavorite;
    public MutableLiveData<BaseResponse> requestAddFavoriteShop(int shop_Id) {
        getCompositeDisposable().add(Observable.just(shop_Id)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(id -> getRepository().requestAddFavorite(id))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> responseAddShopFavorite.setValue(response), this::onFailure));
        return responseAddShopFavorite;
    }

    @Inject
    public MutableLiveData<BaseResponse> responseRemoveFavoriteShop;
    public MutableLiveData<BaseResponse> requestRemoveFavoriteShop(int shop_Id) {
        getCompositeDisposable().add(Observable.just(shop_Id)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(id -> getRepository().requestRemoveFavorite(id))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> responseRemoveFavoriteShop.setValue(response), this::onFailure));
        return responseRemoveFavoriteShop;
    }
}
