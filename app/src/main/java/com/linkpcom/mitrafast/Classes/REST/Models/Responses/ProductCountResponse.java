package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ProductCountResponse {
    private int id;
    private int count;

}