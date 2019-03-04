package com.cyber.ScissorLiftApp.module.bluetooth;


import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ble.ble.BleService;
import com.ble.ble.scan.LeScanner;
import com.ble.utils.ToastUtil;
import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.adapter.Register;
import com.cyber.ScissorLiftApp.module.base.BaseListFragment;
import com.cyber.ScissorLiftApp.module.bean.LoadingBean;
import com.cyber.ScissorLiftApp.module.bean.LoadingEndBean;
import com.cyber.ScissorLiftApp.util.DiffCallback;
import com.cyber.ScissorLiftApp.util.LeProxy;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BleListFragment extends BaseListFragment<IBleList.Presenter> implements IBleList.View {

    private static final String TAG = "BleListFragment";

    public static BleListFragment newInstance() {
        return new BleListFragment();
    }

    @Override
    protected int attachLayoutId() {
        return R.layout.fragment_list_toolbar;
    }

    @Override
    protected void initData() {
        onLoadData();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        initToolBar(toolbar, true, "选择蓝牙");
        toolbar.setOnClickListener(view1 -> recyclerView.smoothScrollToPosition(0));
//        toolbar.setBackgroundColor(SettingUtil.getInstance().getColor());
        adapter = new MultiTypeAdapter(oldItems);
        Register.registerBleListItem(adapter);
        recyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mLocalReceiver, makeFilter());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            //申请打开手机蓝牙，requestCode为LeScanner.REQUEST_ENABLE_BT
            LeScanner.requestEnableBluetooth(getActivity());
        }
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            if (Build.VERSION.SDK_INT >= 23) {
                //TODO Android6.0开始，扫描是个麻烦事，得检测APP有没有定位权限，手机定位有没有开启
                if (!LeScanner.hasFineLocationPermission(getActivity())) {
                    showAlertDialog(
                            R.string.scan_tips_no_location_permission,
                            R.string.to_grant_permission,
                            (dialog, which) -> LeScanner.startAppDetailsActivity(getActivity()));
                } else if (!LeScanner.isLocationEnabled(getActivity())) {
                    showAlertDialog(
                            R.string.scan_tips_location_service_disabled,
                            R.string.to_enable,
                            (dialog, which) -> LeScanner.requestEnableLocation(getActivity()));
                } else {
                    onLoadData();
                }
            } else {
                onLoadData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LeScanner.stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mLocalReceiver);
    }

    @Override
    public void onRefresh() {
        presenter.doRefresh();
    }

    @Override
    public void onLoadData() {
        onShowLoading();
        presenter.doLoadData();
    }

    @Override
    public void onSetAdapter(final List<?> list) {
        Items newItems = new Items(list);
//        newItems.add(new LoadingEndBean());
        //4.adapter更新recyclerview
        DiffCallback.create(oldItems, newItems, adapter);
        /*
        presenter如果是本地改变了数据后,由于是浅拷贝,olditems和newitems数据集在持有的引用指向同一个位置,所以在此处不能更新items
        所以需要调用notifyDataSetChanged()
         */
//        adapter.notifyDataSetChanged();
        oldItems.clear();
        oldItems.addAll(newItems);

    }

    @Override
    public void setPresenter(IBleList.Presenter presenter) {
        if (null == presenter) {
            this.presenter = new BleListPresenter(this);
        }
    }

    @Override
    public void fetchData() {

    }


    private final BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
            switch (intent.getAction()) {
                case LeProxy.ACTION_GATT_CONNECTED:
                    ToastUtil.show(mContext, getString(R.string.scan_connected) + " " + address);
                    break;
                case LeProxy.ACTION_GATT_DISCONNECTED:
                    ToastUtil.show(mContext, getString(R.string.scan_disconnected) + " " + address);
                    break;
                case LeProxy.ACTION_CONNECT_ERROR:
                    ToastUtil.show(mContext, getString(R.string.scan_connection_error) + " " + address);
                    break;
                case LeProxy.ACTION_CONNECT_TIMEOUT:
                    ToastUtil.show(mContext, getString(R.string.scan_connect_timeout) + " " + address);
                    break;
                case LeProxy.ACTION_GATT_SERVICES_DISCOVERED:
                    ToastUtil.show(mContext, "Services discovered: " + address);
                    break;
            }
        }
    };

    private IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LeProxy.ACTION_GATT_CONNECTED);
        filter.addAction(LeProxy.ACTION_GATT_DISCONNECTED);
        filter.addAction(LeProxy.ACTION_CONNECT_ERROR);
        filter.addAction(LeProxy.ACTION_CONNECT_TIMEOUT);
        filter.addAction(LeProxy.ACTION_GATT_SERVICES_DISCOVERED);
        return filter;
    }

    private void showAlertDialog(int msgId, int okBtnTextId, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setMessage(msgId)
                .setPositiveButton(okBtnTextId, okListener)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> getActivity().finish()).show();

    }
}
