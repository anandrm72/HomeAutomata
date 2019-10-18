package com.example.home;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.home.Connection.Discoverer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static final String[] REQUIRED_PERMISSIONS =
            new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            };
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    private MaterialButton discoverButton;
    private static MainFragment instance;
    private MaterialCardView deviceSwitch;
    private Discoverer discoverer;
    private boolean isDeviceConnected = false;

    public MainFragment() {
    }

    public static MainFragment getInstance() {
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        instance = this;

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initUiModules(view);
        discoverer = new Discoverer(getContext(), this);
        return view;
    }

    private void initUiModules(View view) {
        deviceSwitch = view.findViewById(R.id.deviceSwitch);
        deviceSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deviceSwitch.getCardBackgroundColor().getDefaultColor() == -1) {
                    deviceSwitch.setCardBackgroundColor(Color.parseColor("#ffb74d"));
                    deviceSwitch.setCardElevation(16);
                    discoverer.updateDeviceState("On");

                } else {

                    deviceSwitch.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    deviceSwitch.setCardElevation(1);
                    discoverer.updateDeviceState("Off");

                }
            }
        });

        discoverButton = view.findViewById(R.id.discover);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasPermissions(getContext(), REQUIRED_PERMISSIONS)) {
                    requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
                }
                discoverer.startDiscover();
            }
        });
        checkDeviceState();
    }

    private void checkDeviceState() {
        if (isDeviceConnected) {
            discoverButton.setEnabled(false);
            deviceSwitch.setEnabled(true);
        } else {
            discoverButton.setEnabled(true);
            deviceSwitch.setEnabled(false);
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void updateDeviceState(boolean status) {
        isDeviceConnected = status;
        checkDeviceState();
    }

//    private void connect() {
//        if (!hasPermissions(getContext(), REQUIRED_PERMISSIONS)) {
//            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
//        }
//        discoverer.startDiscover();

//    }

}
