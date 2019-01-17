package com.cyber.ScissorLiftApp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyber.ScissorLiftApp.BleDevice;
import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.util.BleProxy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.adapter
 * @date 2019/1/16 15:02
 * @Description:
 */
public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BleDevice> mBleDevices;
    private Context context;
    private BleProxy bleProxy = BleProxy.getInstance();
    public LeDeviceListAdapter(Context context) {
        mBleDevices = new ArrayList<>();
        this.context=context;
    }

    public void addDevice(BleDevice device) {
        if (!mBleDevices.contains(device)) {
            mBleDevices.add(device);
        }
    }

    public void clear() {
        mBleDevices.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBleDevices.size();
    }

    @Override
    public BleDevice getItem(int i) {
        return mBleDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        //view设置
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.ble_item_list_device, null);
            viewHolder = new ViewHolder(convertView);
            viewHolder.connect.setVisibility(View.VISIBLE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //内容设置
        BleDevice device = mBleDevices.get(pos);
        String deviceName = device.getName();
        if (!TextUtils.isEmpty(deviceName))
            viewHolder.deviceName.setText(deviceName);
        else
            viewHolder.deviceName.setText("未知设备");
        viewHolder.deviceAddress.setText(device.getAddress());
        viewHolder.deviceRssi.setText("rssi: " + device.getRssi() + "dbm");
        //点击事件
        viewHolder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleProxy.connect(device.getAddress(), true);

            }
        });
        viewHolder.disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleProxy.disconnect(device.getAddress());
                for (int i = 0; i < mBleDevices.size(); i++) {
                    if(i==pos) {
                        viewHolder.disconnect.setVisibility(View.VISIBLE);
                        viewHolder.connect.setVisibility(View.INVISIBLE);
                    }else{
                        viewHolder.disconnect.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return convertView;
    }

    public  static class ViewHolder {

        @BindView(R.id.device_name) TextView deviceName;
        @BindView(R.id.device_address) TextView deviceAddress;
        @BindView(R.id.device_rssi) TextView deviceRssi;
        @BindView(R.id.btn_connect) TextView connect;
        @BindView(R.id.btn_disconnect) Button disconnect;
        @BindView(R.id.layout_idle) LinearLayout layout_idle;
        @BindView(R.id.layout_connected) LinearLayout layout_connected;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnDeviceClickListener {
        void onConnect(BleDevice bleDevice);

        void onDisConnect(BleDevice bleDevice);

        //void onDetail(BleDevice bleDevice);
    }

    private OnDeviceClickListener mListener;

}