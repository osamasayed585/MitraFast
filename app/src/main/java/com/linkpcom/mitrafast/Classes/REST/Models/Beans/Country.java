package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Country {
    private int id;
    private String image;
    private String name;
    private String code;
    private String start_with;
    private String number_count;
    private String identity_count;
    private String identity_startWith;
    private Currency currency;
    private String iban_word;
    private String iban_number_count;


}
