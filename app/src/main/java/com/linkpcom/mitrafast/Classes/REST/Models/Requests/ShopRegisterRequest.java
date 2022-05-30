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
public class ShopRegisterRequest {
    private String name_ar;
    private String name_en;
    private String commercial_register;
    private String owner_mobile;
    private String mobile;
    private String lat;
    private String lng;
    private String address;
    private String work_start_at;
    private String end_start_at;
    private long value_added_number;
    private MultipartBody.Part image;
    private MultipartBody.Part commercial_registe_image;
    private MultipartBody.Part owner_identity_image;


}
