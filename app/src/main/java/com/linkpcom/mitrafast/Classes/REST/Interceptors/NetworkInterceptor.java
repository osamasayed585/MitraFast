package com.linkpcom.mitrafast.Classes.REST.Interceptors;

import android.content.Context;

import com.google.gson.Gson;
import com.linkpcom.mitrafast.Classes.REST.Models.Responses.BaseResponse;
import com.linkpcom.mitrafast.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ViewModelScoped;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

@ViewModelScoped
public class NetworkInterceptor implements Interceptor {


    @Inject
    public NetworkInterceptor() {
    }
//TODO message error handle needed;
    @Override
    public Response intercept(Chain chain) throws IOException  {
        Gson gson = new Gson();
        Response response = chain.proceed(chain.request());
        Response.Builder builder = response.newBuilder();
        ResponseBody body = response.body();
        String bodyString = body.string();
        if (isValidJSON(bodyString) && response.code() != 200) {
            BaseResponse baseResponse = gson.fromJson(bodyString, BaseResponse.class);
            baseResponse.setStatusCode(response.code());
            return builder.body(ResponseBody.create(gson.toJson(baseResponse), body.contentType())).code(200).build();
        } else
            return builder.body(ResponseBody.create(bodyString, body.contentType())).build();
    }

    public boolean isValidJSON(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}