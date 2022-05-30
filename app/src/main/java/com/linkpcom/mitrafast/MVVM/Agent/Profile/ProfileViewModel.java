package com.linkpcom.mitrafast.MVVM.Agent.Profile;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditAgentProfileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AgentProfileResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class ProfileViewModel extends BaseViewModel {

    @Inject
    public ProfileViewModel() {
    }


    @Inject
    MutableLiveData<AgentProfileResponse> profileResponseMutableLiveData;
    @Inject
    MutableLiveData<AgentProfileResponse> editProfileResponseMutableLiveData;


    public MutableLiveData<AgentProfileResponse> requestProfile() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestAgentProfile())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> profileResponseMutableLiveData.setValue(response), this::onFailure));
        return profileResponseMutableLiveData;

    }


    public MutableLiveData<AgentProfileResponse> requestEditProfile(EditAgentProfileRequest editAgentProfileRequest) {
        getCompositeDisposable().add(Observable.just(editAgentProfileRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestEditAgentProfile(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> editProfileResponseMutableLiveData.setValue(response), this::onFailure));
        return editProfileResponseMutableLiveData;

    }
}