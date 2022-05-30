package com.visionstech.whatsappview;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.visionstech.whatsappview.databinding.ChatViewBinding;

import java.util.List;

import timber.log.Timber;

public class Chat implements ChatAdapter.ChatAdapterOnEventHandler {
    private static final String TAG = "Chat";
    private static final int IMAGE_MESSAGE = 1;
    private static final int VOICE_MESSAGE = 2;
    ChatAdapter chatAdapter;
    WhatsAppView whatsAppView;
    VoiceRecorder voiceRecorder;
    RecyclerView chat;
    List<Image> sendImages;
    RemoveMessageDialog removeMessageDialog;
    int messageType;
    ChatViewBinding binding;
    Activity mActivity;
    int userId;
    private String emailPattern = Patterns.EMAIL_ADDRESS.pattern();
    private String phonePattern = Patterns.PHONE.pattern();
    private ChatHandler mChatHandler;
    private ChatNode selectedChatMessage;
    private ActivityResultLauncher<Intent> mRequestAudioResultLauncher;

    public void onCreate(Activity activity, ViewGroup view, int userId) {
        binding = ChatViewBinding.inflate(LayoutInflater.from(activity));

        if (view == null) {
            Log.e(TAG, "initView ViewGroup can't be NULL");
            return;
        }

        view.removeAllViews();
        view.addView(binding.getRoot());


        mActivity = activity;
        this.userId = userId;

        chatAdapter = new ChatAdapter(userId);
        whatsAppView = new WhatsAppView();
        voiceRecorder = new VoiceRecorder(activity);
        removeMessageDialog = new RemoveMessageDialog(activity);

        setupRemoveMessageDialog();
        whatsAppViewSetup();

        chat.setAdapter(chatAdapter);

        chatAdapter.setEventHandler(this);


        binding.backToNormal.setOnClickListener(this::onBackToNormalClick);
        binding.deleteMessage.setOnClickListener(this::onDeleteMessageClick);
        binding.reply.setOnClickListener(this::onReplyClick);
        binding.copy.setOnClickListener(this::onCopyClick);


    }


    public void onStop() {
        voiceRecorder.onStop();
        chatAdapter.clear();
    }


    private void onBackToNormalClick(View view) {
        if (chatAdapter.isOptionsReveled()) {
            resetMessagesStatus();
            selectedChatMessage = null;
        }
    }

    private void onReplyClick(View view) {
        if (chatAdapter.isOptionsReveled()) {
            resetMessagesStatus();
        }
        whatsAppView.showReplayContainer(selectedChatMessage.getMessage(), selectedChatMessage.isTextMessage(), selectedChatMessage.isImageMessage(), selectedChatMessage.isVoiceMessage());
    }

    private void onCopyClick(View view) {
        ClipboardManager cm = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(selectedChatMessage.getMessage());
        Toast.makeText(mActivity, mActivity.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();

        if (chatAdapter.isOptionsReveled()) {
            resetMessagesStatus();
            selectedChatMessage = null;
        }


    }

    private void setupRemoveMessageDialog() {
        removeMessageDialog.setNegativeListener(view -> {
            removeMessageDialog.getDialog().dismiss();
            selectedChatMessage = null;
        });
        removeMessageDialog.setRemoveMessageDialogHandler(() -> {
            if (mChatHandler != null)
                mChatHandler.deleteMessage(selectedChatMessage);
            selectedChatMessage = null;
        });

    }

    private void whatsAppViewSetup() {
        whatsAppView.initView(binding.layoutMain);
        chat = whatsAppView.getChatRecyclerView();


        whatsAppView.setMessageSwipeController(new MessageSwipeController(mActivity, position -> {
            selectedChatMessage = chatAdapter.getMData().get(position);
            whatsAppView.showReplayContainer(selectedChatMessage.getMessage(), selectedChatMessage.isTextMessage(), selectedChatMessage.isImageMessage(), selectedChatMessage.isVoiceMessage());
        }));

        whatsAppView.setRecordingListener(new WhatsAppView.RecordingListener() {
            @Override
            public void onRecordingStarted() {
                voiceRecorder.onRecord(true);
                Timber.d("Shady onRecordingStarted: ");
            }

            @Override
            public void onRecordingLocked() {
                Timber.d("Shady onRecordingLocked: ");
            }

            @Override
            public void onRecordingCompleted(int recordTime) {
                voiceRecorder.onRecord(false);

                if (chatAdapter.isOptionsReveled()) {
                    resetMessagesStatus();
                    selectedChatMessage = null;
                }
                if (recordTime >= 2) {
                    sendVoiceMessage(voiceRecorder.getFilePath());
                    sendNotification(mActivity.getString(R.string.i_sent_a_voice));

                    Timber.d("Shady onRecordingCompleted: ");
                }
            }

            @Override
            public void onRecordingCanceled() {
                voiceRecorder.onRecord(false);
                Timber.d("Shady onRecordingCanceled: ");

            }
        });

        whatsAppView.setReplayListener(new WhatsAppView.ReplayListener() {
            @Override
            public void onReplyCanceled() {
                selectedChatMessage = null;
            }
        });

        whatsAppView.setAttachmentOptions(AttachmentOption.getDefaultList(), new AttachmentOptionsListener() {
            @Override
            public void onClick(AttachmentOption attachmentOption) {
                switch (attachmentOption.getId()) {

                    case AttachmentOption.DOCUMENT_ID: //Ids for default Attachment Options
                        Timber.d("Document Clicked");
                        break;
                    case AttachmentOption.CAMERA_ID:
                        requestImage();
                        break;
                    case AttachmentOption.GALLERY_ID:
                        requestImage();
                        break;
                    case AttachmentOption.AUDIO_ID:
                        Timber.d("Audio Clicked");
                        requestAudio();
                        break;
                }
            }
        });
        whatsAppView.getSendView().setOnClickListener(this::onSendClick);
        whatsAppView.getCameraView().setOnClickListener(view -> requestImage());
    }


    private void onSendClick(View view) {
        if (!validation())
            return;

        if (chatAdapter.isOptionsReveled()) {
            resetMessagesStatus();
            selectedChatMessage = null;
        }

        String message = whatsAppView.getMessageView().getText().toString();
        sendTextMessage(message);
        sendNotification(message);

        //clear the EditText
        whatsAppView.getMessageView().setText("");

    }

    private boolean validation() {


        return
                isNotEmail(whatsAppView.getMessageView()) &&
                        isNotPhoneNumber(whatsAppView.getMessageView());


    }

    private boolean isNotEmail(TextView view) {
        String value = view.getText().toString().trim();
        if (value.matches(emailPattern)) {
            Toast.makeText(mActivity, mActivity.getString(R.string.you_cant_type_email_in_chat), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private boolean isNotPhoneNumber(TextView view) {
        String value = view.getText().toString().trim();
        if (value.matches(phonePattern)) {
            Toast.makeText(mActivity, mActivity.getString(R.string.you_cant_type_mobile_number_in_chat), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    public void onChatMessageAddedResponse(ChatNode chatNode) {
        chatAdapter.addMessage(chatNode);
        chat.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    public void onChatMessageChangedResponse(ChatNode chatNode) {
        chatAdapter.changeMessage(chatNode);
    }

    @Override
    public void onChatMessageSeen(ChatNode chatNode) {
        if (mChatHandler != null)
            mChatHandler.seeMessage(chatNode);
    }

    @Override
    public void onMessageLongClick(ChatNode chatNode, boolean isMyMessage) {
        selectedChatMessage = chatNode;
        binding.chatActionBar.setVisibility(View.VISIBLE);
        binding.deleteMessage.setVisibility(isMyMessage ? View.VISIBLE : View.GONE);
    }

    @Override
    public void scrollToPosition(int index) {
        chat.smoothScrollToPosition(index);

    }


    private void onDeleteMessageClick(View view) {
        if (chatAdapter.isOptionsReveled()) {
            resetMessagesStatus();
        }
        removeMessageDialog.getDialog().show();
    }


    private void requestImage() {
        ImagePicker.create(mActivity)
                .returnMode(ReturnMode.CAMERA_ONLY)
                .folderMode(true)
                .multi()
                .limit(10)
                .enableLog(true)
                .start();

    }

    private void requestAudio() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        if (intent.resolveActivity(mActivity.getPackageManager()) != null)
            mRequestAudioResultLauncher.launch(intent);


    }

    public void onRequestAudioActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri audioUri = result.getData().getData();
            sendVoiceMessage(RealPathUtil.getRealPath(mActivity, audioUri));
        }
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            if (chatAdapter.isOptionsReveled()) {
                resetMessagesStatus();
                selectedChatMessage = null;
            }

            sendImages = ImagePicker.getImages(data);
            if (sendImages.size() > 1) {
                sendNotification(mActivity.getString(R.string.i_sent_images));
            } else {
                sendNotification(mActivity.getString(R.string.i_sent_an_image));
            }
            sendFirstImageThenDeleteIt();

        }

    }


    public void onSendFileResponse() {
        clearReply();
        if (messageType == IMAGE_MESSAGE) {
            if (!sendImages.isEmpty()) {
                sendFirstImageThenDeleteIt();
            }
        }
    }

    private void sendFirstImageThenDeleteIt() {
        sendImageMessage(sendImages.get(0));
        sendImages.remove(0);
    }

    private void sendTextMessage(String message) {

        //create the new message object for inserting
        ChatNode newMessage = new ChatNode();
        newMessage.setMessage(message);
        newMessage.setCreatedAt(System.currentTimeMillis());
        newMessage.setSendBy(userId);
        newMessage.setType("1");
        newMessage.setStatus(2);
        if (selectedChatMessage != null)
            newMessage.setReplyKey(String.valueOf(selectedChatMessage.getCreatedAt()));
        else
            newMessage.setReplyKey(" ");

        if (mChatHandler != null)
            mChatHandler.sendTextMessage(newMessage);

        clearReply();
    }

    private void clearReply() {
        selectedChatMessage = null;
        whatsAppView.deleteReply();
    }

    private void sendImageMessage(Image image) {
        messageType = IMAGE_MESSAGE;
        if (mChatHandler != null)
            mChatHandler.sendImageMessage(
                    image.getPath(),
                    selectedChatMessage != null ? String.valueOf(selectedChatMessage.getCreatedAt()) : null);


    }

    private void sendVoiceMessage(String filePath) {
        messageType = VOICE_MESSAGE;
        Timber.d("Shady sendVoiceMessage: %s", filePath);
        if (mChatHandler != null)
            mChatHandler.sendVoiceMessage(
                    filePath,
                    selectedChatMessage != null ? String.valueOf(selectedChatMessage.getCreatedAt()) : null);

    }

    private void sendNotification(String message) {
        if (mChatHandler != null)
            mChatHandler.sendMessageNotification(message);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event, boolean superReturn) {
        if (chatAdapter.isOptionsReveled()) {
            resetMessagesStatus();
            selectedChatMessage = null;
            return true;

        } else {
            return superReturn;
        }

    }

    private void resetMessagesStatus() {
        chatAdapter.dismissOptions();
        binding.chatActionBar.setVisibility(View.GONE);
    }

    public void onDestroy() {
        chatAdapter.clearExoPlayers();
    }

    public void setChatImages(String userImage, String otherUserImage) {
        chatAdapter.setUserImage(userImage);
        chatAdapter.setOtherUserImage(otherUserImage);
    }

    public void setChatHandler(ChatHandler chatHandler) {
        mChatHandler = chatHandler;

        mRequestAudioResultLauncher = mChatHandler.registerForActivityResult();
    }

    public interface ChatHandler {
        void sendMessageNotification(String message);

        void sendVoiceMessage(String filePath, String replyKey);

        void sendImageMessage(String filePath, String replyKey);

        void sendTextMessage(ChatNode chatNode);

        void seeMessage(ChatNode chatNode);

        void deleteMessage(ChatNode chatNode);

        ActivityResultLauncher<Intent> registerForActivityResult();


    }


}