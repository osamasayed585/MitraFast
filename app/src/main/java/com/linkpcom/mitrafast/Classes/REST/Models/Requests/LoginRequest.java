package com.linkpcom.mitrafast.Classes.REST.Models.Requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class LoginRequest {
    private String mobile;
    private String fire_base_token;
    private String password;
    private String country_id;
    private String lat;
    private String lng;
    private String Locality;
    private String SubLocality;
    private String AdminArea;
    private String address;
    private String name;


}
