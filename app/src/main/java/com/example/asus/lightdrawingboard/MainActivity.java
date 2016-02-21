package com.example.asus.lightdrawingboard;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements DrawingBoard.onLightLevelListener{
    private DrawingBoard drawboard;
    private Button btnClean;
    private Button btnCleanCanvace;
    private SensorManager sensorManager;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawboard= (DrawingBoard) findViewById(R.id.drawborad);
        btnClean= (Button) findViewById(R.id.btnClean);

        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawboard.clean();
            }
        });

        btnCleanCanvace= (Button) findViewById(R.id.btnCleanCanvace);
        btnCleanCanvace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawboard.cleanCanvace();
            }
        });

        /*获取到SensorManager的实例
          得到光传感器
         */
        sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        textView= (TextView) findViewById(R.id.textView);

        drawboard.setOnLightLevelListener(this);//设置监听器

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=sensorManager)
            sensorManager.unregisterListener(listener);//释放所用资源
    }

    private float value;
    private SensorEventListener listener=new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            value=event.values[0];//vaules数组中第一个下标的值为当前的光照强度
            textView.setText("Current light is"+value+"lx");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public float onLightlevel() {
        return this.value;
    }
}
