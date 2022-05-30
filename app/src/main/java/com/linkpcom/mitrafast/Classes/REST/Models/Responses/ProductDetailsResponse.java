package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Product;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductDetailsResponse extends BaseResponse {
    private Product data;
}