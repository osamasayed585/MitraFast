package com.linkpcom.mitrafast.MVVM.Chat;

import static com.linkpcom.mitrafast.Classes.Others.CONSTANTS.INTENTS.OBJECT;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.ChatFileRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Requests.MessageNotificationRequest;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.Classes.Utils.StaticMethods;
import com.linkpcom.mitrafast.MVVM.$Base.BaseActivity;
import com.linkpcom.mitrafast.R;
import com.linkpcom.mitrafast.databinding.ActivityChatBinding;
import com.squareup.picasso.Picasso;
import com.visionstech.whatsappview.Chat;
import com.visionstech.whatsappview.ChatNode;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends BaseActivity implements Chat.ChatHandler {

    @Inject
    Chat chat;
    Order mOrder;

    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityChatBinding binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mOrder = getIntent().getParcelableExtra(OBJECT);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.getOnApiErrorMutableLiveData().observe(this, this::onApiError);
        viewModel.getOnLoadingMutableLiveData().observe(this, this::onLoading);
        viewModel.getChatMessageAddedMutableLiveData().observe(this, this::onChatMessageAddedResponse);
        viewModel.getChatMessageChangedMutableLiveData().observe(this, this::onChatMessageChangedResponse);
        viewModel.getSendFileResponseMutableLiveData().observe(this, this::onSendFileResponse);

        chat.onCreate(this, binding.layoutMain, Integer.parseInt(preferencesManager.getUserId()));
        chat.setChatHandler(this);

        if (preferencesManager.getUserTypeId().equals("2")) {
            if (mOrder.getDriver() != null)
                Picasso.get().load(mOrder.getDriver().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(binding.civUserImage);
            binding.tvUserName.setText(mOrder.getDriver().getName());
            chat.setChatImages(mOrder.getUser().getImage(), mOrder.getDriver().getImage());
        } else {
            Picasso.get().load(mOrder.getUser().getImage()).placeholder(R.drawable.logo_holder).error(R.drawable.logo_holder).into(binding.civUserImage);
            binding.tvUserName.setText(mOrder.getUser().getName());
            binding.llDeliveryContainer.setVisibility(View.VISIBLE);
            chat.setChatImages(mOrder.getDriver().getImage(), mOrder.getUser().getImage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.startChatListening(String.valueOf(mOrder.getId()));
    }

    @Override
    public void onStop() {
        super.onStop();
        chat.onStop();
        viewModel.removeChatListener();
    }

    private void onChatMessageAddedResponse(ChatNode chatNode) {
        chat.onChatMessageAddedResponse(chatNode);

    }

    private void onChatMessageChangedResponse(ChatNode chatNode) {
        chat.onChatMessageChangedResponse(chatNode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chat.onActivityResult(requestCode, resultCode, data);
    }

    private void onSendFileResponse(BaseResponse baseResponse) {
        chat.onSendFileResponse();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return chat.onKeyDown(keyCode, event, super.onKeyDown(keyCode, event));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.removeChatListener();
        chat.onDestroy();
    }

    @Override
    public void sendMessageNotification(String message) {
        viewModel.requestMessageNotification(MessageNotificationRequest.builder()
                .order_id(String.valueOf(mOrder.getId()))
                .message(message)
                .build());
    }

    @Override
    public void sendVoiceMessage(String filePath, String replyKey) {
        viewModel.requestSendChatFile(ChatFileRequest.builder()
                .chat_id(String.valueOf(mOrder.getId()))
                .file_type("4")
                .file(StaticMethods.getFileRequestBody(new File(filePath), "file"))
                .reply_key(replyKey)
                .build());
    }

    @Override
    public void sendImageMessage(String filePath, String replyKey) {
        viewModel.requestSendChatFile(ChatFileRequest.builder()
                .chat_id(String.valueOf(mOrder.getId()))
                .file_type("2")
                .file(StaticMethods.getImageRequestBody(filePath, "file"))
                .reply_key(replyKey)
                .build());
    }

    @Override
    public void sendTextMessage(ChatNode chatNode) {
        viewModel.sendMessage(String.valueOf(mOrder.getId()), chatNode);

    }

    @Override
    public void seeMessage(ChatNode chatNode) {
        viewModel.seeMessage(String.valueOf(mOrder.getId()), chatNode);

    }

    @Override
    public void deleteMessage(ChatNode chatNode) {
        viewModel.deleteMessage(String.valueOf(mOrder.getId()), chatNode);

    }

    @Override
    public ActivityResultLauncher<Intent> registerForActivityResult() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> chat.onRequestAudioActivityResult(result));
    }
}