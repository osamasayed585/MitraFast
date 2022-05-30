package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class AcceptedForOrderRequest {
    private boolean is_order_deleted;
    private boolean is_agent_exist;
    private String order_status;

}