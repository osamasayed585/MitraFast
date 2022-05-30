package com.linkpcom.mitrafast.MVVM.Client.ContactUs;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ContactUsRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.SubjectsResponse;
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
    MutableLiveData<BaseResponse> contactUsResponseMutableLiveData;
    @Inject
    MutableLiveData<SubjectsResponse> subjectsResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestContactUs(ContactUsRequest contactUsRequest) {
        getCompositeDisposable().add(Observable.just(contactUsRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestContactUs(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> contactUsResponseMutableLiveData.setValue(response), this::onFailure));
        return contactUsResponseMutableLiveData;

    }


    public MutableLiveData<SubjectsResponse> requestSubjects( ) {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestSubjects())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> subjectsResponseMutableLiveData.setValue(response), this::onFailure));
        return subjectsResponseMutableLiveData;

    }

}