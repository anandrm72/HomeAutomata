package com.example.home.Lib;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.home.Controller.DeviceManager;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String[] REQUIRED_PERMISSION = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private static final int REQUEST_CODE_REQUIRED_PERMISSION = 1;

    private static final String TAG = "com.iot.homeautomata";
    private static final String NAME = "Mobile device";
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static final String SERVICE_ID = "com.iot.homeautomata";
    private static String endpoitId;
    private final PayloadCallback mPayloadCallBack = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            receiveCommand(new String(payload.asBytes(), UTF_8));
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                logD("Message sent...");
            }
        }
    };

    private void receiveCommand(String msg) {
        JsonElement jsonElement = new Gson().fromJson(msg, JsonElement.class);
        JsonObject command = jsonElement.getAsJsonObject();
        switch (command.get("deviceState").getAsString()) {
            case "success":
                DeviceManager.getInstance().updateDevice(command);
                break;
        }
    }

    private ConnectionsClient mConnectionsClient;
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
            logD("Connecting to " + connectionInfo.getEndpointName());
            mConnectionsClient.acceptConnection(s, mPayloadCallBack);
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            logD(connectionResolution.getStatus().getStatusMessage());
            if (connectionResolution.getStatus().isSuccess()) {
                logD("Connected successful with " + s);
                endpoitId = s;
                connectedToEndpoint(endpoitId);

            } else {
                logD("Connection failed");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            logD("Disconnected from " + s);
            disconnectedFromEndpoint(s);
        }
    };
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull final String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            mConnectionsClient.requestConnection(NAME, s, mConnectionLifecycleCallback)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            logD("Connecting to " + s);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            logD(e.getMessage());
                        }
                    });
        }

        @Override
        public void onEndpointLost(@NonNull String s) {
            logD("Connection lost from " + s);
        }
    };

    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConnectionsClient = Nearby.getConnectionsClient(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!hasPermissions(this, getRequiredPermissions())) {
            if (Build.VERSION.SDK_INT < 23) {
                ActivityCompat.requestPermissions(this, getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSION);
            } else {
                requestPermissions(getRequiredPermissions(), REQUEST_CODE_REQUIRED_PERMISSION);
            }
        }
    }

    protected void startDiscover() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        mConnectionsClient.startDiscovery(SERVICE_ID, mEndpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        logD("Discovering...");
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

    protected void sendCommand(String deviceJsonString) {
        mConnectionsClient.sendPayload(endpoitId, Payload.fromBytes(deviceJsonString.getBytes(UTF_8)));
        showToast("sending message..");
    }

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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String[] getRequiredPermissions() {
        return REQUIRED_PERMISSION;
    }
}
