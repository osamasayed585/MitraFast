package com.linkpcom.mitrafast.Classes.REST.Models.Beans;


import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplyImages implements Parcelable {
    private int id;
    private String image;


    protected ReplyImages(Parcel in) {
        id = in.readInt();
        image = in.readString();
    }

    public static final Creator<ReplyImages> CREATOR = new Creator<ReplyImages>() {
        @Override
        public ReplyImages createFromParcel(Parcel in) {
            return new ReplyImages(in);
        }

        @Override
        public ReplyImages[] newArray(int size) {
            return new ReplyImages[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
    }
}