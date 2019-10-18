package com.example.home.Connection;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.example.home.MainFragment;
import com.example.home.R;
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

import static java.nio.charset.StandardCharsets.UTF_8;

public class Discoverer {

    private static final String TAG = "com.iot.homeautomata";
    private static final String NAME = "Mobile device";
    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static final String SERVICE_ID = "com.iot.homeautomata";
    private static String endpoitId;
    private final PayloadCallback mPayloadCallBack = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {

        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate payloadTransferUpdate) {
            if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                Toast.makeText(mContext, "Switch status updated", Toast.LENGTH_LONG).show();
            }
        }
    };
    private Context mContext;
    private ConnectionsClient mConnectionsClient;
    private final ConnectionLifecycleCallback mConnectionLifecycleCallback = new ConnectionLifecycleCallback() {
        @Override
        public void onConnectionInitiated(@NonNull final String s, @NonNull ConnectionInfo connectionInfo) {
            Log.d(TAG, "Connecting to " + connectionInfo.getEndpointName());
            new AlertDialog.Builder(mContext)
                    .setTitle("Accept connection to " + connectionInfo.getEndpointName())
                    .setMessage("Do you like to connect with " + connectionInfo.getEndpointName())
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mConnectionsClient.acceptConnection(s, mPayloadCallBack);
                        }
                    })
                    .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mConnectionsClient.rejectConnection(s);
                        }
                    })
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_developer_board_icon_24dp)
                    .show();
//            mConnectionsClient.acceptConnection(s, mPayloadCallBack);
        }

        @Override
        public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
            Log.d(TAG, connectionResolution.getStatus().getStatusMessage());
            if (connectionResolution.getStatus().isSuccess()) {
                Log.d(TAG, "Connected successfully with " + s);
                mConnectionsClient.stopDiscovery();
                endpoitId = s;

//                updating ui components
                MainFragment.getInstance().updateDeviceState(true);

            } else {
                Log.d(TAG, "Connection failed");
            }
        }

        @Override
        public void onDisconnected(@NonNull String s) {
            Log.d(TAG, "Disconnected from " + s);
        }
    };
    private final EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
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
    private MainFragment mFragmentActivity;

    public Discoverer(Context context, MainFragment activity) {
        this.mContext = context;
        this.mFragmentActivity = activity;
        this.mConnectionsClient = Nearby.getConnectionsClient(mContext);
    }

    public void startDiscover() {
        mConnectionsClient = Nearby.getConnectionsClient(mContext);
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

    //    updating switch state
    public void updateDeviceState(String state) {
        mConnectionsClient.sendPayload(endpoitId, Payload.fromBytes(state.getBytes(UTF_8)));
    }
}
