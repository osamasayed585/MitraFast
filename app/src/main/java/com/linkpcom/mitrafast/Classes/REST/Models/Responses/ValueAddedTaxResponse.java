package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.ValueAddedTax;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValueAddedTaxResponse extends BaseResponse {
    private ValueAddedTax data;
}