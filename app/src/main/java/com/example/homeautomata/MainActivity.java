package com.example.homeautomata;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.homeautomata.Controller.DeviceController;
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
public class MainActivity extends Activity {

    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static final String SERVICE_ID = "com.iot.homeautomata";
    private static final String TAG = "com.iot.homeautomata";
    private static final String NAME = "Mobile device";
    private static String endpoitId;
    private final PayloadCallback mPayloadCallBack = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };

    private Handler mHandler = new Handler();
    private DeviceController deviceController = new DeviceController();
    private ConnectionsClient mConnectionsClient;

    private Runnable mDeviceControllerRunable = new Runnable() {
        @Override
        public void run() {
            try {
                deviceController.initDevices();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
            mConnectionsClient.acceptConnection(s, mPayloadCallBack);
            Log.d(TAG, "Connecting to : " + connectionInfo.getEndpointName());
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            Log.d(TAG, connectionResolution.getStatus().getStatusMessage());
            if (connectionResolution.getStatus().isSuccess()) {
                Log.d(TAG, "Connected successfully with " + s);
                mConnectionsClient.stopAdvertising();
                endpoitId = s;
            } else {
                Log.d(TAG, "Connection failed");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            Log.d(TAG, "Disconnected from endpoint");
            finish();
            System.exit(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConnectionsClient = Nearby.getConnectionsClient(this);
        mHandler.post(mDeviceControllerRunable);
        startAdvertising();
    }

    private void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        mConnectionsClient.startAdvertising(NAME, SERVICE_ID, mConnectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Advertising...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                });
    }
}
