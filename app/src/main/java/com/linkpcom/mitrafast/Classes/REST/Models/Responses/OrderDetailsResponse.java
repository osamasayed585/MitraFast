package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDetailsResponse extends BaseResponse {
    private Order data;
}