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
public class MainActivity extends Activity {

    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static final String SERVICE_ID = "com.iot.homeautomata";
    private static final String TAG = "com.iot.homeautomata";
    private static final String NAME = "PI";
    private static String endpoitId;

    private Handler mHandler = new Handler();
    private DeviceController deviceController = new DeviceController();
    private final PayloadCallback mPayloadCallBack = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            updateState(new String(payload.asBytes(), UTF_8));
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };
    private ConnectionsClient mConnectionsClient;
    private Runnable mDeviceControllerRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                deviceController.initDevices();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mConnectionsClient = Nearby.getConnectionsClient(this);
        mHandler.post(mDeviceControllerRunnable);
        startAdvertising();
    }


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

    //    Controling over mobile
    private void updateState(String s) {
        deviceController.updateDeviceState(s);
    }
}
