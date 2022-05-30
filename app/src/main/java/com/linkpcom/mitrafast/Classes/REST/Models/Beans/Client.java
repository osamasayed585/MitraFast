package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Client implements Parcelable {

    private int id;
    private String name;
    private String image;
    private String email;
    private boolean userVerify;
    private String mobile;
    private String join_from;
    private float rate;
    private String address ;
    private String user_type_id ;
    private boolean is_data_complete ;


    protected Client(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        userVerify = in.readByte() != 0;
        mobile = in.readString();
        join_from = in.readString();
        rate = in.readFloat();
        address = in.readString();
        user_type_id = in.readString();
        is_data_complete = in.readByte() != 0;
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
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
        dest.writeByte((byte) (userVerify ? 1 : 0));
        dest.writeString(mobile);
        dest.writeString(join_from);
        dest.writeFloat(rate);
        dest.writeString(address);
        dest.writeString(user_type_id);
        dest.writeByte((byte) (is_data_complete ? 1 : 0));
    }
}
