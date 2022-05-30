package com.linkpcom.mitrafast.MVVM.Client.ShopDetails;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ProductsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ShopDetailsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CategoriesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductCountResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ProductsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ShopDetailsResponse;
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
public class ShopDetailsViewModel extends BaseViewModel {

    @Inject
    public ShopDetailsViewModel() {
    }


    @Inject
    MutableLiveData<ProductsResponse> productsResponseMutableLiveData;
    @Inject
    MutableLiveData<CategoriesResponse> shopCategoriesResponseMutableLiveData;
    @Inject
    MutableLiveData<List<CartProduct>> allCartProductsResponseMutableLiveData;
    @Inject
    MutableLiveData<Float> totalCostResponseMutableLiveData;
    @Inject
    MutableLiveData<ProductCountResponse> productCountResponseMutableLiveData;
    @Inject
    MutableLiveData<ShopDetailsResponse> shopDetailsResponseMutableLiveData;




    public MutableLiveData<ShopDetailsResponse> requestShopDetails(ShopDetailsRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestShopDetails(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> shopDetailsResponseMutableLiveData.setValue(response), this::onFailure));
        return shopDetailsResponseMutableLiveData;
    }

    public MutableLiveData<ProductsResponse> requestProducts(ProductsRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestProducts(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> productsResponseMutableLiveData.setValue(response), this::onFailure));
        return productsResponseMutableLiveData;
    }

    public MutableLiveData<CategoriesResponse> requestShopCategories(int shopId) {
        getCompositeDisposable().add(Observable.just(shopId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestShopCategories(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> shopCategoriesResponseMutableLiveData.setValue(response), this::onFailure));
        return shopCategoriesResponseMutableLiveData;
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


    public void requestProductCount(int productId) {
        getCompositeDisposable().add(Observable.just(productId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestProductCount(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> productCountResponseMutableLiveData.setValue(ProductCountResponse.builder()
                                .id(productId)
                                .count(response)
                                .build()), this::onFailure));
    }
}
