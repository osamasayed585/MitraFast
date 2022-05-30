package com.linkpcom.mitrafast.MVVM.Client.PaymentCards;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentCard;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeBalanceRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChargeRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentCardsResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@Getter
@HiltViewModel
public class PaymentCardsViewModel extends BaseViewModel {

    @Inject
    MutableLiveData<BaseResponse> cardIsValid;

    @Inject
    public PaymentCardsViewModel() {
    }


    @Inject
    MutableLiveData<PaymentCardsResponse> paymentCardsResponseMutableLiveData;

    public MutableLiveData<PaymentCardsResponse> requestPaymentCards() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestPaymentCards())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> paymentCardsResponseMutableLiveData.setValue(response), this::onFailure));
        return paymentCardsResponseMutableLiveData;
    }


    @Inject
    MutableLiveData<BaseResponse> deletePaymentCardResponseMutableLiveData;

    public MutableLiveData<BaseResponse> requestDeletePaymentCard(int paymentId) {
        getCompositeDisposable().add(Observable.just(paymentId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestDeletePaymentCard(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> deletePaymentCardResponseMutableLiveData.setValue(response), this::onFailure));
        return deletePaymentCardResponseMutableLiveData;
    }

    @Inject
    MutableLiveData<BaseResponse> chargeResponseMutableLiveData;

    public MutableLiveData<BaseResponse> requestCharge(ChargeRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestCharge(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> chargeResponseMutableLiveData.setValue(response), this::onFailure));
        return chargeResponseMutableLiveData;
    }

    @Inject
    MutableLiveData<PaymentResponse> paymentResponseMutableLiveData;

    public MutableLiveData<PaymentResponse> requestChargeBalance(ChargeBalanceRequest addTicketRequest) {
        getCompositeDisposable().add(Observable.just(addTicketRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestChargeBalance(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> paymentResponseMutableLiveData.setValue(response), this::onFailure));
        return paymentResponseMutableLiveData;
    }

    public MutableLiveData<BaseResponse> requestCardValid(int credit_card_id) {
        getCompositeDisposable().add(
                Observable.just(credit_card_id).doOnNext(pb -> getOnLoadingMutableLiveData().setValue(true))
                        .observeOn(Schedulers.io())
                        .flatMap(key -> getRepository().requestCardValid(key))
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                        .subscribe(response -> cardIsValid.setValue(response), this::onFailure)
        );
        return cardIsValid;
    }

}