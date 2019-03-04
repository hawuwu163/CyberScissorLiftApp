package com.cyber.ScissorLiftApp.module.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ble.ble.BleService;
import com.cyber.ScissorLiftApp.util.LeProxy;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module.base
 * @date 2019/3/1 15:10
 * @Description:
 */
public abstract class BaseWithBleActivity extends BaseActivity {
    private static final String TAG = "BaseWithBleActivity";
    protected LeProxy LeProxyInstance = LeProxy.getInstance();
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w(TAG, "onServiceDisconnected()");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LeProxy.getInstance().setBleService(service);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, BleService.class), mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    public abstract void onReceiveBleBroadcast(Context context, @NonNull Intent intent);


}
