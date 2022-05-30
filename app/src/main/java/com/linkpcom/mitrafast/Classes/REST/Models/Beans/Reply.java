package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;



import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Reply implements Parcelable {
    private int id;
    private String reply;
    private String mobile;
    private Client user;
    private String created_at;
    private List<ReplyImages> Support_ticket_reply_image;

    protected Reply(Parcel in) {
        id = in.readInt();
        reply = in.readString();
        mobile = in.readString();
        user = in.readParcelable(Client.class.getClassLoader());
        created_at = in.readString();
        Support_ticket_reply_image = in.createTypedArrayList(ReplyImages.CREATOR);
    }

    public static final Creator<Reply> CREATOR = new Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel in) {
            return new Reply(in);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(reply);
        dest.writeString(mobile);
        dest.writeParcelable(user, flags);
        dest.writeString(created_at);
        dest.writeTypedList(Support_ticket_reply_image);
    }

}