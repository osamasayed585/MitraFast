package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class ShopsRequest {
    private String keyword;
    private String lat;
    private String lng;
    private int category_id;
    private int classification_id;

}