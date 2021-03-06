package com.example.diogo.app_sensores_android;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;
    private View root;
    private float maxValue;
    private TextView tvLuz;
    private ImageView imgViewCaution;
    private Vibrator vibrator;
    private float maxValueProx;
    private TextView tvProx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
        tvLuz = findViewById(R.id.tvValorLuz);
        imgViewCaution = findViewById(R.id.imgViewCaution);
        tvProx = findViewById(R.id.tvValorProx);
        vibrator = (Vibrator )getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (lightSensor == null) {
            Toast.makeText(this, "O dispositivo não tem sensor de luminosidade!", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (proximitySensor == null) {
            Toast.makeText(this, "O dispositivo não tem sensor de proximidade!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // valor maximo do sensor de luminosidade
        maxValue = lightSensor.getMaximumRange();

        //valor máximo proximidade
        maxValueProx = proximitySensor.getMaximumRange();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType()==Sensor.TYPE_LIGHT){
            float value = sensorEvent.values[0];
            tvLuz.setText("Luminosidade : " + value + " lx");
            // entre 0 e 255
            int newValue = (int) (255f * value / maxValue);
            root.setBackgroundColor(Color.rgb(newValue*100, newValue*100, newValue*100));

        }
        if (sensorEvent.sensor.getType()==Sensor.TYPE_PROXIMITY){
            float value = sensorEvent.values[0];
            tvProx.setText("Proximidade = " + value);
            if (value==maxValueProx){
                imgViewCaution.setVisibility(View.INVISIBLE);
                vibrator.cancel();
            }
            else{
                imgViewCaution.setVisibility(View.VISIBLE);
                vibrator.vibrate(99999);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
