package com.example.beaconapp.Server;

import com.google.gson.annotations.SerializedName;

public class BeaconDTO {
    @SerializedName("id")
    private String id;

    @SerializedName("B1_MAC")
    private String B1_MAC;

    @SerializedName("B1_RSSI")
    private String B1_RSSI;

    @SerializedName("B2_MAC")
    private String B2_MAC;

    @SerializedName("B2_RSSI")
    private String B2_RSSI;

    @SerializedName("B3_MAC")
    private String B3_MAC;

    @SerializedName("B3_RSSI")
    private String B3_RSSI;

    @SerializedName("B4_MAC")
    private String B4_MAC;

    @SerializedName("B4_RSSI")
    private String B4_RSSI;

    @SerializedName("mdate")
    private String mDate;

    @SerializedName("mTime")
    private String mTime;

    public BeaconDTO(){

    }

    public BeaconDTO(String id, String B1_MAC, String B1_RSSI, String B2_MAC, String B2_RSSI, String B3_MAC, String B3_RSSI, String B4_MAC, String B4_RSSI,String mDate, String mTime){
        this.id = id;
        this.B1_MAC = B1_MAC;
        this.B1_RSSI = B1_RSSI;
        this.B2_MAC = B2_MAC;
        this.B2_RSSI = B2_RSSI;
        this.B3_MAC = B3_MAC;
        this.B3_RSSI = B3_RSSI;
        this.B4_MAC = B4_MAC;
        this.B4_RSSI = B4_RSSI;
        this.mDate = mDate;
        this.mTime = mTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getB1_MAC() {
        return B1_MAC;
    }

    public void setB1_MAC(String b1_MAC) {
        B1_MAC = b1_MAC;
    }

    public String getB1_RSSI() {
        return B1_RSSI;
    }

    public void setB1_RSSI(String b1_RSSI) {
        B1_RSSI = b1_RSSI;
    }

    public String getB2_MAC() {
        return B2_MAC;
    }

    public void setB2_MAC(String b2_MAC) {
        B2_MAC = b2_MAC;
    }

    public String getB2_RSSI() {
        return B2_RSSI;
    }

    public void setB2_RSSI(String b2_RSSI) {
        B2_RSSI = b2_RSSI;
    }

    public String getB3_MAC() {
        return B3_MAC;
    }

    public void setB3_MAC(String b3_MAC) {
        B3_MAC = b3_MAC;
    }

    public String getB3_RSSI() {
        return B3_RSSI;
    }

    public void setB3_RSSI(String b3_RSSI) {
        B3_RSSI = b3_RSSI;
    }

    public String getB4_MAC() {
        return B4_MAC;
    }

    public void setB4_MAC(String b4_MAC) {
        B4_MAC = b4_MAC;
    }

    public String getB4_RSSI() {
        return B4_RSSI;
    }

    public void setB4_RSSI(String b4_RSSI) {
        B4_RSSI = b4_RSSI;
    }

}
