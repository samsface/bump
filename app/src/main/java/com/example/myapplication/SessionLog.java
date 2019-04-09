package com.example.myapplication;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

public class SessionLog {

    private Context context;
    private Vector<String> buffer;

    public SessionLog(Context context) {
        this.context = context;
        buffer = new Vector<>();
        reset();
    }

    public void write(DataPoint dataPoint) {

        buffer.add(dataPoint.time + "," + dataPoint.latitude + "\n");

        if(buffer.size() > 50) {
            flush();
        }
    }

    public void reset() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("session.txt", Context.MODE_PRIVATE));
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }

    public void flush() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("session.txt", Context.MODE_APPEND | Context.MODE_PRIVATE));
            for(String s : buffer) {
                outputStreamWriter.write(s);
            }
            buffer.clear();
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }
}
