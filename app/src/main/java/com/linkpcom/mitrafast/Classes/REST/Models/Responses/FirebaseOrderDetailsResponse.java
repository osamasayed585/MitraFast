package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class FirebaseOrderDetailsResponse  {
    private String delivery_cost;
    private String distance_text;
    private String duration_text;
    private String duration_value;
    private String location;
    private String total_cost;
}