package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Order implements Parcelable {
    private int id;
    private String cost;
    private String currency;
    private String date;
    private Shop shop;
    private FavoritePlace location;
    private Status order_status;
    private Client user;
    private List<OrderContent> products;
    private Agent driver;
    private String notes;
    private Rate rate;
    private String payment_id;
    private PaymentMethod payment_type;
    private String app_percentage;
    private String final_cost;
    private String provider_cost;
    private CancelReason cancellation_reason;
    private String value_added_tax_percentage;
    private String tax_cost;
    private String coupon_discount_percentage;
    private String coupon_discount_cost;
    private String products_cost;


    private String distance_text;
    private String distance_value;
    private String duration_text;
    private String duration_value;
    private String created_at;
    private String delivery_cost;
    private String delivery_cost_app_percentage;
    private String delivery_cost_tax_percentage;
    private String delivery_cost_tax_percentage_value;
    private String delivery_cost_with_app_percentage_value;
    private String delivery_cost_with_tax;
    private String shop_products_cost;
    private String shop_products_cost_app_percentage;
    private String shop_products_cost_app_percentage_value;
    private String shop_products_app_percentage_with_tax;
    private String total_cost;

    protected Order(Parcel in) {
        id = in.readInt();
        cost = in.readString();
        currency = in.readString();
        date = in.readString();
        shop = in.readParcelable(Shop.class.getClassLoader());
        location = in.readParcelable(FavoritePlace.class.getClassLoader());
        order_status = in.readParcelable(Status.class.getClassLoader());
        user = in.readParcelable(Client.class.getClassLoader());
        driver = in.readParcelable(Agent.class.getClassLoader());
        notes = in.readString();
        payment_id = in.readString();
        payment_type = in.readParcelable(PaymentMethod.class.getClassLoader());
        app_percentage = in.readString();
        final_cost = in.readString();
        provider_cost = in.readString();
        cancellation_reason = in.readParcelable(CancelReason.class.getClassLoader());
        value_added_tax_percentage = in.readString();
        tax_cost = in.readString();
        coupon_discount_percentage = in.readString();
        coupon_discount_cost = in.readString();
        products_cost = in.readString();
        distance_text = in.readString();
        distance_value = in.readString();
        duration_text = in.readString();
        duration_value = in.readString();
        created_at = in.readString();
        delivery_cost = in.readString();
        delivery_cost_app_percentage = in.readString();
        delivery_cost_tax_percentage = in.readString();
        delivery_cost_tax_percentage_value = in.readString();
        delivery_cost_with_app_percentage_value = in.readString();
        delivery_cost_with_tax = in.readString();
        shop_products_cost = in.readString();
        shop_products_cost_app_percentage = in.readString();
        shop_products_cost_app_percentage_value = in.readString();
        shop_products_app_percentage_with_tax = in.readString();
        total_cost = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cost);
        dest.writeString(currency);
        dest.writeString(date);
        dest.writeParcelable(shop, flags);
        dest.writeParcelable(location, flags);
        dest.writeParcelable(order_status, flags);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(driver, flags);
        dest.writeString(notes);
        dest.writeString(payment_id);
        dest.writeParcelable(payment_type, flags);
        dest.writeString(app_percentage);
        dest.writeString(final_cost);
        dest.writeString(provider_cost);
        dest.writeParcelable(cancellation_reason, flags);
        dest.writeString(value_added_tax_percentage);
        dest.writeString(tax_cost);
        dest.writeString(coupon_discount_percentage);
        dest.writeString(coupon_discount_cost);
        dest.writeString(products_cost);
        dest.writeString(distance_text);
        dest.writeString(distance_value);
        dest.writeString(duration_text);
        dest.writeString(duration_value);
        dest.writeString(created_at);
        dest.writeString(delivery_cost);
        dest.writeString(delivery_cost_app_percentage);
        dest.writeString(delivery_cost_tax_percentage);
        dest.writeString(delivery_cost_tax_percentage_value);
        dest.writeString(delivery_cost_with_app_percentage_value);
        dest.writeString(delivery_cost_with_tax);
        dest.writeString(shop_products_cost);
        dest.writeString(shop_products_cost_app_percentage);
        dest.writeString(shop_products_cost_app_percentage_value);
        dest.writeString(shop_products_app_percentage_with_tax);
        dest.writeString(total_cost);
    }
}
