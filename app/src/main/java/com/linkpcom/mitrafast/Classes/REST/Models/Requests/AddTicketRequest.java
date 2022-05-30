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
public class AddTicketRequest {
    private int ticket_type_id;
    private String name;
    private String mobile;
    private String message;
    private MultipartBody.Part[] images;


    private int support_section_id;
    private String email;

}