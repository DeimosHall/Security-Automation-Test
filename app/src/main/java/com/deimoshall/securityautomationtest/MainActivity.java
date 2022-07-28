package com.deimoshall.securityautomationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Switch swSensor, swSensor2;
    TextView tvSensorValue;
    Button btnAlarm;
    private boolean sw_state = false;

    // Database references
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceSensorStatus = database.getReference("SensorStatus");
    DatabaseReference referenceSensorValue = database.getReference("SensorValue");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swSensor = findViewById(R.id.sw_sensor_1);
        swSensor2 = findViewById(R.id.sw_sensor_2);
        tvSensorValue = findViewById(R.id.tv_sensor_value);
        btnAlarm = findViewById(R.id.btn_alarm);

        referenceSensorValue.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String sensorValue = snapshot.child("Value").getValue().toString();
                if (sensorValue.equals("true")) {
                    tvSensorValue.setText("Estado: Intruso detectado");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swSensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                HashMap<Object, Object> info = new HashMap<>();
                if (!isChecked) {
                    tvSensorValue.setText("Estado: No disponible");
                    info.put("Value", "off");
                    referenceSensorStatus.setValue(info);
                    sw_state = false;
                } else {
                    tvSensorValue.setText("Estado: Todo en orden");
                    info.put("Value", "on");
                    referenceSensorStatus.setValue(info);
                    sw_state = true;
                }
            }
        });

        swSensor2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(MainActivity.this, "No disponible", Toast.LENGTH_SHORT).show();
                swSensor2.setChecked(false);
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_state) {
                    tvSensorValue.setText("Estado: Todo en orden");
                }
                HashMap<Object, Object> info = new HashMap<>();
                info.put("Value", "false");
                referenceSensorValue.setValue(info);
            }
        });
    }
}