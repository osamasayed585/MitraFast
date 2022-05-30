package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ProductsRequest {
    private int shop_id;
    @SerializedName("category_id")
    private int shop_category_id;
    private String keyword;
    private int page;

}