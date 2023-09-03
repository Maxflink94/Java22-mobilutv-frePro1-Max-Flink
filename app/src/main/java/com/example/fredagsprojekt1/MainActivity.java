package com.example.fredagsprojekt1;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    SensorManager sm;
    TextView accText;
    View fragLayout;
    Sensor accSens;
    Sensor gyroSens;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accText = findViewById(R.id.accText);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accSens = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gyroSens = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accSens != null) {
            sm.registerListener(accListener, accSens, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(accListener);
    }

    private final SensorEventListener accListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            String accData = "X: " + x + "\nY: " + y + "\nZ: " + z;
            accText.setText(accData);

            View layout = findViewById(R.id.main);

            if (layout != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    layout.setBackgroundColor(Color.rgb(z, x, y));
                }
            }

            if (x < -3 || x > 3) {
                Log.d("X-värde", String.valueOf(x));
            }

            if (x > 0 || y > 9.81 || z < 0) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().add(R.id.frame, BlankFragment.class, null).commit();
                fragLayout = findViewById(R.id.fragLayout);
                imageView = findViewById(R.id.imageView2);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && fragLayout != null) {
                    fragLayout.setBackgroundColor(Color.rgb(x, y, z));
                }
                if (imageView != null) {
                    imageView.setRotation(x * 360);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

}