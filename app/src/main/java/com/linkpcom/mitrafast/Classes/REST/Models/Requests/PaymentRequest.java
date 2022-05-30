package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PaymentRequest {
    private String credit_card_id;
    private String creditcard_id;
    private String order_id;
    private String payment_type;
    private String redirect_url;
    private double price;


}