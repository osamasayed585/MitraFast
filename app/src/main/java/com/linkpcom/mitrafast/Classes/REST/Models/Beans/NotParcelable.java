package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotParcelable implements Parcelable {

    List<Notification> notificationList;

    protected NotParcelable(Parcel in) {
        notificationList = in.createTypedArrayList(Notification.CREATOR);
    }

    public NotParcelable(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public static final Creator<NotParcelable> CREATOR = new Creator<NotParcelable>() {
        @Override
        public NotParcelable createFromParcel(Parcel in) {
            return new NotParcelable(in);
        }

        @Override
        public NotParcelable[] newArray(int size) {
            return new NotParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(notificationList);
    }
}
