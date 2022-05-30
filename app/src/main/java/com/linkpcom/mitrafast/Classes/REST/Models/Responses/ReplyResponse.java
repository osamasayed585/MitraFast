package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Reply;
import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReplyResponse extends BaseResponse {
    private List<Reply> data;
}