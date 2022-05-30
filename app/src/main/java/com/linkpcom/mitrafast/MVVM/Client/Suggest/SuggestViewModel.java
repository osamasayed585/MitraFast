package com.linkpcom.mitrafast.MVVM.Client.Suggest;

import androidx.lifecycle.MutableLiveData;

import com.linkpcom.mitrafast.Classes.REST.Models.Requests.SuggestRequest;
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
public class SuggestViewModel extends BaseViewModel {

    @Inject
    public SuggestViewModel() {
    }


    @Inject
    MutableLiveData<BaseResponse> suggestResponseMutableLiveData;


    public MutableLiveData<BaseResponse> requestSuggest(SuggestRequest suggestRequest) {
        getCompositeDisposable().add(Observable.just(suggestRequest)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestSuggest(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> suggestResponseMutableLiveData.setValue(response), this::onFailure));
        return suggestResponseMutableLiveData;

    }


}