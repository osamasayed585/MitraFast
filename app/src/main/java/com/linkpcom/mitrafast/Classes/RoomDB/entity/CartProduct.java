package com.linkpcom.mitrafast.Classes.RoomDB.entity;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Builder
@Entity(tableName = "cart_products")
public class CartProduct {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int product_id;
    private String name;
    private String image;
    private double cost;
    private int quantity;
    private int shopId;
    private String currency;
    private int type_id;
    private String type_name;
    private int size_id;
    private String size_name;
    private String extras_ids;
    private String extras_names;
    private String choices_ids;
    private String choices_names;
    private String min_value;
    private String max_value;
    private String distance_text;
    private String distance_value;
    private String duration_text;
    private String duration_value;
    private String delivery_cost;

    public CartProduct(int product_id, String name,  String image,double cost, int quantity, int shopId, String currency, int type_id, String type_name, int size_id, String size_name, String extras_ids, String extras_names, String choices_ids, String choices_names,String min_value,String max_value,String distance_text,String distance_value,String duration_text,String duration_value,String delivery_cost) {
        this.product_id = product_id;
        this.name = name;
        this.image = image;
        this.cost = cost;
        this.quantity = quantity;
        this.shopId = shopId;
        this.currency = currency;
        this.type_id = type_id;
        this.type_name = type_name;
        this.size_id = size_id;
        this.size_name = size_name;
        this.extras_ids = extras_ids;
        this.extras_names = extras_names;
        this.choices_ids = choices_ids;
        this.choices_names = choices_names;
        this.min_value = min_value;
        this.max_value = max_value;
        this.distance_text = distance_text;
        this.distance_value = distance_value;
        this.duration_text = duration_text;
        this.duration_value = duration_value;
        this.delivery_cost = delivery_cost;

    }

    @Ignore
    public CartProduct(int id, int product_id, String name,String image, double cost, int quantity, int shopId, String currency, int type_id, String type_name, int size_id, String size_name, String extras_ids, String extras_names, String choices_ids, String choices_names,String min_value,String max_value,String distance_text,String distance_value,String duration_text,String duration_value,String delivery_cost) {
        this.product_id = product_id;
        this.name = name;
        this.image = image;
        this.cost = cost;
        this.quantity = quantity;
        this.shopId = shopId;
        this.currency = currency;
        this.type_id = type_id;
        this.type_name = type_name;
        this.size_id = size_id;
        this.size_name = size_name;
        this.extras_ids = extras_ids;
        this.extras_names = extras_names;
        this.choices_ids = choices_ids;
        this.choices_names = choices_names;
        this.min_value = min_value;
        this.max_value = max_value;
        this.distance_text = distance_text;
        this.distance_value = distance_value;
        this.duration_text = duration_text;
        this.duration_value = duration_value;
        this.delivery_cost = delivery_cost;
    }


}
