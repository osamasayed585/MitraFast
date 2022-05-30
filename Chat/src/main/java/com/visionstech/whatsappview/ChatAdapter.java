package com.visionstech.whatsappview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.visionstech.whatsappview.databinding.ListItemOtherUserMessageBinding;
import com.visionstech.whatsappview.databinding.ListItemUserMessageBinding;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


@Getter
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int USER_MESSAGE = 1;
    private static final int OTHER_USER_MESSAGE = 2;
    ChatImagesDialog chatImagesDialog;
    Context context;
    int userId;

    String userImage;
    String otherUserImage;

    private List<ChatNode> mData;
    private ChatAdapterOnEventHandler mClickHandler;
    private boolean isOptionsReveled;


    public ChatAdapter(int userId) {
        this.mData = new ArrayList<>();
        this.userId = userId;
    }
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void setOtherUserImage(String otherUserImage) {
        this.otherUserImage = otherUserImage;
    }


    public void setEventHandler(ChatAdapterOnEventHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    @Override
    public int getItemViewType(int position) {
        ChatNode chatNode = mData.get(position);
        if (chatNode.getSendBy() == userId || chatNode.getSendBy() == 0)
            return USER_MESSAGE;
        else
            return OTHER_USER_MESSAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        chatImagesDialog = new ChatImagesDialog(context);
        View view;
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case USER_MESSAGE: {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_user_message, parent, false);
                viewHolder = new UserMessageViewHolder(view);
                break;
            }
            case OTHER_USER_MESSAGE: {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_other_user_message, parent, false);
                viewHolder = new OtherUserMessageViewHolder(view);
                break;
            }

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatNode chatNode = mData.get(position);

        int ViewType = getItemViewType(position);
        switch (ViewType) {
            case USER_MESSAGE: {
                UserMessageViewHolder userMessageViewHolder = (UserMessageViewHolder) holder;
                userMessageViewHolder.binding.status.setVisibility(VISIBLE);
                if (chatNode.getSendBy() == 0)
                    userMessageViewHolder.binding.civUserImage.setImageResource(R.drawable.main_logo);
                else
                    Glide.with(context).load(userImage).placeholder(R.drawable.main_logo).error(R.drawable.main_logo).into(userMessageViewHolder.binding.civUserImage);

                handelMessage(chatNode, userMessageViewHolder.binding.container, userMessageViewHolder.binding.deletedMessage, userMessageViewHolder.binding.replyContainer, userMessageViewHolder.binding.textMessage, userMessageViewHolder.binding.imageMessage, userMessageViewHolder.binding.voiceMessage, userMessageViewHolder.binding.replyImage, userMessageViewHolder.binding.replyMessage, userMessageViewHolder.binding.messageCreatedTime);
                switch (chatNode.getStatus()) {
                    case 0:
                        userMessageViewHolder.binding.status.setVisibility(GONE);
                        break;
                    case 1:
                        userMessageViewHolder.binding.status.setImageResource(R.drawable.ic_sending);
                        break;
                    case 2:
                        userMessageViewHolder.binding.status.setImageResource(R.drawable.ic_sent);
                        break;
                    case 3:
                        userMessageViewHolder.binding.status.setImageResource(R.drawable.ic_reached);
                        break;
                    case 4:
                        userMessageViewHolder.binding.status.setImageResource(R.drawable.ic_seen);
                        break;
                }
                break;
            }
            case OTHER_USER_MESSAGE: {
                OtherUserMessageViewHolder otherUserMessageViewHolder = (OtherUserMessageViewHolder) holder;
                Glide.with(context).load(otherUserImage).placeholder(R.drawable.main_logo).error(R.drawable.main_logo).into(otherUserMessageViewHolder.binding.civUserImage);
                handelMessage(chatNode, otherUserMessageViewHolder.binding.container, otherUserMessageViewHolder.binding.deletedMessage, otherUserMessageViewHolder.binding.replyContainer, otherUserMessageViewHolder.binding.textMessage, otherUserMessageViewHolder.binding.imageMessage, otherUserMessageViewHolder.binding.voiceMessage, otherUserMessageViewHolder.binding.replyImage, otherUserMessageViewHolder.binding.replyMessage, otherUserMessageViewHolder.binding.messageCreatedTime);
                switch (chatNode.getStatus()) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if (mClickHandler != null)
                            mClickHandler.onChatMessageSeen(chatNode);
                        break;
                }

                break;
            }
        }


    }

    private void handelMessage(ChatNode chatNode, LinearLayout container, TextView deletedMessage, LinearLayout replyContainer, TextView textMessage, ImageView imageMessage, PlayerView voiceMessage, ImageView replyImage, TextView replyMessage, RelativeTimeTextView messageCreatedTime) {
        container.setBackgroundResource(android.R.color.transparent);

        deletedMessage.setVisibility(GONE);
        replyContainer.setVisibility(chatNode.isReply() ? VISIBLE : GONE);
        textMessage.setVisibility(chatNode.isTextMessage() ? VISIBLE : GONE);
        imageMessage.setVisibility(chatNode.isImageMessage() ? VISIBLE : GONE);
        voiceMessage.setVisibility(chatNode.isVoiceMessage() ? VISIBLE : GONE);

        if (chatNode.isTextMessage()) {
            textMessage.setText(chatNode.getMessage());
        } else if (chatNode.isImageMessage()) {
            Glide.with(context).load(chatNode.getMessage()).placeholder(R.drawable.main_logo).error(R.drawable.main_logo).into(imageMessage);
        } else if (chatNode.isVoiceMessage()) {
            voiceMessage.setPlayer(chatNode.getPlayer(context));
        }


        if (chatNode.isReply()) {
            replyImage.setVisibility(View.GONE);
            ChatNode replyChatNode = getReplyMessage(chatNode);
            if (replyChatNode.isTextMessage()) {
                replyMessage.setText(replyChatNode.getMessage());
            } else if (replyChatNode.isImageMessage()) {
                replyImage.setVisibility(View.VISIBLE);
                replyMessage.setText(context.getString(R.string.photo));
                Glide.with(context).load(replyChatNode.getMessage()).placeholder(R.drawable.main_logo).error(R.drawable.main_logo).into(replyImage);
            } else if (replyChatNode.isVoiceMessage()) {
                replyMessage.setText(context.getString(R.string.voice_message));
            }
        }

        messageCreatedTime.setReferenceTime(chatNode.getCreatedAt());


        if (chatNode.getStatus() == 0) {
            deletedMessage.setVisibility(VISIBLE);
            replyContainer.setVisibility(GONE);
            textMessage.setVisibility(GONE);
            imageMessage.setVisibility(GONE);
            voiceMessage.setVisibility(GONE);
        }
    }

    private ChatNode getReplyMessage(ChatNode chatNode) {
        if (chatNode.getReplyIndex() == null) {
            for (int i = 0; i < mData.size(); i++) {
                if (String.valueOf(mData.get(i).getCreatedAt()).equals(chatNode.getReplyKey())) {
                    chatNode.setReplyIndex(i);
                    return mData.get(i);
                }
            }
        } else {
            return mData.get(chatNode.getReplyIndex());

        }

        return new ChatNode();
    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.size();
    }


    public void addMessage(ChatNode data) {
        mData.add(data);
        notifyItemInserted(mData.size() - 1);
    }

    public void changeMessage(ChatNode data) {
        int index = getIndex(data);
        mData.set(index, data);
        notifyItemChanged(index);
    }

    private int getIndex(ChatNode data) {
        for (int i = 0; i < mData.size(); i++) {
            if (data.getCreatedAt().equals(mData.get(i).getCreatedAt())) {
                return i;
            }
        }
        return 0;
    }

    private void showImageDialog(String image) {
        chatImagesDialog.setImage(image);
        chatImagesDialog.getDialog().show();
    }

    public void dismissOptions() {
        isOptionsReveled = false;
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        clearExoPlayers();
        notifyDataSetChanged();
    }

    public void clearExoPlayers() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).getPlayer().release();
        }
    }

    private void onReplyClick(int position) {
        ChatNode chatNode = mData.get(position);
        if (mClickHandler != null)
            mClickHandler.scrollToPosition(chatNode.getReplyIndex());
    }

    private void onImageClick(int position) {
        showImageDialog(mData.get(position).getMessage());
    }

    private boolean onMessageLongClick(int position, LinearLayout container) {
        if (!isOptionsReveled) {
            if (mData.get(position).getStatus() != 0) {
                container.setBackgroundResource(R.color.colorPrimary);
                ChatNode chatNode = mData.get(position);
                if (mClickHandler != null)
                    mClickHandler.onMessageLongClick(chatNode, mData.get(position).getSendBy() == userId);
                isOptionsReveled = true;
            }
        }
        return false;

    }


    public interface ChatAdapterOnEventHandler {
        void onChatMessageSeen(ChatNode chatNode);

        void onMessageLongClick(ChatNode chatNode, boolean isMyMessage);

        void scrollToPosition(int index);

    }

    public class UserMessageViewHolder extends RecyclerView.ViewHolder {


        ListItemUserMessageBinding binding;


        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListItemUserMessageBinding.bind(itemView);
            binding.replyMessage.setOnClickListener(view -> onReplyClick(getBindingAdapterPosition()));
            binding.imageMessage.setOnClickListener(view -> onImageClick(getBindingAdapterPosition()));
            binding.container.setOnLongClickListener(view -> onMessageLongClick(getBindingAdapterPosition(), binding.container));

        }

    }

    public class OtherUserMessageViewHolder extends RecyclerView.ViewHolder {


        ListItemOtherUserMessageBinding binding;

        public OtherUserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ListItemOtherUserMessageBinding.bind(itemView);
            binding.replyMessage.setOnClickListener(view -> onReplyClick(getBindingAdapterPosition()));
            binding.imageMessage.setOnClickListener(view -> onImageClick(getBindingAdapterPosition()));
            binding.container.setOnLongClickListener(view -> onMessageLongClick(getBindingAdapterPosition(), binding.container));
        }


    }

}
