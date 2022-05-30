package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ShopDetailsRequest {
    private int shop_id;
    private String lat;
    private String lng;


}