package com.example.beaconapp.Server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface BenchRtrf {
    public static final String API_URL = "http://168.188.129.191/";

    @POST("send_beacon_bench.php/")
    Call<Void> sendBench(@Body benchDTO bDTO);

}
