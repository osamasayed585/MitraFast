package com.linkpcom.mitrafast.Classes.REST.Models.Beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Offer {
    private int id;
    private String image;

    private int url_type_id;
    private int shop_id;

    private String offer_url;

}