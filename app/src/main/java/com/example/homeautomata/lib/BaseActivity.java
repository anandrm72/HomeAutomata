package com.example.homeautomata.lib;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import static java.nio.charset.StandardCharsets.UTF_8;

public class BaseActivity extends Activity {
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static final String SERVICE_ID = "com.iot.homeautomata";
    private static final String TAG = "com.iot.homeautomata";
    private static final String NAME = "PI";
    private static String endpoitId;
    private final PayloadCallback mPayloadCallBack = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            receiveCommand(new String(payload.asBytes(), UTF_8));
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };

    private ConnectionsClient mConnectionsClient;
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
            mConnectionsClient.acceptConnection(s, mPayloadCallBack);
            logD("Connecting to : " + connectionInfo.getEndpointName());
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            logD(connectionResolution.getStatus().getStatusMessage());
            if (connectionResolution.getStatus().isSuccess()) {
                logD("Connected successfully with " + s);
                mConnectionsClient.stopAdvertising();
                endpoitId = s;
                connectedToEndpoint(endpoitId);
            } else {
                logD("Connection failed");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            logD("Disconnected from endpoint");
            disconnectedFromEndpoint(s);
            finish();
            System.exit(0);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectionsClient = Nearby.getConnectionsClient(this);
    }

    protected void startAdvertising() {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        mConnectionsClient.startAdvertising(NAME, SERVICE_ID, mConnectionLifecycleCallback, advertisingOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        logD("Advertising...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        logD(e.getMessage());
                        e.printStackTrace();
                    }
                });
    }

    private void disconnectedFromEndpoint(String s) {
        onEndpointDisconnected(s);
    }

    protected void onEndpointDisconnected(String s) {
    }

    private void connectedToEndpoint(String endpoitId) {
        onEndpointConnected(endpoitId);
    }

    protected void onEndpointConnected(String endpoitId) {
    }

    private void receiveCommand(String command) {
        onReceiveCommand(command);
    }

    protected void onReceiveCommand(String command) {
    }

    protected void sendCommand(String command) {
        mConnectionsClient.sendPayload(endpoitId, Payload.fromBytes(command.getBytes(UTF_8)));
    }


//    protected void sendCommand(String msg) {
//        mConnectionsClient.sendPayload(endpoitId, Payload.fromBytes(msg.getBytes(UTF_8)));
//    }

    //    Log message
    protected void logD(String msg) {
        Log.d(TAG, msg);
    }

    protected void logE(String msg) {
        Log.e(TAG, msg);
    }

    protected void logI(String msg) {
        Log.i(TAG, msg);
    }

    protected void logW(String msg) {
        Log.w(TAG, msg);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

