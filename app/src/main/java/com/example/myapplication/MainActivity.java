package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private LocationManager locationManager;

    private TextView location_txt;
    private TextView bump_score_txt;
    private Button start_button;
    private Button share_button;

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bump_score_txt = findViewById(R.id.bump_score_txt);
        location_txt = findViewById(R.id.location_txt);
        start_button = findViewById(R.id.start_button);
        share_button = findViewById(R.id.share_button);

        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onStartButtonClick();
            }
        });

        share_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onShareButtonClick();
            }
        });

        senSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        session = new Session(this);

        redraw();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        onLocationChanged(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    private void onStartButtonClick() {
        switch (session.getState()) {
            case STARTED:
                session.stopSession();
                break;
            case STOPPED:
            case NONE:
                session.startSession();
                break;
        }

        redraw();
    }

    private void onShareButtonClick() {
        Uri uri = FileProvider.getUriForFile(this, "com.example.myapplication", this.getFileStreamPath("session.txt"));

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    private void redraw() {
        switch (session.getState())
        {
            case STARTED:
                start_button.setText("stop");
                share_button.setVisibility(View.INVISIBLE);
                break;
            case STOPPED:
                start_button.setText("start again");
                share_button.setVisibility(View.VISIBLE);
                break;
            case NONE:
                start_button.setText("start");
                share_button.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            session.onAccelerometerEvent(System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]);
            bump_score_txt.setText("" + session.getBumpScore());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            session.onGeoEvent(location.getTime(), location.getLatitude(), location.getLongitude());
            location_txt.setText(location.getLatitude() + ", " + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
