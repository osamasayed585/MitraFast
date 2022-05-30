package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Coupon;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponResponse extends BaseResponse {
    private Coupon data;
}