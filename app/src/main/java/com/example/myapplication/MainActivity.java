package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "CycleCrowd";
    private TextView bump_score_txt;
    private Button start_button;
    private Button share_button;
    private PendingIntent scheduledIntent;
    private AlarmManager scheduler;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        bump_score_txt = findViewById(R.id.bump_score_txt);
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

        session = new Session(this);

        redraw();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }

    private void onStartButtonClick() {
        switch (session.getState())
        {
            case STARTED:
                Log.d(TAG, "stop clicked");
                scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                scheduler.cancel(scheduledIntent);
                Intent myService = new Intent(MainActivity.this, BumpMonitorService.class);
                stopService(myService);
                session.stopSession();
                break;
            case STOPPED:
            case NONE:
                session.startSession();
                Log.d(TAG,"started session");
                scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, BumpMonitorService.class);
                scheduledIntent = PendingIntent.getService(getApplicationContext(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                scheduler.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 , scheduledIntent);
                break;
        }

       redraw();
    }

    private void onShareButtonClick() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("session.txt")));
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
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


    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            session.onAccelerometerEvent(event.values[0], event.values[1], event.values[2]);
            bump_score_txt.setText("" + session.getBumpScore());
        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
