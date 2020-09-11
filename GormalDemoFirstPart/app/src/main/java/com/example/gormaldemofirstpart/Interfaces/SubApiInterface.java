package com.example.gormaldemofirstpart.Interfaces;

import com.example.gormaldemofirstpart.models.*;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SubApiInterface {

    @POST("addNewProduct")
    Call<ProductModel> requestServerToAddProduct(@Body Map<String, String> jsonObject);

}
