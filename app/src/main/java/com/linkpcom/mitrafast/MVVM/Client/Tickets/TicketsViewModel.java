package com.linkpcom.mitrafast.MVVM.Client.Tickets;

import androidx.lifecycle.MutableLiveData;

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
public class TicketsViewModel extends BaseViewModel {

    @Inject
    public TicketsViewModel() {
    }


    @Inject
    MutableLiveData<TicketsResponse> ticketsResponseMutableLiveData;
    public MutableLiveData<TicketsResponse> requestClientTickets() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestClientTickets())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> ticketsResponseMutableLiveData.setValue(response), this::onFailure));
        return ticketsResponseMutableLiveData;

    }

    @Inject
    MutableLiveData<ReplyResponse> replyResponseMutableLiveData;
    public MutableLiveData<ReplyResponse> requestClientReply(int supportTicketId) {
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