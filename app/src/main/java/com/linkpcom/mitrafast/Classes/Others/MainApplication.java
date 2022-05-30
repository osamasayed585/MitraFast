package com.linkpcom.mitrafast.Classes.Others;

import android.app.Application;
import android.content.Context;

import com.linkpcom.mitrafast.BuildConfig;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class MainApplication extends Application {

    @Inject
    Timber.DebugTree debugTree;
    private static boolean isPaymentSuccess;

    public static int creditCardId;
    public static String value;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(debugTree);
        }
    }


    public static boolean isPaymentSuccess() {
        return isPaymentSuccess;
    }

    public static void setPaymentSuccess(boolean isPaymentSuccess) {
        MainApplication.isPaymentSuccess = isPaymentSuccess;
    }



}