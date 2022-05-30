package com.visionstech.whatsappview;

import android.content.Context;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.database.Exclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatNode {
    private Long createdAt;
    private String message;
    private Integer sendBy;
    private String type;
    private String replyKey;
    /*
     * 0 For Deleted
     * 1 For Sending
     * 2 For Sent
     * 3 For Reached
     * 4 For Seen
     * */
    private Integer status;
    @Exclude
    private boolean isReply;
    @Exclude
    private Integer replyIndex;
    @Exclude
    private MediaItem mediaItem;
    @Exclude
    private SimpleExoPlayer player;

    public ChatNode() {

    }

    @Exclude
    public boolean isTextMessage() {
        return type.equals("1");
    }

    @Exclude
    public boolean isImageMessage() {
        return type.equals("2");
    }

    @Exclude
    public boolean isVoiceMessage() {
        return type.equals("4");
    }

    @Exclude
    public SimpleExoPlayer getPlayer(Context context) {
        if (mediaItem == null)
            mediaItem = MediaItem.fromUri(message);
        if (player == null) {
            player = new SimpleExoPlayer.Builder(context).build();
            player.setMediaItem(mediaItem);
            player.prepare();
        }

        return player;
    }

    @Exclude
    public boolean isReply() {
        return replyKey != null && !replyKey.trim().equals("");
    }
}
