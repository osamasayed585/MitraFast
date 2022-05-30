package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User implements Parcelable {
    private int id;
    private String user_type_id;
    private String name;
    private String birthday_date;
    private String image;
    private String country_id;
    private String mobile;
    private boolean mobile_verified;
    private String email;
    private boolean email_verified;
    private String fire_base_token;
    private boolean userVerify;
    private boolean need_reset_password;
    private String identity_name;
    private String membership_number;
    private String created_at;
    private String updated_at;
    private int is_profile_approved;
    private String api_token;
    private int is_deliver;
    private int is_order_followed;
    private int app_percentage;
    private String payment_type;
    private int company_id;
    private String identity_image;
    private int status;
    private int recieve_order_status;
    private int is_moyaser_active;
    private String shop_rate;
    private int map_distance;

    protected User(Parcel in) {
        id = in.readInt();
        user_type_id = in.readString();
        name = in.readString();
        birthday_date = in.readString();
        image = in.readString();
        country_id = in.readString();
        mobile = in.readString();
        mobile_verified = in.readByte() != 0;
        email = in.readString();
        email_verified = in.readByte() != 0;
        fire_base_token = in.readString();
        userVerify = in.readByte() != 0;
        need_reset_password = in.readByte() != 0;
        identity_name = in.readString();
        membership_number = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        is_profile_approved = in.readInt();
        api_token = in.readString();
        is_deliver = in.readInt();
        is_order_followed = in.readInt();
        app_percentage = in.readInt();
        payment_type = in.readString();
        company_id = in.readInt();
        identity_image = in.readString();
        status = in.readInt();
        recieve_order_status = in.readInt();
        is_moyaser_active = in.readInt();
        shop_rate = in.readString();
        map_distance = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(user_type_id);
        parcel.writeString(name);
        parcel.writeString(birthday_date);
        parcel.writeString(image);
        parcel.writeString(country_id);
        parcel.writeString(mobile);
        parcel.writeByte((byte) (mobile_verified ? 1 : 0));
        parcel.writeString(email);
        parcel.writeByte((byte) (email_verified ? 1 : 0));
        parcel.writeString(fire_base_token);
        parcel.writeByte((byte) (userVerify ? 1 : 0));
        parcel.writeByte((byte) (need_reset_password ? 1 : 0));
        parcel.writeString(identity_name);
        parcel.writeString(membership_number);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeInt(is_profile_approved);
        parcel.writeString(api_token);
        parcel.writeInt(is_deliver);
        parcel.writeInt(is_order_followed);
        parcel.writeInt(app_percentage);
        parcel.writeString(payment_type);
        parcel.writeInt(company_id);
        parcel.writeString(identity_image);
        parcel.writeInt(status);
        parcel.writeInt(recieve_order_status);
        parcel.writeInt(is_moyaser_active);
        parcel.writeString(shop_rate);
        parcel.writeInt(map_distance);
    }
}
