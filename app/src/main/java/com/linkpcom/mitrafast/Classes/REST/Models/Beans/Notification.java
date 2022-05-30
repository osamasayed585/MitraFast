package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Notification implements Parcelable {
    private int id;
    private int order_id;
    private String title;
    private String body;
    private boolean viewed;
    private User user;
    private String time;


    protected Notification(Parcel in) {
        id = in.readInt();
        order_id = in.readInt();
        title = in.readString();
        body = in.readString();
        viewed = in.readByte() != 0;
        time = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(order_id);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeByte((byte) (viewed ? 1 : 0));
        parcel.writeString(time);
    }
}