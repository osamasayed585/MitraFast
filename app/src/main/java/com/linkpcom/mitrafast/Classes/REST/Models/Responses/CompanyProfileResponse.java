package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.CompanyProfile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyProfileResponse extends BaseResponse {
    private CompanyProfile data;
}
