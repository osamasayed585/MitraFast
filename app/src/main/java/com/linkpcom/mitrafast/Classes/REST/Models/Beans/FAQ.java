package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FAQ {
    private int id;
    private String question;
    private String answer;
    private boolean is_visible;

    public void inverseVisibility() {
        is_visible = !is_visible;
    }
}