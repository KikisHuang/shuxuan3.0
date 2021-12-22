package com.gxdingo.sg.bean;

public class changeLocationEvent {
    public String name;

    public double longitude;
    public double latitude;

    public changeLocationEvent(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
