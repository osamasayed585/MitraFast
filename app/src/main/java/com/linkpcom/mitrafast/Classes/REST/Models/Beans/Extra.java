package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Extra {
    private int id;
    private String name;
    private double price;

    private transient boolean isChecked;

    public void reverseCheck() {
        isChecked = !isChecked;
    }
}