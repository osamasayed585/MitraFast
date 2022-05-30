package com.linkpcom.mitrafast.MVVM.Agent.Register;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.AgentRegisterRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CompaniesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.NationalitiesResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.RegisterResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.TermsAndConditionsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class RegisterViewModel extends BaseViewModel {

    @Inject
    public RegisterViewModel() {
    }

    @Inject
    MutableLiveData<NationalitiesResponse> nationalitiesResponseMutableLiveData;

    @Inject
    MutableLiveData<CompaniesResponse> companiesResponseMutableLiveData;

    @Inject
    MutableLiveData<RegisterResponse> registerResponseMutableLiveData;
    @Inject
    MutableLiveData<TermsAndConditionsResponse> termsResponseMutableLiveData;


    public MutableLiveData<RegisterResponse> requestRegister(AgentRegisterRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestAgentRegister(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> registerResponseMutableLiveData.setValue(response), this::onFailure));
        return registerResponseMutableLiveData;


    }

    public MutableLiveData<NationalitiesResponse> requestNationalities() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestNationalities())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> nationalitiesResponseMutableLiveData.setValue(response), this::onFailure));
        return nationalitiesResponseMutableLiveData;
    }








    public MutableLiveData<TermsAndConditionsResponse> requestTerms(int userTypeId) {
        getCompositeDisposable().add(Observable.just(userTypeId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestTerms(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> termsResponseMutableLiveData.setValue(response), this::onFailure));
        return termsResponseMutableLiveData;


    }

    public MutableLiveData<CompaniesResponse> requestCompanies() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestCompanies())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> companiesResponseMutableLiveData.setValue(response), this::onFailure));
        return companiesResponseMutableLiveData;


    }


}
