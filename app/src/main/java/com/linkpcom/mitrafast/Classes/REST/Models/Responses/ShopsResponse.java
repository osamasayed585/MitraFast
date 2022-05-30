package com.linkpcom.mitrafast.Classes.REST.Models.Responses;


import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Shop;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShopsResponse extends BaseResponse {
    private List<Shop> data;
}
