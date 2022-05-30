package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Shop implements Parcelable {
    private int id;
    private String image;
    private String mobile;
    private String name;
    private String lat;
    private String lng;
    private String rate;
    private String work_start_at;
    private String end_start_at;
    private boolean is_working;
    private boolean is_favourite;
    private String delivering_time_from;
    private String delivering_time_to;
    private int is_order_followed;
    private String twitter;
    private String instgram;
    private String snapchat;
    private String logo;
    private String min_order_value;
    private String max_order_value;
    private String delivery_cost;
    private String currency;
    private String distance_text;
    private String distance_value;
    private String duration_text;
    private String duration_value;
    private String shop_address;
    private float delivery_cost_with_tax;


    protected Shop(Parcel in) {
        id = in.readInt();
        image = in.readString();
        mobile = in.readString();
        name = in.readString();
        lat = in.readString();
        lng = in.readString();
        rate = in.readString();
        work_start_at = in.readString();
        end_start_at = in.readString();
        is_working = in.readByte() != 0;
        is_favourite = in.readByte() != 0;
        delivering_time_from = in.readString();
        delivering_time_to = in.readString();
        is_order_followed = in.readInt();
        twitter = in.readString();
        instgram = in.readString();
        snapchat = in.readString();
        logo = in.readString();
        min_order_value = in.readString();
        max_order_value = in.readString();
        delivery_cost = in.readString();
        currency = in.readString();
        distance_text = in.readString();
        distance_value = in.readString();
        duration_text = in.readString();
        duration_value = in.readString();
        shop_address = in.readString();
        delivery_cost_with_tax = in.readFloat();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public String getDelivery_cost() {
        try {

            return String.format(Locale.US, "%.2f", Double.parseDouble(delivery_cost));

        } catch (Exception e) {
            return "0";

        }
    }


    public String getImage() {
        return image.trim().equals("") ? null : image;
    }

    public String getLogo() {
        return logo == null || logo.trim().equals("") ? null : logo;
    }

    public boolean isTraceable() {
        return is_order_followed == 1;
    }

    public String getTime() {
        return String.format("%s - %s", delivering_time_from, delivering_time_to);
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(image);
        dest.writeString(mobile);
        dest.writeString(name);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(rate);
        dest.writeString(work_start_at);
        dest.writeString(end_start_at);
        dest.writeByte((byte) (is_working ? 1 : 0));
        dest.writeByte((byte) (is_favourite ? 1 : 0));
        dest.writeString(delivering_time_from);
        dest.writeString(delivering_time_to);
        dest.writeInt(is_order_followed);
        dest.writeString(twitter);
        dest.writeString(instgram);
        dest.writeString(snapchat);
        dest.writeString(logo);
        dest.writeString(min_order_value);
        dest.writeString(max_order_value);
        dest.writeString(delivery_cost);
        dest.writeString(currency);
        dest.writeString(distance_text);
        dest.writeString(distance_value);
        dest.writeString(duration_text);
        dest.writeString(duration_value);
        dest.writeString(shop_address);
        dest.writeFloat(delivery_cost_with_tax);
    }
}