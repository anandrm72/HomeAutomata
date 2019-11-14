package com.example.homeautomata.Controller;

import android.util.Log;
import android.widget.Toast;

import com.example.homeautomata.Model.Constants;
import com.example.homeautomata.Model.Device;
import com.example.homeautomata.Model.Port;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeviceController {
    private static DeviceController instance = null;
    private PortManager portManager;

    private DeviceController() {
        portManager = PortManager.getInstance();
    }

    public static DeviceController getInstance() {
        if (instance == null) {
            instance = new DeviceController();
        }
        return instance;
    }
    private List<Device> deviceList = new ArrayList<>();

//    public void initDevices() throws IOException {
//        Device lightDevice = new Device();
//        lightDevice.setDeviceId(Constants.DEVICE_ID);
//        lightDevice.setDeviceName(Constants.DEVICE_NAME);
//        lightDevice.setDeviceCategory(Constants.DEVICE_CATEGORY);
//        lightDevice.setDeviceInputPort(Constants.DEVICE_INPUT_PORT);
//        lightDevice.setDeviceOutputPort(Constants.DEVICE_OUTPUT_PORT);
//        lightDevice.setDeviceType(Constants.DEVICE_TYPE);
//        lightDevice.init();
//        deviceList.add(lightDevice);
//
//        Device lightDevice1 = new Device();
//        lightDevice1.setDeviceId(Constants.DEVICE_ID2);
//        lightDevice1.setDeviceName(Constants.DEVICE_NAME2);
//        lightDevice1.setDeviceCategory(Constants.DEVICE_CATEGORY2);
//        lightDevice1.setDeviceInputPort(Constants.DEVICE_INPUT_PORT2);
//        lightDevice1.setDeviceOutputPort(Constants.DEVICE_OUTPUT_PORT2);
//        lightDevice1.setDeviceType(Constants.DEVICE_TYPE2);
//        lightDevice1.init();
//        deviceList.add(lightDevice1);
//    }

//    public void updateDeviceState(String s) {
//        switch (s) {
//            case "On":
//                for (int i = 0; i < deviceList.size(); i++) {
//                    deviceList.get(i).onDeviceStateChange(true);
//                }
//                break;
//            case "Off":
//                for (int i = 0; i < deviceList.size(); i++) {
//                    deviceList.get(i).onDeviceStateChange(false);
//                }
//                break;
//        }
//    }

    public void createDevices(String deviceName, String deviceCategory, String deviceType) {
        Port inputPort = portManager.getPort();
        Port outputPort = portManager.getPort();
        if (inputPort != null && outputPort != null) {
            Device device = new Device();
            device.setDeviceName(deviceName);
            device.setDeviceId(UUID.randomUUID().toString());
            device.setDeviceCategory(deviceCategory);
            device.setDeviceType(deviceType);
            device.setDeviceInputPort(inputPort);
            device.setDeviceOutputPort(outputPort);
            device.init();
            deviceList.add(device);
        } else {
            Log.d("automata", "no ports available");
        }
    }
}
