package com.example.homeautomata.Controller;

import com.example.homeautomata.MainActivity;
import com.example.homeautomata.Model.Device;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

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

    public String createDevices(JsonObject jsonObject, MainActivity mainActivity) {
        if (portManager.getTotalAvailblePort() >= 2) {
            Device device = new Device();
            device.setDeviceId(jsonObject.get("deviceId").getAsString());
            device.setDeviceName(jsonObject.get("deviceName").getAsString());
            device.setDeviceType(jsonObject.get("deviceType").getAsString());
            device.setDeviceCategory(jsonObject.get("deviceCategory").getAsString());
            device.setDeviceSwitchState(jsonObject.get("deviceSwitchState").getAsBoolean());
            device.setDeviceInputPort(portManager.getPort());
            device.initInputControlesystem();
            device.setDeviceOutputPort(portManager.getPort());
            device.initOutputControlesystem();
            device.setDeviceState("success");
            deviceList.add(device);

            return returnDeviceUpdates(device);
        }
        return null;
    }

//    {"deviceCategory":"kitchen","deviceId":"ff8f32a3-1448-42b2-970a-09ddc1033dac","deviceName":"kitchen light","deviceState":"create","deviceSwitchState":false,"deviceType":"light"}

    private String returnDeviceUpdates(Device device) {

        if (device.getDeviceState().equals("success")) {
            JsonObject deviceJsonObject = new JsonObject();
            deviceJsonObject.addProperty("deviceId", device.getDeviceId());
            deviceJsonObject.addProperty("deviceInputPort", device.getDeviceInputPort().getPortName());
            deviceJsonObject.addProperty("deviceOutputPort", device.getDeviceOutputPort().getPortName());
            deviceJsonObject.addProperty("deviceState", device.getDeviceState());
            deviceJsonObject.addProperty("deviceSwitchState", device.isDeviceSwitchState());


            String command = deviceJsonObject.toString();
            return command;
        }
        return null;
    }
}
