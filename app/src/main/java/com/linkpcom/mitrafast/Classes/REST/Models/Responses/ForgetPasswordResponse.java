package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ForgetPasswordResponse extends BaseResponse {
    private String access_token;
    private String token_type;
}
