package com.cyber.ScissorLiftApp;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.ble.api.DataUtil;
import com.ble.ble.LeScanRecord;
import com.ble.utils.ToastUtil;
import com.cyber.ScissorLiftApp.adapter.LeDeviceListAdapter;
import com.cyber.ScissorLiftApp.util.BleProxy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScanFragment extends Fragment {
    private final static String TAG = "ScanFragment";
    private static final long SCAN_PERIOD = 5000;

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler = new Handler();
    private boolean mScanning;
    private BleProxy mBleProxy;

    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.ble_scanlist)
    ListView ble_scanlist;
    private Unbinder unbinder;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                if (state == BluetoothAdapter.STATE_ON) {
                    scanLeDevice(true);
                }
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBleProxy = BleProxy.getInstance();
        mLeDeviceListAdapter = new LeDeviceListAdapter(getActivity());
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getActivity().registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ble_scanlist_fragment, container, false);
        unbinder=ButterKnife.bind(this,view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        scanLeDevice(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void initView() {
        mRefreshLayout.setOnRefreshListener(() -> scanLeDevice(true));

        ble_scanlist.setAdapter(mLeDeviceListAdapter);
        ble_scanlist.setOnItemClickListener(mOnItemClickListener);
        ble_scanlist.setOnItemLongClickListener(mOnItemLongClickListener);
    }

    /**
     * 扫描BLE设备
     *
     * @param enable true开始扫描，false停止扫描
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            if (mBluetoothAdapter.isEnabled()) {
                if (mScanning) return;
                mScanning = true;
                mRefreshLayout.setRefreshing(true);
                mLeDeviceListAdapter.clear();
                mHandler.postDelayed(mScanRunnable, SCAN_PERIOD);
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                ToastUtil.show(getActivity(), "蓝牙未开启,请打开蓝牙");
            }
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mRefreshLayout.setRefreshing(false);
            mHandler.removeCallbacks(mScanRunnable);
            mScanning = false;
        }
    }

    private final Runnable mScanRunnable = () -> scanLeDevice(false);

    private final OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //单击连接设备
            scanLeDevice(false);
            BleDevice device = mLeDeviceListAdapter.getItem(position);
            mBleProxy.connect(device.getAddress(), false);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 233) {
            scanLeDevice(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final OnItemLongClickListener mOnItemLongClickListener = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            //长按查看广播数据
            BleDevice device = mLeDeviceListAdapter.getItem(position);
            showAdvDetailsDialog(device);
            return true;
        }
    };

    //显示广播数据
    private void showAdvDetailsDialog(BleDevice device) {
        LeScanRecord record = device.getLeScanRecord();

        TextView textView = new TextView(getActivity());
        textView.setPadding(32, 32, 32, 32);
        String sb = (device.getAddress() + "\n\n") +
                '[' + DataUtil.byteArrayToHex(record.getBytes()) + "]\n\n" +
                record.toString();
        textView.setText(sb);

        Dialog dialog = new Dialog(getActivity());
        dialog.setTitle(device.getName());
        dialog.setContentView(textView);
        dialog.show();
    }



    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            getActivity().runOnUiThread(() -> {
                mLeDeviceListAdapter.addDevice(new BleDevice(device.getName(), device.getAddress(), rssi, scanRecord));
                mLeDeviceListAdapter.notifyDataSetChanged();
            });
        }
    };


}