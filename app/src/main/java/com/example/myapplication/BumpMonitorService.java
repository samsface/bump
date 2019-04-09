package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


public class BumpMonitorService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private static final String TAG = "CycleCrowd";
    private static PublishSubject<DataPoint> current = PublishSubject.create();

    public BumpMonitorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "starting BumpMonitorService");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            DataPoint dp = new DataPoint();
            dp.accelerometer_x = event.values[0];
            dp.accelerometer_y = event.values[1];
            dp.accelerometer_z = event.values[2];
            current.onNext(dp);
            long curTime = System.currentTimeMillis();
            if((curTime - lastUpdate) >100){
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                Log.d(TAG,String.format("x: %f, y: %f, z: %f",dp.accelerometer_x,dp.accelerometer_y,dp.accelerometer_z));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG,"task removed");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    public static Observable<DataPoint> getObservable(){
        return current;
    }

}
