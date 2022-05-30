package com.linkpcom.mitrafast.Classes.REST.Models.Responses;


import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Offer;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OffersResponse extends BaseResponse {
    private List<Offer> data;
}