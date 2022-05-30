package com.linkpcom.mitrafast.Classes.RoomDB.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM cart_products")
    List<CartProduct> getAllProducts();

    @Query("DELETE FROM cart_products WHERE id = :id")
    int deleteProductById(int id);

    @Query("SELECT SUM(cost * quantity) FROM cart_products")
    float getTotalProductsCost();

    @Query("DELETE FROM cart_products")
    int deleteAll();

    @Query("UPDATE cart_products SET quantity = :count WHERE id = :id")
    int updateProductCountById(int id, int count);

    @Query("SELECT * FROM cart_products WHERE product_id = :product_id AND type_id = :type_id AND size_id = :size_id AND extras_ids = :extras_ids AND choices_ids = :choices_ids")
    List<CartProduct> findSameProduct(int product_id, String type_id, String size_id, String extras_ids, String choices_ids);

    @Query("SELECT * FROM cart_products WHERE shopId != :shopId")
    List<CartProduct> requestIsAnotherStoreExist(int shopId);



    @Query("SELECT  SUM(quantity) FROM cart_products WHERE product_id = :product_id ")
    int requestProductCount(int product_id);



    @Insert
    long InsertProduct(CartProduct cartProduct);

}
