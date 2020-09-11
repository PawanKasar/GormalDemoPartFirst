package com.example.gormaldemofirstpart.RetroClient;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.gormaldemofirstpart.BaseUrlClient.ApiClient;
import com.example.gormaldemofirstpart.Interfaces.SubApiInterface;
import com.example.gormaldemofirstpart.utilities.URLs;


public class RetrofitApiUtils {
    public ProgressDialog pDialog;
    Context activity;

    public RetrofitApiUtils(Context activity) {
        this.activity = activity;
        this.pDialog = new ProgressDialog(activity);
    }

    public static SubApiInterface getAPIService() {
        return ApiClient.getClient(URLs.BASE_URL).create(SubApiInterface.class);

    }
}