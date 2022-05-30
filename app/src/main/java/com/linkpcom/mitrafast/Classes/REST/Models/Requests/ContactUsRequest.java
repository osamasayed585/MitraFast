package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ContactUsRequest {
    private String name;
    private String mobile;
    private String message;
    private String country_id;
    private int contact_us_subject_id;
    private String email;


}
