package com.linkpcom.mitrafast.Classes.DI.Modules;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.linkpcom.mitrafast.Classes.RoomDB.dao.ProductDao;
import com.linkpcom.mitrafast.Classes.RoomDB.db.AppDatabase;
import com.linkpcom.mitrafast.Classes.RoomDB.entity.CartProduct;

import java.util.List;

import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DataBaseModule {

    @Provides
    @Singleton
    public static AppDatabase provideAppDatabase(Application application) {
        return Room.databaseBuilder(application,
                AppDatabase.class, "mitrafast")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public static ProductDao provideProductDao(AppDatabase appDatabase) {
        return appDatabase.productDao();
    }

    @Provides
    public MutableLiveData<List<CartProduct>> provideCartProductsMutableLiveData() {
        return new MutableLiveData<>();
    }

}
