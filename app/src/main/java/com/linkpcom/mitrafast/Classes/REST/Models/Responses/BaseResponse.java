package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    /**
     * message : تم استلام البيانات بنجاح
     * success : true
     */

    private String message;
    private boolean success;
    private int statusCode;


}
