package com.linkpcom.mitrafast.Classes.REST.Models.Requests;

import com.google.gson.Gson;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MakeOrderRequest {
    private int shop_id;
    private int payment_id;
    private String credit_card_id;
    private String products;
    private String location_id;
    private String notes;
    private String coupon_id;


    public void setOrderProducts(List<CartProduct> products) {
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            CartProduct cartProduct = products.get(i);
            OrderProduct orderProduct = OrderProduct.builder()
                    .product_price_id(cartProduct.getProduct_id())
                    .count(cartProduct.getQuantity())
                    .price(String.valueOf(cartProduct.getCost()))
                    .build();


            if (cartProduct.getType_id() != 0) {
                orderProduct.setProduct_type_id(cartProduct.getType_id());
            }
            if (cartProduct.getExtras_ids() != null) {
                if (cartProduct.getExtras_ids().length() > 2)
                    orderProduct.setProduct_extra(cartProduct.getExtras_ids());
            }
            if (cartProduct.getChoices_ids() != null) {
                if (cartProduct.getChoices_ids().length() > 2)
                    orderProduct.setProduct_chocies(cartProduct.getChoices_ids());
            }
            if (cartProduct.getSize_id() != 0) {
                orderProduct.setProduct_size_id(cartProduct.getSize_id());
            }
            orderProducts.add(orderProduct);
        }
        Gson gson = new Gson();
        this.products = gson.toJson(orderProducts);
    }

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class OrderProduct {
        private Integer product_price_id;
        private int count;
        private Integer product_type_id;
        private Integer product_size_id;
        private String product_extra;
        private String product_chocies;
        private String price;
    }


}
