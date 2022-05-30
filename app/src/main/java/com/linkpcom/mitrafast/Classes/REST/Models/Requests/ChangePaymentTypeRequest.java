package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ChangePaymentTypeRequest {
    private String order_id;
    private int payment_id;

}