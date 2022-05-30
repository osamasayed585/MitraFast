package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Client;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterResponse extends BaseResponse {
    private Client data;
    private String access_token;
    private String token_type;
}
