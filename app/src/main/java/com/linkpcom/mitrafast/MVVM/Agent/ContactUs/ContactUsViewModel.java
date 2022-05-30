package com.linkpcom.mitrafast.MVVM.Agent.ContactUs;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ReplyResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TicketsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class ContactUsViewModel extends BaseViewModel {

    @Inject
    public ContactUsViewModel() {
    }


    @Inject
    MutableLiveData<TicketsResponse> ticketsResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> deleteTicketResponseMutableLiveData;


    public MutableLiveData<TicketsResponse> requestTickets() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestTickets())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> ticketsResponseMutableLiveData.setValue(response), this::onFailure));
        return ticketsResponseMutableLiveData;

    }
    public MutableLiveData<BaseResponse> requestDeleteTicket(int ticketId) {
        getCompositeDisposable().add(Observable.just(ticketId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestDeleteTicket(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> deleteTicketResponseMutableLiveData.setValue(response), this::onFailure));
        return deleteTicketResponseMutableLiveData;

    }

    @Inject
    MutableLiveData<ReplyResponse> replyResponseMutableLiveData;
    public MutableLiveData<ReplyResponse> requestAgentReply(int supportTicketId) {
        getCompositeDisposable().add(Observable.just(supportTicketId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(id -> getRepository().requestClientReply(id))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> replyResponseMutableLiveData.setValue(response), this::onFailure));
        return replyResponseMutableLiveData;
    }

}