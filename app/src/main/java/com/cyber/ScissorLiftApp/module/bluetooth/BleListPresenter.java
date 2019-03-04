package com.cyber.ScissorLiftApp.module.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ble.ble.scan.LeScanResult;
import com.ble.ble.scan.LeScanner;
import com.ble.ble.scan.OnLeScanListener;
import com.cyber.ScissorLiftApp.util.LeProxy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.bluetooth
 * @date 2019/2/27 10:57
 * @Description:
 */
public class BleListPresenter implements IBleList.Presenter {
    private static final String TAG = "BleListPresenter";

    private static final int MSG_SCAN_STARTED = 1;
    private static final int MSG_SCAN_DEVICE = 2;
    private static final int MSG_SCAN_STOPPED = 3;

    private List<BleDevice> bleDeviceList = new ArrayList<>();
    private IBleList.View view;
    private Handler mHandler;

    BleListPresenter(IBleList.View view) {
        this.view = view;
        mHandler = new MyHandler(new WeakReference<>(this));
    }

    @Override
    public void doLoadData() {

        bleDeviceList.clear();
        BluetoothDevice device = LeProxy.getInstance().getConnectedDevice();
        if (device != null) {
            bleDeviceList.add(new BleDevice(device.getName(), device.getAddress()));
        }
        Log.d(TAG, "doLoadData: start,connected device:"+device);
        doSetAdaper();
        LeScanner.startScan(new OnLeScanListener() {
            @Override
            public void onScanStart() {
                Log.d(TAG, "onScanStart: ");
            }
            @Override
            public void onLeScan(LeScanResult leScanResult) {
                BluetoothDevice device = leScanResult.getDevice();
                Message msg = new Message();
                msg.what = MSG_SCAN_DEVICE;
                msg.obj = new BleDevice(
                        device.getName(),
                        device.getAddress(),
                        leScanResult.getRssi(),
                        leScanResult.getLeScanRecord().getBytes()
                );
                mHandler.sendMessage(msg);
            }
            @Override
            public void onScanFailed(int i) {
                switch (i) {
                    case ScanCallback.SCAN_FAILED_ALREADY_STARTED:
                        Log.d(TAG, "onScanFailed: " + "     * Fails to start scan as BLE scan with the same settings is already started by the app.");
                        break;
                    case ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                        Log.d(TAG, "onScanFailed: " + "Fails to start scan as app cannot be registered.");
                        break;
                    case ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED:
                        Log.d(TAG, "onScanFailed: " + "Fails to start scan due an internal error");
                        break;
                    case ScanCallback.SCAN_FAILED_INTERNAL_ERROR:
                        Log.d(TAG, "onScanFailed: " + "Fails to start power optimized scan as this feature is not supported.");
                        break;
                    default:
                        Log.d(TAG, "onScanFailed: Unknown Error");
                        break;
                }
                doShowError(i);
                view.onHideLoading();
            }
            @Override
            public void onScanStop() {
                Log.d(TAG, "onScanStop: bleDeivce size:"+bleDeviceList.size());
                view.onHideLoading();
            }
        });
//        for (int i =0; i<10 ; i ++) {
//            bleDeviceList.add(new BleDevice("name" + i, "address" + i));
//        }
//        doSetAdaper(bleDeviceList);
    }

    @Override
    public void doSetAdaper() {
        view.onSetAdapter(bleDeviceList);
    }

    @Override
    public void doShowNoMore() {
        view.onHideLoading();
        view.onShowNoMore();
    }

    @Override
    public void doRefresh() {
        doLoadData();
    }

    @Override
    public void doShowError(int val) {

    }

    @Override
    public void addDevice(BleDevice bleDevice) {
        if(!bleDeviceList.contains(bleDevice))
        bleDeviceList.add(bleDevice);
    }

    private static class MyHandler extends Handler {
        WeakReference<IBleList.Presenter> reference;

        MyHandler(WeakReference<IBleList.Presenter> reference) {
            this.reference = reference;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            IBleList.Presenter presenter =  reference.get();
            if (presenter != null) {
                switch (msg.what) {
                    case MSG_SCAN_STARTED:
                        break;

                    case MSG_SCAN_DEVICE:
                        presenter.addDevice((BleDevice)msg.obj);
                        presenter.doSetAdaper();
                        break;

                    case MSG_SCAN_STOPPED:
                        break;
                }
            }
        }
    }
}
