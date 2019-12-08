package com.example.home.Controller;

import android.util.Log;

import com.example.home.Model.Device;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class DeviceManager {
    private static DeviceManager instance = null;
    public ArrayList<Device> deviceList = new ArrayList<>();

    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    public ArrayList<Device> getDeviceList() {
        return deviceList;
    }

    public void addDevice(Device device) {
        deviceList.add(device);
    }

    public void createDevice(JsonObject command) {
        Device device = new Device();
        device.setDeviceId(command.get("deviceId").getAsString());
        device.setDeviceName(command.get("deviceName").getAsString());
        device.setDeviceCategory(command.get("deviceCategory").getAsString());
        device.setDeviceType(command.get("deviceType").getAsString());
        device.setDeviceInputPort(command.get("deviceInputPort").getAsString());
        device.setDeviceOutputPort(command.get("deviceOutputPort").getAsString());
        device.setDeviceSwitchState(command.get("deviceState").getAsBoolean());
        deviceList.add(device);
        Log.d("Anand", "Size of deviceList" + deviceList.size());
    }

    public void updateDevice(JsonObject command) {
        Device device = getDeviceById(command.get("deviceId").getAsString());
        if (device != null) {
            device.setDeviceState(command.get("deviceState").getAsString());
            device.setDeviceInputPort(command.get("deviceInputPort").getAsString());
            device.setDeviceOutputPort(command.get("deviceOutputPort").getAsString());
            device.setDeviceSwitchState(command.get("deviceSwitchState").getAsBoolean());
        }
    }

    private Device getDeviceById(String deviceId) {
        for (Device device : deviceList) {
            if (device.getDeviceId().equals(deviceId)) {
                return device;
            }
        }
        return null;
    }
}
