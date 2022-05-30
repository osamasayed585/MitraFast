package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.TermsAndConditions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TermsAndConditionsResponse extends BaseResponse {
    private TermsAndConditions data;
}