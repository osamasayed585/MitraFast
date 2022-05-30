package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Agent implements Parcelable {

    private int id;
    private String image;
    private boolean userVerify;
    private String identity_name;
    private String name;
    private String membership_number;


    private String mobile;
    private String join_from;
    private String identity_id;
    private String email;

    private String finish_orders_count;
    private String cancel_orders_count;
    private String pending_orders_count;
    private String last_orders_status;
    private float rate;
    private Country country;

    private int is_profile_approved;

    protected Agent(Parcel in) {
        id = in.readInt();
        image = in.readString();
        userVerify = in.readByte() != 0;
        identity_name = in.readString();
        name = in.readString();
        membership_number = in.readString();
        mobile = in.readString();
        join_from = in.readString();
        identity_id = in.readString();
        email = in.readString();
        finish_orders_count = in.readString();
        cancel_orders_count = in.readString();
        pending_orders_count = in.readString();
        last_orders_status = in.readString();
        rate = in.readFloat();
        is_profile_approved = in.readInt();
    }

    public static final Creator<Agent> CREATOR = new Creator<Agent>() {
        @Override
        public Agent createFromParcel(Parcel in) {
            return new Agent(in);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };

    public boolean is_profile_approved() {
        return is_profile_approved==1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeByte((byte) (userVerify ? 1 : 0));
        dest.writeString(identity_name);
        dest.writeString(name);
        dest.writeString(membership_number);
        dest.writeString(mobile);
        dest.writeString(join_from);
        dest.writeString(identity_id);
        dest.writeString(email);
        dest.writeString(finish_orders_count);
        dest.writeString(cancel_orders_count);
        dest.writeString(pending_orders_count);
        dest.writeString(last_orders_status);
        dest.writeFloat(rate);
        dest.writeInt(is_profile_approved);
    }
}
