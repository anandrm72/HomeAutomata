package com.example.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.home.Lib.BaseActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MainActivity extends BaseActivity /*implements NavigationHost*/ {
    private static boolean isDeviceConnected = false;
    private BottomAppBar bottomAppBar;
    private BottomSheetDialog bottomSheetDialog;
    private Button connectButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_circular);
        initBottomBar();
        initBottomSheet();
    }

    private void initBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.buttom_sheet);
        connectButton = bottomSheetDialog.findViewById(R.id.connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDiscover();
                progressBar.setVisibility(View.VISIBLE);
                dismissBottomSheet();
            }
        });
    }

    private void dismissBottomSheet() {
        bottomSheetDialog.dismiss();
    }

    private void initBottomBar() {
        bottomAppBar = findViewById(R.id.bottoAppBar);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    protected void onEndpointConnected(String endpoitId) {
        isDeviceConnected = true;
        progressBar.setVisibility(View.INVISIBLE);
        showToast("Connected");
    }

    @Override
    protected void onEndpointDisconnected(String s) {
        showToast("Disconnected");
        isDeviceConnected = false;
    }

    public void activitySwitcher(View view) {
        if (isDeviceConnected == true) {
            Intent intent = new Intent(this, DeviceCreateActivity.class);
            startActivity(intent);
        } else {
            showToast("Device not connected");
        }
    }
}
