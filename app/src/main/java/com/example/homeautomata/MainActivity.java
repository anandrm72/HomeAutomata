package com.example.homeautomata;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.homeautomata.Controller.DeviceController;
import com.example.homeautomata.lib.BaseActivity;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

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
//        mHandler.post(mDeviceControllerRunnable);
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
    protected void onReceiveCommand(String[] cmd) {
        switch (cmd[0]) {
            case "Create":
                showToast("Creating....");
                deviceController.createDevices(cmd[1], cmd[2], cmd[3]);
        }
    }

    //    Controling over mobile
//    private void updateState(String s) {
//        deviceController.updateDeviceState(s);
//    }
}
