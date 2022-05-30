package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class Address implements Parcelable {


    private String name;
    private String address;
    private String lat;
    private String lng;
    private String subLocality;
    private String street_number;
    private String street;
    private String optionalData;


    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    protected Address(Parcel in) {
        name = in.readString();
        address = in.readString();
        lat = in.readString();
        lng = in.readString();
        subLocality = in.readString();
        street_number = in.readString();
        street = in.readString();
        optionalData = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeString(subLocality);
        parcel.writeString(street_number);
        parcel.writeString(street);
        parcel.writeString(optionalData);
    }
}