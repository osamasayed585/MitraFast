package com.linkpcom.mitrafast.Classes.REST.Interceptors;


import android.util.Log;

import com.linkpcom.mitrafast.Classes.Others.MainApplication;
import com.linkpcom.mitrafast.Classes.Utils.PreferencesManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@ViewModelScoped
public class HeadersInterceptor implements Interceptor {
    private PreferencesManager preferencesManager;

    @Inject
    public HeadersInterceptor(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        requestBuilder.addHeader("lang", preferencesManager.getLanguage());
        requestBuilder.addHeader("Accept", "application/json");

        if (preferencesManager.getAuthenticationToken() != null) {
            requestBuilder.addHeader("Authorization", preferencesManager.getAuthenticationType() + " " + preferencesManager.getAuthenticationToken());
        }

        return chain.proceed(requestBuilder.build());
    }


}
