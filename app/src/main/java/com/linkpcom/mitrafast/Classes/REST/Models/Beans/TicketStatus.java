package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketStatus implements Parcelable {

    private String updated_at;
    private String created_at;
    private String status_color;
    private String name_en;
    private String name_ar;

    public String getStatus_color() {
        return "#"+status_color;
    }

    private int id;

    protected TicketStatus(Parcel in) {
        updated_at = in.readString();
        created_at = in.readString();
        status_color = in.readString();
        name_en = in.readString();
        name_ar = in.readString();
        id = in.readInt();
    }

    public static final Creator<TicketStatus> CREATOR = new Creator<TicketStatus>() {
        @Override
        public TicketStatus createFromParcel(Parcel in) {
            return new TicketStatus(in);
        }

        @Override
        public TicketStatus[] newArray(int size) {
            return new TicketStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updated_at);
        dest.writeString(created_at);
        dest.writeString(status_color);
        dest.writeString(name_en);
        dest.writeString(name_ar);
        dest.writeInt(id);
    }
}
