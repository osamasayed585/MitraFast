package com.linkpcom.mitrafast.MVVM.Client.AddTicketReply;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketReplyRequest;
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
public class AddTicketReplyViewModel extends BaseViewModel {

    @Inject
    public AddTicketReplyViewModel() {
    }


    @Inject
    MutableLiveData<BaseResponse> addTicketReplyResponseMutableLiveData;
    public MutableLiveData<BaseResponse> requestAddTicketReply(AddTicketReplyRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAddTicketReply(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> addTicketReplyResponseMutableLiveData.setValue(response), this::onFailure));
        return addTicketReplyResponseMutableLiveData;

    }
}