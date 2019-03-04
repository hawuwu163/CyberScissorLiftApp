package com.cyber.ScissorLiftApp;

import android.content.IntentFilter;

import com.cyber.ScissorLiftApp.util.LeProxy;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp
 * @date 2019/3/3 21:05
 * @Description:
 */
public class Filter {
    public static final IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LeProxy.ACTION_GATT_DISCONNECTED);
        filter.addAction(LeProxy.ACTION_RSSI_AVAILABLE);
        filter.addAction(LeProxy.ACTION_DATA_AVAILABLE);
        filter.addAction(LeProxy.ACTION_WRITE_SUCCESS);
        filter.addAction(LeProxy.ACTION_WRITE_FAILED);
        return filter;
    }
}
