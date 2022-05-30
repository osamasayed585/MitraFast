package com.linkpcom.mitrafast.MVVM.Client.ProductDetails;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ProductDetailsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SameProductRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductDetailsResponse;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;


import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class ProductDetailsViewModel extends BaseViewModel {

    @Inject
    public ProductDetailsViewModel() {
    }


    @Inject
    MutableLiveData<ProductDetailsResponse> productDetailsResponseMutableLiveData;


    @Inject
    MutableLiveData<List<CartProduct>> findSameProductResponseMutableLiveData;
    @Inject
    MutableLiveData<List<CartProduct>> isAnotherStoreExistResponseMutableLiveData;
    @Inject
    MutableLiveData<Long> addProductResponseMutableLiveData;
    @Inject
    MutableLiveData<Integer> updateCountByIdResponseMutableLiveData;
    @Inject
    MutableLiveData<Boolean> deleteCartItemsMutableLiveData;
    @Inject
    MutableLiveData<List<CartProduct>> allCartProductsResponseMutableLiveData;
    @Inject
    MutableLiveData<Float> totalCostResponseMutableLiveData;


    public MutableLiveData<ProductDetailsResponse> requestProductDetails(ProductDetailsRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestProductDetails(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> productDetailsResponseMutableLiveData.setValue(response), this::onFailure));
        return productDetailsResponseMutableLiveData;

    }
    public void requestCart() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(object -> getRepository().getAllProducts())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> allCartProductsResponseMutableLiveData.setValue(response), this::onFailure));
    }

    public void requestAddToCart(CartProduct cartProduct) {
        getCompositeDisposable().add(Observable.just(cartProduct)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(object -> getRepository().InsertProduct(object))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> addProductResponseMutableLiveData.setValue(response), this::onFailure));
    }

    public void requestFindSameProduct(SameProductRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().findSameProduct(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> findSameProductResponseMutableLiveData.setValue(response), this::onFailure));
    }

    public void requestIsAnotherStoreExist(int storeId) {
        getCompositeDisposable().add(Observable.just(storeId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestIsAnotherStoreExist(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> isAnotherStoreExistResponseMutableLiveData.setValue(response), this::onFailure));
    }

    public void requestUpdateById(int cartProductId, int count) {
        getCompositeDisposable().add(Observable.just(cartProductId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().updateProductCountById(data, count))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> updateCountByIdResponseMutableLiveData.setValue(response), this::onFailure));
    }

    public MutableLiveData<Boolean> requestDeleteCartItems() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().deleteAllProducts())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> deleteCartItemsMutableLiveData.setValue(true), this::onFailure));
        return deleteCartItemsMutableLiveData;
    }
    public void requestTotalCost() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(object -> getRepository().getTotalProductsCost())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> totalCostResponseMutableLiveData.setValue(response), this::onFailure));
    }

}