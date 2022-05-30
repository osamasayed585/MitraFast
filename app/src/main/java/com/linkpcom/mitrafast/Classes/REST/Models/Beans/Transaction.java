package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Transaction {
    private int id;
    private String status_name;
    private String reason;
    private String created_at;
    private String end_date;
    private String value;
    private Order order;
}