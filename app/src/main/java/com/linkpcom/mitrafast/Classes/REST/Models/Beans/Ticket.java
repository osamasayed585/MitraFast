package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ticket implements Parcelable {
    private String email;
    private Support support_section;
    private boolean is_visible;
    private List<Image> support_ticket_images;
    private int id;
    private String name;
    private String time;
    private String mobile;
    private String message;
    private TicketStatus support_ticket_statuses;
    private String ticket_status_name;
    private TicketType ticket_type;
    private User user;


    protected Ticket(Parcel in) {
        email = in.readString();
        support_section = in.readParcelable(Support.class.getClassLoader());
        is_visible = in.readByte() != 0;
        id = in.readInt();
        name = in.readString();
        time = in.readString();
        mobile = in.readString();
        message = in.readString();
        support_ticket_statuses = in.readParcelable(TicketStatus.class.getClassLoader());
        ticket_status_name = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    public void inverseVisibility() {
        is_visible = !is_visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeParcelable(support_section, flags);
        dest.writeByte((byte) (is_visible ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeString(mobile);
        dest.writeString(message);
        dest.writeParcelable(support_ticket_statuses, flags);
        dest.writeString(ticket_status_name);
        dest.writeParcelable(user, flags);
    }
}