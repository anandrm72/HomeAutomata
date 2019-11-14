package com.example.homeautomata.Controller;

import android.util.Log;

import com.example.homeautomata.Model.Port;
import com.google.android.things.pio.PeripheralManager;

import java.util.ArrayList;
import java.util.List;

public class PortManager {
    private static final String TAG = "com.iot.homeautomata";
    private static PortManager instance = null;
    public PeripheralManager peripheralManager = PeripheralManager.getInstance();
    public List<Port> portList = new ArrayList<>();

    private PortManager() {
        updatePortList();
    }

    public static PortManager getInstance() {
        if (instance == null) {
            instance = new PortManager();
        }
        return instance;
    }

    public void updatePortList() {
        List<String> GpioList = peripheralManager.getGpioList();
        if (GpioList.isEmpty()) {
            Log.i(TAG, "No gpio pins available");
        } else {
            for (String pin : GpioList) {
                Port port = new Port();
                port.setPortName(pin);
                portList.add(port);
            }
        }
        for (Port port : portList) {
            Log.i(TAG, port.portName);
        }
    }

    public Port getPort() {
        for (Port port : portList) {
            if (port.isAvailable) {
                return port;
            }
        }
        return null;
    }
}
