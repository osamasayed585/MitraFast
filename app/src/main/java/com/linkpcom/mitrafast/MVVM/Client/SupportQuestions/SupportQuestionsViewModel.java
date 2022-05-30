package com.linkpcom.mitrafast.MVVM.Client.SupportQuestions;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.FAQResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class SupportQuestionsViewModel extends BaseViewModel {

    @Inject
    public SupportQuestionsViewModel() {
    }


    @Inject
    MutableLiveData<FAQResponse> FAQResponseMutableLiveData;



    public MutableLiveData<FAQResponse> requestFAQ(int supportSectionId) {
        getCompositeDisposable().add(Observable.just(supportSectionId)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestFAQ(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> FAQResponseMutableLiveData.setValue(response), this::onFailure));
        return FAQResponseMutableLiveData;

    }
}