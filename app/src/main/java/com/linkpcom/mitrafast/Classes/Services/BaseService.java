package com.linkpcom.mitrafast.Classes.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.linkpcom.mitrafast.Classes.Others.MainApplication;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class BaseService extends Service {

    protected static final String PACKAGE_NAME = "com.linkpcom.mitrafast";
    private final IBinder mBinder = new LocalBinder();
    @Inject
    PreferencesManager preferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("onCreate");

    }


    @Override
    public IBinder onBind(Intent intent) {
        Timber.d("onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Timber.d("onUnbind");
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        Timber.d("onRebind");
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy");

        //TODO: Shady See What This Should Do

//        try {
//            fireBaseHelper.removeOrderListener();
//        } catch (Exception e) {
//            Timber.e(e);
//        }
        super.onDestroy();
    }


//TODO: Shady See What This Should Do

//    protected void startOrderListener() {
//        fireBaseHelper.startOrderListener();
//    }


    public class LocalBinder extends Binder {
        public BaseService getService() {
            return BaseService.this;
        }
    }

}
