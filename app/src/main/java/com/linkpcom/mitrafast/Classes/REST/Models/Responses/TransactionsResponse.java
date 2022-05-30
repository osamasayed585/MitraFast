package com.linkpcom.mitrafast.Classes.REST.Models.Responses;

import com.linkpcom.mitrafast.Classes.REST.Models.Beans.Transaction;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionsResponse extends BaseResponse {
    private List<Transaction> data;
}