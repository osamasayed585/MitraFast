package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.PaymentMethod;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentMethodsResponse extends BaseResponse {
   private List<PaymentMethod> data;

}
