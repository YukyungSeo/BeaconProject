package com.example.bletest.Server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitService {
    public static final String API_URL = "http://168.188.129.191/";

    @POST("send_bleData.php/")
    Call<Void> sendBles(@Body BleDTO bleData);

    @POST("send_bleBenchData.php/")
    Call<Void> sendBleBench(@Body BleBenchDTO bleBenchDTO);

}
