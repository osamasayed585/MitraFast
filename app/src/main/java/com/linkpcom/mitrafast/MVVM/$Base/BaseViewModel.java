package com.linkpcom.mitrafast.MVVM.$Base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.linkpcom.mitrafast.Classes.REST.Models.Responses.AuthResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Repository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import okhttp3.MultipartBody;
import timber.log.Timber;

@Getter
@HiltViewModel
public class BaseViewModel extends ViewModel {
    @Inject
    public BaseViewModel() {
    }
    @Inject
    Repository repository;
    @Inject
    CompositeDisposable compositeDisposable;
    @Inject
    MutableLiveData<Boolean> onLoadingMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> onApiErrorMutableLiveData;




    @Inject
    MutableLiveData<AuthResponse> updateImageResponseMutableLiveData;


    public MutableLiveData<AuthResponse> requestUpdateImage(MultipartBody.Part image) {
        getCompositeDisposable().add(Observable.just(image)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestUpdateImage(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> updateImageResponseMutableLiveData.setValue(response), this::onFailure));
        return updateImageResponseMutableLiveData;

    }


    protected boolean isSuccess(BaseResponse response) {
        if (!response.isSuccess()) {
            onApiErrorMutableLiveData.setValue(response);
            Timber.e("From new BaseViewModel %s", response.getMessage());
            return false;
        } else
            return true;

    }
    protected void onFailure(Throwable throwable) {
        onApiErrorMutableLiveData.setValue(BaseResponse.builder().message(throwable.toString()).success(false).build());
        Timber.e(throwable);

    }

    public boolean isInternetAvailable(Object object) {
        return true;
//        try {
//            int timeoutMs = 5000;
//            Socket sock = new Socket();
//            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
//
//            sock.connect(sockaddr, timeoutMs);
//            sock.close();
//
//            return true;
//        } catch (IOException e) {
//            onLoadingMutableLiveData.postValue(false);
//            onApiErrorMutableLiveData.postValue(BaseResponse.builder().statusCode(503).success(false).build());
//            return false;
//        }
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
