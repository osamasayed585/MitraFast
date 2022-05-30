package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AddPaymentCardRequest {
    private String credit_card_number;
    private String credit_card_name;
    private String cvv;
    private String credit_card_year;
    private String credit_card_month;

}