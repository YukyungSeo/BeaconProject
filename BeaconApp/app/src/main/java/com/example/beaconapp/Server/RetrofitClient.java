package com.example.beaconapp.Server;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private Retrofit retrofit;
    private RetrofitService retrofitservice;
    private BenchRtrf benchservice;

    public void sendBench(benchDTO bDTO){

        benchservice.sendBench(bDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("success!","bench 전송 완료!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Fail!",t.toString());
            }
        });

    }
    public void init2(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(benchservice.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        benchservice = retrofit.create(BenchRtrf.class);
    }

    public void init(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(retrofitservice.API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitservice = retrofit.create(RetrofitService.class);
    }

    public void SendBeacons(BeaconDTO beaconDTO){
        retrofitservice.sendBeacons(beaconDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Log.d("success!","비콘 신호 전송 완료!");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Fail!",t.toString());
            }
        });
    }

    public static String getDate(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(date);
    }
}
