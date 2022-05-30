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
public class EditAgentProfileRequest {

    private String name;
    private String identity_name;
    private String identity_id;
    private String email;
    private MultipartBody.Part image;

}
