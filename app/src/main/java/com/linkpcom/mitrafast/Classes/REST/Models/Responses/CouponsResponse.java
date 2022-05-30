package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Coupon;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CouponsResponse extends BaseResponse {
    private List<Coupon> data;
}