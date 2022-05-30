package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelReason implements Parcelable {
    private int id;
    private String name;


    protected CancelReason(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<CancelReason> CREATOR = new Creator<CancelReason>() {
        @Override
        public CancelReason createFromParcel(Parcel in) {
            return new CancelReason(in);
        }

        @Override
        public CancelReason[] newArray(int size) {
            return new CancelReason[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
