package com.cyber.ScissorLiftApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.cyber.ScissorLiftApp.module.base.BaseWithBleActivity;
import com.cyber.ScissorLiftApp.util.LeProxy;

public class MyReceiver extends BroadcastReceiver {
    private BaseWithBleActivity activity;
    public MyReceiver(BaseWithBleActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, @NonNull Intent intent) {
        activity.onReceiveBleBroadcast(context,intent);
    }

    public void sendAndReceiveData(String address, byte[] data) {
        LeProxy.getInstance().send(address, data);
    }

}
