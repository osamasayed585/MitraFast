package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FavoritePlace implements Parcelable {

    private int id;
    private String user_id;
    private String name;
    private String lat;
    private String lng;
    private String address;


    protected FavoritePlace(Parcel in) {
        id = in.readInt();
        user_id = in.readString();
        name = in.readString();
        lat = in.readString();
        lng = in.readString();
        address = in.readString();
    }

    public static final Creator<FavoritePlace> CREATOR = new Creator<FavoritePlace>() {
        @Override
        public FavoritePlace createFromParcel(Parcel in) {
            return new FavoritePlace(in);
        }

        @Override
        public FavoritePlace[] newArray(int size) {
            return new FavoritePlace[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(user_id);
        dest.writeString(name);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(address);
    }
}
