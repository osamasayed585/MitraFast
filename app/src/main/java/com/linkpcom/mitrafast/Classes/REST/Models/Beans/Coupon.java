package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Coupon {
    private int id;
    private String coupon_number;
    private String expiry_date;
    private String coupon_type;
    private String discount_value;



    private Voucher voucher;

    public String getCoupon_discount() {
        return voucher.discount;
    }

    @Setter
    @Getter
    public class Voucher {


        private int id;
        private String code;
        private String description;
        private String discount;
        // How many times a user can use this voucher.
        private int max_uses_user;
        // The type can be: gift, discount.
        private String type_id;
        // When is enable or not
        private boolean is_enable;
        // Whether or not the voucher is a percentage or a fixed price.
        private boolean is_fixed;
        // When the voucher begins
        private long starts_at;
        // When the voucher ends
        private String expires_at;
        private String number_of_use;
        private float maximum_discount;
        private int shop_id;
        private Type type;


    }
    @Setter
    @Getter
    public class Type {
        private int id;
        private String name;

    }
}