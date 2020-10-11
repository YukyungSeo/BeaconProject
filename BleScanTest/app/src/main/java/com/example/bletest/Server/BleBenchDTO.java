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

    public BleBenchDTO(String myid, String date, String sTime, String eTime) {
        this.benchID = myid;
        this.mDate = date;
        this.sTime = sTime;
        this.eTime = eTime;
    }

    public String getBenchID() {
        return benchID;
    }

    public void setBenchID(String benchID) {
        this.benchID = benchID;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }
}
