package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.BankAccount;

import java.util.List;

import lombok.Getter;

@Getter
public class BankAccountResponse extends BaseResponse {

    private List<BankAccount> data;
}
