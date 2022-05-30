package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Status;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatusesResponse extends BaseResponse {
    private List<Status> data;
}