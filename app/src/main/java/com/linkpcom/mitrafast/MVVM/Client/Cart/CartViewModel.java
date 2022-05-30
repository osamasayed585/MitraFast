package com.linkpcom.mitrafast.MVVM.Client.Cart;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.MakeOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CouponResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.OrderDetailsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ValueAddedTaxResponse;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
@Getter
@HiltViewModel
public class CartViewModel extends BaseViewModel {

    @Inject
    public CartViewModel() {
    }


    @Inject
    MutableLiveData<ValueAddedTaxResponse> valueAddedTaxResponseMutableLiveData;
    @Inject
    MutableLiveData<CouponResponse> couponResponseMutableLiveData;

    @Inject
    MutableLiveData<OrderDetailsResponse> makeOrderResponseMutableLiveData;

    @Inject
    MutableLiveData<List<CartProduct>> findProductByIdResponseMutableLiveData;
    @Inject
    MutableLiveData<List<CartProduct>> allCartProductsResponseMutableLiveData;
    @Inject
    MutableLiveData<Float> totalCostResponseMutableLiveData;
    @Inject
    MutableLiveData<Boolean> deleteCartItemsMutableLiveData;
    @Inject
    MutableLiveData<Boolean> itemDeletedResponseMutableLiveData;
    @Inject
    MutableLiveData<Boolean> itemUpdatedResponseMutableLiveData;
    @Inject
    MutableLiveData<PaymentResponse> paymentResponseMutableLiveData;


    public MutableLiveData<OrderDetailsResponse> requestMakeOrder(MakeOrderRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestMakeOrder(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> makeOrderResponseMutableLiveData.setValue(response), this::onFailure));
        return makeOrderResponseMutableLiveData;

    }
    public MutableLiveData<PaymentResponse> requestPayment(PaymentRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestPayment(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> paymentResponseMutableLiveData.setValue(response), this::onFailure));
        return paymentResponseMutableLiveData;
    }

    public MutableLiveData<ValueAddedTaxResponse> requestValueAddedTax() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestValueAddedTax())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> valueAddedTaxResponseMutableLiveData.setValue(response), this::onFailure));
        return valueAddedTaxResponseMutableLiveData;

    }
    public MutableLiveData<CouponResponse> requestCoupon(String coupon) {
        getCompositeDisposable().add(Observable.just(coupon)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestCoupon(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> couponResponseMutableLiveData.setValue(response), this::onFailure));
        return couponResponseMutableLiveData;

    }

    public void requestAddToCart(CartProduct cartProduct) {
        getCompositeDisposable().add(Observable.just(cartProduct)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(object -> getRepository().InsertProduct(object))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> {
                        }, this::onFailure));
    }


    public void requestUpdateById(int cartProductId, int count) {
        getCompositeDisposable().add(Observable.just(cartProductId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().updateProductCountById(data, count))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> itemUpdatedResponseMutableLiveData.setValue(true), this::onFailure));
    }

    public void requestDeleteProductById(int cartProductId) {
        getCompositeDisposable().add(Observable.just(cartProductId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().deleteProductById(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .subscribe(response -> itemDeletedResponseMutableLiveData.setValue(true), this::onFailure));
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

}