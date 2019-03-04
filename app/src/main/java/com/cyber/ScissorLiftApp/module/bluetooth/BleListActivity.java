package com.cyber.ScissorLiftApp.module.bluetooth;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ble.ble.BleService;
import com.cyber.ScissorLiftApp.InitApp;
import com.cyber.ScissorLiftApp.module.base.BaseWithBleActivity;
import com.cyber.ScissorLiftApp.module.parameter.ParameterListFragment;
import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.module.base.BaseActivity;
import com.cyber.ScissorLiftApp.util.LeProxy;

public class BleListActivity extends BaseWithBleActivity {
    private static final String TAG = "BleListActivity";
    ParameterListFragment instance;
    private long exitTime = 0;
    public static void launch() {
        InitApp.AppContext
                .startActivity(new Intent(InitApp.AppContext, BleListActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,
                        BleListFragment.newInstance())
                .commit();

    }

    @Override
    public void onReceiveBleBroadcast(Context context, @NonNull Intent intent) {}
}
