package com.example.homeautomata;

import android.os.Bundle;

import com.example.homeautomata.Controller.DeviceController;
import com.example.homeautomata.lib.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends BaseActivity {
    private static boolean isDeviceConnected = false;
    DeviceController deviceController = DeviceController.getInstance();
//    private Handler mHandler = new Handler();
//    private DeviceController deviceController = new DeviceController();
//    private Runnable mDeviceControllerRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                deviceController.initDevices();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startAdvertising();
    }

    @Override
    protected void onEndpointConnected(String endpoitId) {
        isDeviceConnected = true;
    }

    @Override
    protected void onEndpointDisconnected(String s) {
        isDeviceConnected = false;
    }

    @Override
    protected void onReceiveCommand(String command) {
        JsonElement jsonElement = new Gson().fromJson(command, JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        switch (jsonObject.get("deviceState").getAsString()) {
            case "create":
                String deviceJsonString = deviceController.createDevices(jsonObject, this);
                sendCommand(deviceJsonString);
                break;
        }
    }
}
