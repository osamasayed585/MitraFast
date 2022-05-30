package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentResponse extends BaseResponse {
    private DataBean data;


    @Setter
    @Getter
    public class DataBean {
        private String url;
        private TransactionCharge transaction;
    }
    @Getter
    @Setter
    public class TransactionCharge implements Parcelable {
        private int id;
        private int transaction_id;
        private double price;
        private String payment_type;
        private int status;
        private String expired_at;
        private String redirect_url;

        protected TransactionCharge(Parcel in) {
            id = in.readInt();
            transaction_id = in.readInt();
            price = in.readDouble();
            payment_type = in.readString();
            status = in.readInt();
            expired_at = in.readString();
            redirect_url = in.readString();
        }

        public final Creator<TransactionCharge> CREATOR = new Creator<TransactionCharge>() {
            @Override
            public TransactionCharge createFromParcel(Parcel in) {
                return new TransactionCharge(in);
            }

            @Override
            public TransactionCharge[] newArray(int size) {
                return new TransactionCharge[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeInt(transaction_id);
            parcel.writeDouble(price);
            parcel.writeString(payment_type);
            parcel.writeInt(status);
            parcel.writeString(expired_at);
            parcel.writeString(redirect_url);
        }
    }

}