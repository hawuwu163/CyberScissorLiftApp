package com.cyber.ScissorLiftApp.binder;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ble.ble.scan.LeScanner;
import com.cyber.ScissorLiftApp.InitApp;
import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.module.base.BaseListFragment;
import com.cyber.ScissorLiftApp.module.bluetooth.BleDevice;
import com.cyber.ScissorLiftApp.util.LeProxy;
import com.cyber.ScissorLiftApp.util.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.binder
 * @date 2019/2/27 11:27
 * @Description:
 */
public class BleDeviceViewBinder extends ItemViewBinder<BleDevice, BleDeviceViewBinder.ViewHolder> {
    private static final String TAG = "BleDeviceViewBinder";
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_ble_device, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BleDevice bleDevice) {
        String name = bleDevice.getName();
        if (TextUtils.isEmpty(name)) name = "未知设备";
        String address = bleDevice.getAddress();
        String rssi = ""+bleDevice.getRssi();
        holder.deviceName.setText(name);
        holder.deviceAddress.setText(address);
        holder.deviceRssi.setText(rssi);
        BluetoothDevice bluetoothDevice= LeProxy.getInstance().getConnectedDevice();
        String connectedAddress = null;
        boolean hasConnected = false;
        if (bluetoothDevice != null ) {
            connectedAddress = bluetoothDevice.getAddress();
            hasConnected = true;
        }
        Log.d(TAG, "onBindViewHolder: " + connectedAddress + " " + hasConnected + " " + address);

        if (hasConnected) {
            if (connectedAddress.equals(address)) {//已连接设备时,已连接的设备显示断开连接按钮
                setHolderVisibility(holder,false,true);
                holder.disconnect.setOnClickListener(v -> {
                    LeProxy.getInstance().disconnect(address);
                    setHolderVisibility(holder,true,false);
                });
            } else {
                setHolderVisibility(holder, false, false);//未连接的设备连接和断开连接的按钮都不显示
            }
        } else {
            setHolderVisibility(holder,true,false);//未连接设备时所有设备均显示连接的按钮
            holder.connect.setOnClickListener(v -> {
                LeScanner.stopScan();
                LeProxy.getInstance().connect(address, false);//点击连接按钮后更新按钮为断开连接
                setHolderVisibility(holder,false,true);
            });
        }
    }

    void setHolderVisibility(@NonNull ViewHolder holder,boolean idle,boolean connected) {
        holder.layout_idle.setVisibility(idle? View.VISIBLE:View.GONE);
        holder.layout_connected.setVisibility(connected? View.VISIBLE:View.GONE);
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.device_name) TextView deviceName;
        @BindView(R.id.device_address) TextView deviceAddress;
        @BindView(R.id.device_rssi) TextView deviceRssi;
        @BindView(R.id.btn_connect) Button connect;
        @BindView(R.id.btn_disconnect) Button disconnect;
        @BindView(R.id.layout_idle) LinearLayout layout_idle;
        @BindView(R.id.layout_connected) LinearLayout layout_connected;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
