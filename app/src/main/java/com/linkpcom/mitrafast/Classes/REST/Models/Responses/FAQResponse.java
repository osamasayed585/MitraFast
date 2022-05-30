package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.FAQ;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FAQResponse extends BaseResponse {
    private List<FAQ> data;
}
