package com.example.homeautomata.Controller;

import android.util.Log;

import com.example.homeautomata.Model.Constants;
import com.example.homeautomata.Model.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceController {
    private List<Device> deviceList = new ArrayList<>();

    public void initDevices() throws IOException {
        Device lightDevice = new Device();
        lightDevice.setDeviceId(Constants.DEVICE_ID);
        lightDevice.setDeviceName(Constants.DEVICE_NAME);
        lightDevice.setDeviceCategory(Constants.DEVICE_CATEGORY);
        lightDevice.setDeviceInputPort(Constants.DEVICE_INPUT_PORT);
        lightDevice.setDeviceOutputPort(Constants.DEVICE_OUTPUT_PORT);
        lightDevice.setDeviceType(Constants.DEVICE_TYPE);
        lightDevice.init();
        deviceList.add(lightDevice);

        Device lightDevice1 = new Device();
        lightDevice1.setDeviceId(Constants.DEVICE_ID2);
        lightDevice1.setDeviceName(Constants.DEVICE_NAME2);
        lightDevice1.setDeviceCategory(Constants.DEVICE_CATEGORY2);
        lightDevice1.setDeviceInputPort(Constants.DEVICE_INPUT_PORT2);
        lightDevice1.setDeviceOutputPort(Constants.DEVICE_OUTPUT_PORT2);
        lightDevice1.setDeviceType(Constants.DEVICE_TYPE2);
        lightDevice1.init();
        deviceList.add(lightDevice1);
    }
}
