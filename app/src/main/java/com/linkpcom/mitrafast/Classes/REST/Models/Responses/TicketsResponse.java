package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Ticket;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketsResponse extends BaseResponse {
    private List<Ticket> data;
}