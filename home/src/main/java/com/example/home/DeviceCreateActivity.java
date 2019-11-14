package com.example.home;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.home.Lib.BaseActivity;

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
                String msg = "Create" + "/" + deviceNameET.getText().toString() + "/" + deviceCategoryET
                        .getText().toString() + "/" + deviceTypeTV
                        .getText().toString();
                sendCommand(msg);
            }
        });
    }
}
