package com.linkpcom.mitrafast.Classes.REST.Models.Requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import okhttp3.MultipartBody;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CompleteDataRequest {
    private String name;
    private String mobile;
    private String password;
    private MultipartBody.Part image;
    private String country_id;
    private String fire_base_token;


}
