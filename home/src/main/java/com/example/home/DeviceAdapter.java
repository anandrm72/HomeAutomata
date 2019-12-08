package com.example.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home.Model.Device;

import java.util.ArrayList;

class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private ArrayList<Device> devices;
    private Context context;

    public DeviceAdapter(Context context, ArrayList<Device> devices) {
        this.devices = devices;
        this.context = context;
    }

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.device_bulb, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder viewHolder, int position) {
        Device getCurrentSport = devices.get(position);
        viewHolder.bindTo(getCurrentSport);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView deviceName;
        private TextView devicecategory;
        private TextView deviceInputPort;
        private TextView deviceOutputPort;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name);
            devicecategory = itemView.findViewById(R.id.device_category);
            deviceInputPort = itemView.findViewById(R.id.device_inputPort);
            deviceOutputPort = itemView.findViewById(R.id.device_outPutPort);
        }

        public void bindTo(Device getCurrentDevice) {
            deviceName.setText(getCurrentDevice.getDeviceName());
            devicecategory.setText(getCurrentDevice.getDeviceCategory());
            deviceInputPort.setText(getCurrentDevice.getDeviceInputPort());
            deviceOutputPort.setText(getCurrentDevice.getDeviceOutputPort());
        }

        @Override
        public void onClick(View view) {

        }
    }
}