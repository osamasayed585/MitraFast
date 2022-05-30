package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankAccount {
    private int id;
    private Bank bank;
    private String account_name;
    private String iban_number;

}