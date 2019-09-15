package com.example.home.Connection;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

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

public class Discoverer {

    private static final String TAG = "com.iot.homeautomata";
    private static final String NAME = "Mobile device";
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static final String SERVICE_ID = "com.iot.homeautomata";
    private static String endpoitId;
    public final PayloadCallback mPayloadCallBack = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {

        }
    };
    private Context mContext;
    private ConnectionsClient mConnectionsClient;
    public final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
            mConnectionsClient.acceptConnection(s, mPayloadCallBack);
            Log.d(TAG, "Connecting to " + connectionInfo.getEndpointName());
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            Log.d(TAG, connectionResolution.getStatus().getStatusMessage());
            if (connectionResolution.getStatus().isSuccess()) {
                Log.d(TAG, "Connected successfully with " + s);
                mConnectionsClient.stopDiscovery();
                endpoitId = s;
            } else {
                Log.d(TAG, "Connection failed");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            Log.d(TAG, "Disconnected from " + s);
        }
    };
    public final EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            mConnectionsClient.requestConnection(NAME, s, mConnectionLifecycleCallback)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Connecting...");
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

        @Override
        public void onEndpointLost(@NonNull String s) {
            Log.d(TAG, "Disconnected from endpoint");
        }
    };

    public Discoverer(Context mContext) {
        this.mContext = mContext;
        mConnectionsClient = Nearby.getConnectionsClient(mContext);
    }

    public void startDiscover() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        mConnectionsClient.startDiscovery(SERVICE_ID, mEndpointDiscoveryCallback, discoveryOptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Discovering...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                });
    }
}
