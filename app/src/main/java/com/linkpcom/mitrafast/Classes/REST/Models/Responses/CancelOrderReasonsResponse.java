package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.CancelReason;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelOrderReasonsResponse extends BaseResponse {
    private List<CancelReason> data;
}
