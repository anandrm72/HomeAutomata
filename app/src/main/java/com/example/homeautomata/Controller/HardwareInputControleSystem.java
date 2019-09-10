package com.example.homeautomata.Controller;

import android.util.Log;

import com.example.homeautomata.Model.Device;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import java.io.Closeable;
import java.io.IOException;

public class HardwareInputControleSystem implements Closeable {
    private static final String TAG = "Honey";
    private PeripheralManager peripheralManager = PeripheralManager.getInstance();
    private Gpio mGpio;
    private String port;
    private Device mDevice;
    private GpioCallback gpioCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                if (gpio.getValue()) {
                    //pin high
//                    ((StateChangedListener)getPeripheralManager()).onStateChange(true);
                    Log.i(TAG, "Button released  " + mDevice.getDeviceName());
                    mDevice.onDeviceStateChange(false);
                } else {
                    //pin low
//                    ((StateChangedListener)getPeripheralManager()).onStateChange(false);
                    Log.i(TAG, "Button pressed  " + mDevice.getDeviceName());
                    mDevice.onDeviceStateChange(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage().toString());
            }
            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            Log.w(TAG, gpio + "Error event : " + error);
        }
    };

    public void init(Device device, String inputPort) {
        this.port = inputPort;
        this.mDevice = device;
        configure();
    }

    private void configure() {
        try {
            mGpio = peripheralManager.openGpio(port);
            mGpio.setDirection(Gpio.DIRECTION_IN);
            mGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
            mGpio.registerGpioCallback(gpioCallback);
            Log.i(TAG, "Configuring input port   " + mDevice.getDeviceName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        if (mGpio != null) {
            try {
                mGpio.close();
            } finally {
                mGpio = null;
            }
        }
    }
}
