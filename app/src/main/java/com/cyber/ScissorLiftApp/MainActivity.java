package com.cyber.ScissorLiftApp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.api.DataUtil;
import com.ble.utils.TimeUtil;
import com.cyber.ScissorLiftApp.adapter.MainOptionAdapter;
import com.cyber.ScissorLiftApp.adapter.MainOptionItemDecoration;
import com.cyber.ScissorLiftApp.module.base.BaseActivity;
import com.cyber.ScissorLiftApp.module.base.BaseWithBleActivity;
import com.cyber.ScissorLiftApp.module.bean.MainOption;
import com.cyber.ScissorLiftApp.module.bluetooth.BleDevice;
import com.cyber.ScissorLiftApp.module.bluetooth.BleListActivity;
import com.cyber.ScissorLiftApp.module.parameter.ParameterIntegerBean;
import com.cyber.ScissorLiftApp.module.parameter.ParameterListActivity;
import com.cyber.ScissorLiftApp.util.LeProxy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseWithBleActivity{

    private static final String TAG = "MainActivity";
    //    private DynamicGridView gridView;
    @BindView(R.id.img_zhangdan)
    ImageView mImgZhangdan;
    @BindView(R.id.img_zhangdan_txt)
    TextView mImgZhangdanTxt;
    @BindView(R.id.toolbar_top)
    View toolbar_top;
    @BindView(R.id.toolbar_folding)
    View toolbar_folding;
    @BindView(R.id.jiahao)
    ImageView mJiahao;
    @BindView(R.id.tongxunlu)
    ImageView mTongxunlu;
    @BindView(R.id.img_shaomiao)
    ImageView mImgShaomiao;
    @BindView(R.id.img_fukuang)
    ImageView mImgFukuang;
    @BindView(R.id.img_search)
    ImageView mImgSearch;
    @BindView(R.id.img_zhaoxiang)
    ImageView mImgZhaoxiang;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.home_rv)
    RecyclerView home_rv;
    @BindView(R.id.ble_switch)
    SwitchCompat ble_switch;
    @BindView(R.id.ble_connected)
    RelativeLayout ble_connected;
    @BindView(R.id.ble_name)
    AppCompatTextView ble_name;
    @BindView(R.id.ps_mode_switch)
    SwitchCompat ps_mode_switch;
    private long exitTime = 0;
    private final BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);

            switch (intent.getAction()) {
                case LeProxy.ACTION_GATT_DISCONNECTED:// 断线
                    Toast.makeText(MainActivity.this,address+"断线了",Toast.LENGTH_LONG).show();
                    break;

                case LeProxy.ACTION_RSSI_AVAILABLE: {// 更新rssi
                }
                break;

                case LeProxy.ACTION_DATA_AVAILABLE:// 接收到从机数据
                    displayRxData(intent);
                    break;
            }
        }
    };
    private void displayRxData(Intent intent) {
        String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
        String uuid = intent.getStringExtra(LeProxy.EXTRA_UUID);
        byte[] data = intent.getByteArrayExtra(LeProxy.EXTRA_DATA);

        String dataStr = "timestamp: " + TimeUtil.timestamp("MM-dd HH:mm:ss.SSS") + '\n'
                + "uuid: " + uuid + '\n'
                + "length: " + (data == null ? 0 : data.length) + '\n';

            dataStr += "data: " + DataUtil.byteArrayToHex(data) + '\n';
        Toast.makeText(MainActivity.this,dataStr,Toast.LENGTH_LONG).show();


    }
    private IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LeProxy.ACTION_GATT_DISCONNECTED);
        filter.addAction(LeProxy.ACTION_RSSI_AVAILABLE);
        filter.addAction(LeProxy.ACTION_DATA_AVAILABLE);
        return filter;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalReceiver, makeFilter());
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalReceiver);

    }

    private void initView() {
        initToolbarAlpha();
        initRecyclerView();
        ble_switch.setChecked(BluetoothAdapter.getDefaultAdapter().isEnabled());
        ble_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                BluetoothAdapter.getDefaultAdapter().enable();
            } else {
                BluetoothAdapter.getDefaultAdapter().disable();
            }
        });
        ble_connected.setOnLongClickListener(v -> {
            BleListActivity.launch();
            return true;
        });
        ps_mode_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String address = LeProxy.getInstance().getConnectedDevice().getAddress();
            if (isChecked) {
                String gotoPS = "201801340003274A";
                byte[] data = DataUtil.hexToByteArray(gotoPS);
                LeProxy.getInstance().send(address, data);
                Log.e(TAG, gotoPS + " -> " + DataUtil.byteArrayToHex(data)+"["+address+"]");
            } else {
                String exitPS = "201801340002E68A";
                byte[] data = DataUtil.hexToByteArray(exitPS);
                LeProxy.getInstance().send(address, data);
                Log.e(TAG, exitPS + " -> " + DataUtil.byteArrayToHex(data)+"["+address+"]");
            }
        });
    }

    private void initRecyclerView(){
        final List<MainOption> list= new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add(new MainOption(R.drawable.ic_tmp_83,"速度参数"+i,i));
        }
        //实例化Adapter并且给RecyclerView设上
        final MainOptionAdapter adapter = new MainOptionAdapter(list);
        home_rv.setAdapter(adapter);

        // 如果我们想要一个GridView形式的RecyclerView，那么在LayoutManager上我们就要使用GridLayoutManager
        // 实例化一个GridLayoutManager，列数为3
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        //把我们自定义的ItemDecoration设置给RecyclerView
        home_rv.addItemDecoration(new MainOptionItemDecoration(10,3,4));
        //把LayoutManager设置给RecyclerView
        home_rv.setLayoutManager(layoutManager);
        home_rv.post(() -> adapter.setOnclick(new MainOptionAdapter.ClickInterface() {
            @Override
            public void onButtonClick(View view, int position) {
                onItemClick(view,position);
//                Toast.makeText(MainActivity.this, "button pos:"+position +"val:"+list.get(position).getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(View view, int position) {
                ParameterListActivity.launch(R.string.ECU_working_parameter2,"机器模式");
             }
        }));
    }


    /**
     * Toolbar的折叠特效
     *
     */
    private void initToolbarAlpha(){

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                System.out.println("verticalOffset = [" + verticalOffset + "]" + "{" + Math.abs(verticalOffset) + "}" + "{:" + appBarLayout.getTotalScrollRange() + "}");
                if (verticalOffset == 0) {
                    //完全展开
//                    Log.i(TAG, "onOffsetChanged: 完全展开");

                    toolbar_top.setVisibility(View.VISIBLE);
                    toolbar_folding.setVisibility(View.GONE);
                    setToolbarTopAlpha(255);
                } else if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    //appBarLayout.getTotalScrollRange() == 200
                    //完全折叠
//                    Log.i(TAG, "onOffsetChanged: 完全折叠");

                    toolbar_top.setVisibility(View.GONE);
                    toolbar_folding.setVisibility(View.VISIBLE);
                    setToolbarFoldingAlpha(255);
                } else {//0~200上滑下滑
                    if (toolbar_top.getVisibility() == View.VISIBLE) {
//                        //操作Toolbar1
                        int alpha = 300 - 150 - Math.abs(verticalOffset);
//                        Log.i("alpha0~200上滑下滑:", alpha + "");
                        setToolbarTopAlpha(alpha);

                    } else if (toolbar_folding.getVisibility() == View.VISIBLE) {
                        if (Math.abs(verticalOffset) > 0 && Math.abs(verticalOffset) < 200) {
                            toolbar_top.setVisibility(View.VISIBLE);
                            toolbar_folding.setVisibility(View.GONE);
                            setToolbarTopAlpha(255);
                        }
//                        //操作Toolbar2
                        int alpha = (int) (255 * (Math.abs(verticalOffset) / 100f));
                        setToolbarFoldingAlpha(alpha);
                    }
                }
            }
        });
    }

    /**
     * @param alpha
     */
    private void setToolbarTopAlpha(int alpha) {
        mImgZhangdan.getDrawable().setAlpha(alpha);
        mImgZhangdanTxt.setTextColor(Color.argb(alpha, 255, 255, 255));
        mTongxunlu.getDrawable().setAlpha(alpha);
        mJiahao.getDrawable().setAlpha(alpha);
    }
    private void setToolbarFoldingAlpha(int alpha) {
        mImgShaomiao.getDrawable().setAlpha(alpha);
        mImgFukuang.getDrawable().setAlpha(alpha);
        mImgSearch.getDrawable().setAlpha(alpha);
        mImgZhaoxiang.getDrawable().setAlpha(alpha);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - exitTime) < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.double_click_exit, Toast.LENGTH_SHORT).show();
            exitTime = currentTime;
        }
    }
}
