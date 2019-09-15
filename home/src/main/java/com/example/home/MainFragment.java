package com.example.home;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.Connection.Discoverer;
import com.google.android.material.button.MaterialButton;


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
    Discoverer discoverer = new Discoverer(getContext());
    private MaterialButton discoverButton;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        discoverButton = view.findViewById(R.id.discover);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });
        return view;
    }

    private void connect() {
        if (!hasPermissions(getContext(), REQUIRED_PERMISSIONS)) {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_REQUIRED_PERMISSIONS);
        }
        discoverer.startDiscover();
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

}
