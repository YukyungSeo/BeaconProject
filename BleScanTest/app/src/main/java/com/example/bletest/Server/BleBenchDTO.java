package com.example.bletest.Server;

import com.google.gson.annotations.SerializedName;

public class BleBenchDTO {
    @SerializedName("setname")
    String benchID;
    @SerializedName("mdate")
    String mDate;
    @SerializedName("sTime")
    String sTime;
    @SerializedName("eTime")
    String eTime;
}
