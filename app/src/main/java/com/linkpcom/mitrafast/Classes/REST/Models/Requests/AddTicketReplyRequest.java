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
public class AddTicketReplyRequest {
    private int support_ticket_id;
    private String reply;
    private MultipartBody.Part[] images;

}