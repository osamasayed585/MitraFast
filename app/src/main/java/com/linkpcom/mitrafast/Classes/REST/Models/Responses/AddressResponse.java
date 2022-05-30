package com.linkpcom.mitrafast.Classes.REST.Models.Responses;


import android.location.Address;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddressResponse extends BaseResponse {
    private Address address;


}
