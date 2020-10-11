package com.example.bletest.Server;

import com.google.gson.annotations.SerializedName;

public class BleDTO {
    @SerializedName("my_id")
    String myID;

    @SerializedName("other_id")
    String otherID;

    @SerializedName("rssi")
    String RSSI;

    @SerializedName("mDate")
    String mDate;

    @SerializedName("mTime")
    String mTime;

    public BleDTO(String myid, String otherID, String rssi, String date, String time) {
        this.myID = myid;
        this.otherID = otherID;
        this.RSSI = rssi;
        this.mDate = date;
        this.mTime = time;
    }

    public String getMyID() {
        return myID;
    }

    public void setMyID(String myID) {
        this.myID = myID;
    }

    public String getOtherID() {
        return otherID;
    }

    public void setOtherID(String otherID) {
        this.otherID = otherID;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }
}
