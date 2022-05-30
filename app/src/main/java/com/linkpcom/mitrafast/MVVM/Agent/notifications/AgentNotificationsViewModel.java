package com.linkpcom.mitrafast.MVVM.Agent.notifications;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NotificationsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

@Getter
@HiltViewModel
public class AgentNotificationsViewModel extends BaseViewModel {


    @Inject
    public AgentNotificationsViewModel() { }

    @Inject
    MutableLiveData<NotificationsResponse> notificationsResponseMutableLiveData;
    public MutableLiveData<NotificationsResponse> requestNotifications() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestNotifications())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> notificationsResponseMutableLiveData.setValue(response), this::onFailure));
        return notificationsResponseMutableLiveData;

    }
}
