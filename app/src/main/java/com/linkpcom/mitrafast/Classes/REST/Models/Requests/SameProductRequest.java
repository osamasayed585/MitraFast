package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SameProductRequest {
    private int product_id;
    private String type_id;
    private String size_id;
    private String extras_ids;
    private String choices_ids;
//    private String notice;

}