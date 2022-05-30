package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankAccountDetailsResponse extends BaseResponse {
    private BankAccount data;
}