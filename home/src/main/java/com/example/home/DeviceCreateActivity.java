package com.example.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.home.Controller.DeviceManager;
import com.example.home.Lib.BaseActivity;
import com.example.home.Model.Device;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.UUID;

public class DeviceCreateActivity extends BaseActivity {
    private EditText deviceNameET, deviceCategoryET;
    private AutoCompleteTextView deviceTypeTV;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_create);
        initUI();
    }

    private void initUI() {
        deviceNameET = findViewById(R.id.device_name);
        deviceCategoryET = findViewById(R.id.device_category);
        deviceTypeTV = findViewById(R.id.device_type);
        createButton = findViewById(R.id.createDevice);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Device device = new Device();
                device.setDeviceId(UUID.randomUUID().toString());
                device.setDeviceName(deviceNameET.getText().toString());
                device.setDeviceType(deviceTypeTV.getText().toString());
                device.setDeviceCategory(deviceCategoryET.getText().toString());
                device.setDeviceState("create");
                String deviceJsonString = new Gson().toJson(device);
                DeviceManager.getInstance().addDevice(device);
                sendCommand(deviceJsonString);
            }
        });
    }
}
