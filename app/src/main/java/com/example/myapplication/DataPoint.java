package com.example.myapplication;

public class DataPoint {
    long time = 0;
    long latitude = 0;
    long longitude = 0;
    float accelerometer_x = 0.0f;
    float accelerometer_y = 0.0f;
    float accelerometer_z = 0.0f;

    public String to_csv() {
        return time + "," + latitude + "," + longitude + "," + accelerometer_x + "," + accelerometer_y + "," + accelerometer_z + "\n";
    }
}
