package com.example.homeautomata.Model;

import com.example.homeautomata.Controller.HardwareInputControleSystem;
import com.example.homeautomata.Controller.OutputControleSystem;

public class Device {
    private int deviceId;
    private String deviceName;
    private String deviceInputPort;
    private String deviceOutputPort;
    private String deviceType;
    private String deviceCategory;
    private boolean deviceState;

    private HardwareInputControleSystem hardwareInputControleSystem = new HardwareInputControleSystem();
    private OutputControleSystem outputControleSystem = new OutputControleSystem();

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceInputPort() {
        return deviceInputPort;
    }

    public void setDeviceInputPort(String deviceInputPort) {
        this.deviceInputPort = deviceInputPort;
    }

    public String getDeviceOutputPort() {
        return deviceOutputPort;
    }

    public void setDeviceOutputPort(String deviceOutputPort) {
        this.deviceOutputPort = deviceOutputPort;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public boolean isDeviceState() {
        return deviceState;
    }

    public void setDeviceState(boolean deviceState) {
        this.deviceState = deviceState;
    }

    public void init() {
        hardwareInputControleSystem.init(this, deviceInputPort);
        outputControleSystem.init(deviceOutputPort);
    }

    public void onDeviceStateChange(boolean deviceState) {
        this.deviceState = deviceState;
        outputControleSystem.setState(deviceState);
    }
}
