package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShopDetailsResponse extends BaseResponse {
    private Shop data;

}