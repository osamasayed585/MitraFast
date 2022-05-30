package com.linkpcom.mitrafast.MVVM.Agent.AddTicket;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketReplyRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AddTicketRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TicketTypesResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class AddTicketViewModel extends BaseViewModel {

    @Inject
    public AddTicketViewModel() {
    }



    @Inject
    MutableLiveData<TicketTypesResponse> ticketTypesResponseMutableLiveData;


    @Inject
    MutableLiveData<BaseResponse> addTicketResponseMutableLiveData;
    public MutableLiveData<BaseResponse> requestAddTicket(AddTicketRequest addTicketRequest) {
        getCompositeDisposable().add(Observable.just(addTicketRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAgentAddTicket(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> addTicketResponseMutableLiveData.setValue(response), this::onFailure));
        return addTicketResponseMutableLiveData;
    }




    public MutableLiveData<TicketTypesResponse> requestTicketTypes() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestTicketTypes())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> ticketTypesResponseMutableLiveData.setValue(response), this::onFailure));
        return ticketTypesResponseMutableLiveData;
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