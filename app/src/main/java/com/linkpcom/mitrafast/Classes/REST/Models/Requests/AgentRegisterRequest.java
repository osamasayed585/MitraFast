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
public class AgentRegisterRequest {
    private String mobile;
    private int country_id;
    private String name;
    private String identity_name;
    private String identity_id;
    private String email;
    private String birthday_date;
    private MultipartBody.Part image;
    private MultipartBody.Part identity_image;
    private MultipartBody.Part driving_license_image;
    private MultipartBody.Part car_insurance_image;
    private String fire_base_token;

    private int nationality_id;
    private String company_id;
    private String time_type;
    private String stc_pay_number;

}