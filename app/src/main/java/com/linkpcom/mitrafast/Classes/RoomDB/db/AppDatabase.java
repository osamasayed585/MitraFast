package com.linkpcom.mitrafast.Classes.RoomDB.db;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.linkpcom.mitrafast.Classes.Others.MainApplication;
import com.linkpcom.mitrafast.Classes.RoomDB.dao.ProductDao;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;


@Database(entities = {CartProduct.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();


}
