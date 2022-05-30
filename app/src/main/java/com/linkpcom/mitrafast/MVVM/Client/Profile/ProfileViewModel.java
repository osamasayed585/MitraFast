package com.linkpcom.mitrafast.MVVM.Client.Profile;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.EditClientProfileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.ClientProfileResponse;
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
    MutableLiveData<ClientProfileResponse> profileResponseMutableLiveData;
    @Inject
    MutableLiveData<ClientProfileResponse> editProfileResponseMutableLiveData;


    public MutableLiveData<ClientProfileResponse> requestClientProfile() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestClientProfile())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> profileResponseMutableLiveData.setValue(response), this::onFailure));
        return profileResponseMutableLiveData;

    }


    public MutableLiveData<ClientProfileResponse> requestEditProfile(EditClientProfileRequest editClientProfileRequest) {
        getCompositeDisposable().add(Observable.just(editClientProfileRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestEditClientProfile(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> editProfileResponseMutableLiveData.setValue(response), this::onFailure));
        return editProfileResponseMutableLiveData;

    }
}