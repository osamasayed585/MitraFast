package com.linkpcom.mitrafast.MVVM.Client.Locating;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.AgentNode;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.CancelOrderRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.CancelOrderReasonsResponse;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
@Getter
@HiltViewModel
public class LocatingViewModel extends BaseViewModel {

    @Inject
    public LocatingViewModel() {
    }

    @Inject
    MutableLiveData<CancelOrderReasonsResponse> cancelOrderReasonsResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> cancelOrderResponseMutableLiveData;


    public MutableLiveData<CancelOrderReasonsResponse> requestCancelOrderReasons() {
        getCompositeDisposable().add(Observable.just("")
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(__ -> getRepository().requestClientCancelOrderReasons())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> cancelOrderReasonsResponseMutableLiveData.setValue(response), this::onFailure));
        return cancelOrderReasonsResponseMutableLiveData;
    }

    public MutableLiveData<BaseResponse> requestUserCancelOrder(CancelOrderRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestUserCancelOrder(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> cancelOrderResponseMutableLiveData.setValue(response), this::onFailure));
        return cancelOrderResponseMutableLiveData;


    }

    private DatabaseReference agentsRef;
    private boolean agentsListenerWorking;
    ChildEventListener agentsListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
            AgentNode agentNode = new AgentNode();
            agentNode.setDriverId(dataSnapshot.getKey());

            agentsMutableLiveData.setValue(agentNode);

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            agentsListenerWorking = false;
        }
    };


    @Inject
    MutableLiveData<AgentNode> agentsMutableLiveData ;



    public void startAgentsListening(String orderId) {
        if (agentsRef != null && agentsRef.getRef().equals(FireBaseReferences.getAgentsRef(orderId)) && agentsListenerWorking)
            return;

        agentsListenerWorking = true;
        agentsRef = FireBaseReferences.getAgentsRef(orderId);
        agentsRef.addChildEventListener(agentsListener);

    }

    public void removeAllListeners() {
        agentsListenerWorking = false;
        if (agentsRef != null)
            agentsRef.removeEventListener(agentsListener);

    }


    @Override
    protected void onCleared() {
        super.onCleared();
        removeAllListeners();
    }
}
