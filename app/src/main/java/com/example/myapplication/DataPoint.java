package com.example.myapplication;

public class DataPoint {
    long time = 0;
    long latitude = 0;
    long longitude = 0;
    float accelerometer_x = 0.0f;
    float accelerometer_y = 0.0f;
    float accelerometer_z = 0.0f;


    @Override
    public String toString() {
        return String.format("x: %f, y: %f, z: %f",this.accelerometer_x,this.accelerometer_y,this.accelerometer_z);
    }
}
