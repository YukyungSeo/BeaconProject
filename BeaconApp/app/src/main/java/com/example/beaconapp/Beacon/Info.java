package com.example.beaconapp.Beacon;

public class Info {
    private static Info info = null;
    private String id;
    private String time;
    private String real_x;
    private String real_y;

    private Info(){}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReal_x() {
        return real_x;
    }

    public void setReal_x(String real_x) {
        this.real_x = real_x;
    }

    public String getReal_y() {
        return real_y;
    }

    public void setReal_y(String real_y) {
        this.real_y = real_y;
    }

    public static Info getInstance(){
        if(info == null){
            info = new Info();
        }
        return info;
    }
}
