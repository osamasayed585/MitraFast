package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrdersResponse extends BaseResponse {
    private List<Order> data;
}
