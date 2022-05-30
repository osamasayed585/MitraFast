package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentCard implements Parcelable {
    private int id;
    private String credit_card_name;
    private String credit_card_x_number;
    private String credit_card_month;
    private String credit_card_year;

    protected PaymentCard(Parcel in) {
        id = in.readInt();
        credit_card_name = in.readString();
        credit_card_x_number = in.readString();
        credit_card_month = in.readString();
        credit_card_year = in.readString();
    }

    public static final Creator<PaymentCard> CREATOR = new Creator<PaymentCard>() {
        @Override
        public PaymentCard createFromParcel(Parcel in) {
            return new PaymentCard(in);
        }

        @Override
        public PaymentCard[] newArray(int size) {
            return new PaymentCard[size];
        }
    };

    public String getExpiry_date() {
        return String.format("%s/%s", credit_card_month, credit_card_year);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(credit_card_name);
        parcel.writeString(credit_card_x_number);
        parcel.writeString(credit_card_month);
        parcel.writeString(credit_card_year);
    }
}