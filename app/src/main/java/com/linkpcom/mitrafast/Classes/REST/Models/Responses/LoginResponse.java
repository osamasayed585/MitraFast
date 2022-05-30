package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Client;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FavoritePlace;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse extends BaseResponse {
    private Data data;
    private String access_token;
    private String token_type;

    @Setter
    @Getter
    public class Data {
        private Client user;
        private FavoritePlace favorite_places;
    }

    public Client getData() {
        return data.user;
    }

    public int getAddressId() {
        return data.favorite_places.getId();
    }
}