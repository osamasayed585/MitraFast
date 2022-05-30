package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WalletResponse extends BaseResponse {
    private Data data;


    @Setter
    @Getter
    public static class Data  {
        private String value;
        private String currency;
    }

}