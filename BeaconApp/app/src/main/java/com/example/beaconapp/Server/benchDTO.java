package com.example.beaconapp.Server;

import com.google.gson.annotations.SerializedName;

public class benchDTO {
    @SerializedName("id")
    private String id;

    @SerializedName("setname")
    private String setname;

    @SerializedName("mdate")
    private String mdate;

    @SerializedName("sTime")
    private String sTime;

    @SerializedName("eTime")
    private String eTime;


    public benchDTO(){

    }

    public benchDTO(String ID, String Setname, String Mdate, String Stime, String Etime){
        this.id = ID;
        this.setname=Setname;
        this.mdate = Mdate;
        this. sTime = Stime;
        this.eTime= Etime;
    }

}
