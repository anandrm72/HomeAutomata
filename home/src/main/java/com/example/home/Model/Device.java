package com.example.home.Model;

public class Device {
    private String deviceId;
    private String deviceName;
    private String deviceInputPort;
    private String deviceOutputPort;
    private String deviceType;
    private String deviceCategory;
    private boolean deviceSwitchState;
    private String deviceState;

    public String getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
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

    public boolean isDeviceSwitchState() {
        return deviceSwitchState;
    }

    public void setDeviceSwitchState(boolean deviceSwitchState) {
        this.deviceSwitchState = deviceSwitchState;
    }
}
