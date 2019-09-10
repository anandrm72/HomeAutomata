package com.example.homeautomata.Controller;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.Closeable;
import java.io.IOException;

public class OutputControleSystem implements Closeable {
    private PeripheralManager peripheralManager = PeripheralManager.getInstance();
    private Gpio mGpio;
    private String port;
    private boolean state;

    public void init(String outputPort) {
        this.port = outputPort;
        configurePort();
    }

    private void configurePort() {
        try {
            mGpio = peripheralManager.openGpio(port);
            mGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setState(boolean state) {
        this.state = state;
        try {
            mGpio.setValue(state);
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
