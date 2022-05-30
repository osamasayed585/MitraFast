package com.linkpcom.mitrafast.Classes.REST.Models.Beans;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Product {
    private int id;
    private NonPricedProduct product;
    private String product_name;
    private String image;
    private String cost;
    private double price;
    private String currency;
    private int choice_min;
    private int choice_max;
    private int extra_min;
    private int extra_max;
    private List<Type> types;
    private List<Size> sizes;
    private List<Extra> extras;
    private List<Choice> choices;
    private transient int count;
    private Shop shop;



    @Setter
    @Getter
    public class NonPricedProduct {
        private String name;
        private String description;
        private String cal;


    }

    public String getCal() {
        return product.cal;
    }

    public String getDescription() {
        return product.description;
    }
}
