package com.drift.ecommerce.service;
import retrofit2.Call;
import retrofit2.http.GET;

import com.drift.ecommerce.model.PayList;
import com.drift.ecommerce.model.ProductList;

public interface RestApiService {

    @GET("products/")
    Call<ProductList> getProductList();
    @GET("payment-types")
    Call<PayList> getPayList();

}
