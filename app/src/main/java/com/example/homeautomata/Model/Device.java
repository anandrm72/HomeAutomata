package com.example.homeautomata.Model;

import com.example.homeautomata.Controller.HardwareInputControlSystem;
import com.example.homeautomata.Controller.OutputControlSystem;

public class Device {
    private String deviceId;
    private String deviceName;
    private Port deviceInputPort;
    private Port deviceOutputPort;
    //    private String deviceInputPort;
//    private String deviceOutputPort;
    private String deviceType;
    private String deviceCategory;
    private String deviceState;
    private boolean deviceSwitchState;

    private HardwareInputControlSystem hardwareInputControlSystem = new HardwareInputControlSystem();
    private OutputControlSystem outputControlSystem = new OutputControlSystem();

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

    public Port getDeviceInputPort() {
        return deviceInputPort;
    }

//    public String getDeviceInputPort() {
//        return deviceInputPort;
//    }

//    public void setDeviceInputPort(String deviceInputPort) {
//        this.deviceInputPort = deviceInputPort;
//    }
//
//    public String getDeviceOutputPort() {
//        return deviceOutputPort;
//    }
//
//    public void setDeviceOutputPort(String deviceOutputPort) {
//        this.deviceOutputPort = deviceOutputPort;
//    }

    public void setDeviceInputPort(Port deviceInputPort) {
        this.deviceInputPort = deviceInputPort;
    }

    public Port getDeviceOutputPort() {
        return deviceOutputPort;
    }

    public void setDeviceOutputPort(Port deviceOutputPort) {
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

    public void initInputControlesystem() {
        hardwareInputControlSystem.init(this, deviceInputPort.getPortName());
        deviceInputPort.setAvailable(false);
    }

    public void initOutputControlesystem() {
        outputControlSystem.init(deviceOutputPort.getPortName());
        deviceOutputPort.setAvailable(false);
    }

    public void onDeviceStateChange(boolean deviceState) {
        this.deviceSwitchState = deviceState;
        outputControlSystem.setState(deviceState);
    }
}
