package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Status implements Parcelable {

    private int id;
    private String name;
    private String message;


    protected Status(Parcel in) {
        id = in.readInt();
        name = in.readString();
        message = in.readString();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
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
        dest.writeString(message);
    }
}