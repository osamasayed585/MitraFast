package com.linkpcom.mitrafast.MVVM.Client.AddPaymentCard;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentCardRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddPaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.PaymentRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.PaymentCardResponse;
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
public class AddPaymentCardViewModel extends BaseViewModel {

    @Inject
    MutableLiveData<BaseResponse> cardIsValid;

    @Inject
    public AddPaymentCardViewModel() {
    }


    @Inject
    MutableLiveData<PaymentCardResponse> addPaymentCardResponseMutableLiveData;
    @Inject
    MutableLiveData<PaymentResponse>cardValidation;


    public MutableLiveData<PaymentCardResponse> requestAddPaymentCard(AddPaymentCardRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAddPaymentCard(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> addPaymentCardResponseMutableLiveData.setValue(response), this::onFailure));
        return addPaymentCardResponseMutableLiveData;

    }


    public MutableLiveData<PaymentResponse> requestValidateCard(AddPaymentRequest creditcard) {
        getCompositeDisposable().add(
          Observable.just(creditcard)
          .doOnNext(pb->getOnLoadingMutableLiveData().setValue(true))
          .observeOn(Schedulers.io())
          .flatMap(key->getRepository().requestValidateCard(key))
          .observeOn(AndroidSchedulers.mainThread())
          .doFinally(()->getOnLoadingMutableLiveData().setValue(false))
          .subscribe(
                  o -> cardValidation.setValue(o),this::onFailure
          )
        );
        return cardValidation;
    }


}