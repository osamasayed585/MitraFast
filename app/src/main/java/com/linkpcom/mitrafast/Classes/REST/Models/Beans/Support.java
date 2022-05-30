package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Support implements Parcelable {
    private int id;
    private String name;
    private String image;


    protected Support(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<Support> CREATOR = new Creator<Support>() {
        @Override
        public Support createFromParcel(Parcel in) {
            return new Support(in);
        }

        @Override
        public Support[] newArray(int size) {
            return new Support[size];
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
        dest.writeString(image);
    }
}