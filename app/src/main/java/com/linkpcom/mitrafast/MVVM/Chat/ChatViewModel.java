package com.linkpcom.mitrafast.MVVM.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChatFileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.MessageNotificationRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseDB;
import com.linkpcom.mitrafast.Classes.Utils.FireBaseReferences;
import com.linkpcom.mitrafast.MVVM.$Base.BaseViewModel;
import com.visionstech.whatsappview.ChatNode;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

@Getter
@HiltViewModel
public class ChatViewModel extends BaseViewModel {
    @Inject
    MutableLiveData<BaseResponse> sendNewMessageNotificationResponseMutableLiveData;
    @Inject
    MutableLiveData<BaseResponse> sendFileResponseMutableLiveData;
    @Inject
    MutableLiveData<ChatNode> chatMessageAddedMutableLiveData;
    @Inject
    MutableLiveData<ChatNode> chatMessageChangedMutableLiveData;
    private boolean chatListenerWorking;
    ChildEventListener ChatListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

            chatMessageAddedMutableLiveData.setValue(dataSnapshot.getValue(ChatNode.class));

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
            chatMessageChangedMutableLiveData.setValue(dataSnapshot.getValue(ChatNode.class));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            chatListenerWorking = false;

        }
    };
    //Firebase
    private DatabaseReference chatRef;

    @Inject
    public ChatViewModel() {
    }

    public MutableLiveData<BaseResponse> requestMessageNotification(MessageNotificationRequest request) {
        getCompositeDisposable().add(Observable.just(request)
//                .doOnNext(__ -> getOnLoadingLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestMessageNotification(data))
                .observeOn(AndroidSchedulers.mainThread())
//                .doFinally(() -> getOnLoadingLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> sendNewMessageNotificationResponseMutableLiveData.setValue(response),  this::onFailure));
        return sendNewMessageNotificationResponseMutableLiveData;

    }

    public MutableLiveData<BaseResponse> requestSendChatFile(ChatFileRequest request) {
        getCompositeDisposable().add(Observable.just(request)
                .doOnNext(__ -> getOnLoadingMutableLiveData().setValue(true))
                .observeOn(Schedulers.io())
                .takeWhile(this::isInternetAvailable)
                .flatMap(data -> getRepository().requestSendChatFile(data))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> getOnLoadingMutableLiveData().setValue(false))
                .takeWhile(this::isSuccess)
                .subscribe(response -> sendFileResponseMutableLiveData.setValue(response),  this::onFailure));
        return sendFileResponseMutableLiveData;
    }

    public void startChatListening(String chatId) {
        if (chatRef != null && chatRef.getRef().equals(FireBaseReferences.getChatRef(chatId)) && chatListenerWorking)
            return;

        chatListenerWorking = true;
        chatRef = FireBaseReferences.getChatRef(chatId);
        chatRef.addChildEventListener(ChatListener);

    }

    public void sendMessage(String chatId, ChatNode chatNode) {
        //get a database reference
        DatabaseReference reference = FireBaseReferences.getChatRef(chatId);

        //insert the new message into the chatroom
        reference
                .child(String.valueOf(chatNode.getCreatedAt()))
                .setValue(chatNode);

    }

    public void seeMessage(String chatId, ChatNode chatNode) {
        //get a database reference
        DatabaseReference reference = FireBaseReferences.getChatRef(chatId).child(String.valueOf(chatNode.getCreatedAt()));

        //insert the new message into the chatroom
        reference
                .child(FireBaseDB.status)
                .setValue(4);

    }

    public void deleteMessage(String chatId, ChatNode chatNode) {
        //get a database reference
        DatabaseReference reference = FireBaseReferences.getChatRef(chatId).child(String.valueOf(chatNode.getCreatedAt()));

        //insert the new message into the chatroom
        reference
                .child(FireBaseDB.status)
                .setValue(0);

    }

    public void removeChatListener() {
        chatListenerWorking = false;
        if (chatRef != null)
            chatRef.removeEventListener(ChatListener);

    }


}
