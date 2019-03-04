package com.cyber.ScissorLiftApp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ble.api.DataUtil;
import com.ble.utils.TimeUtil;
import com.cyber.ScissorLiftApp.adapter.Register;
import com.cyber.ScissorLiftApp.module.base.BaseWithBleActivity;
import com.cyber.ScissorLiftApp.module.mainOption.MainOption;
import com.cyber.ScissorLiftApp.module.bluetooth.BleListActivity;
import com.cyber.ScissorLiftApp.util.LeProxy;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.cyber.ScissorLiftApp.Filter.makeFilter;

public class MainActivity extends BaseWithBleActivity{

    private static final String TAG = "MainActivity";
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
    RecyclerView recyclerView;
    @BindView(R.id.ble_switch)
    SwitchCompat ble_switch;
    @BindView(R.id.ble_connected)
    RelativeLayout ble_connected;
    @BindView(R.id.ble_name)
    AppCompatTextView ble_name;
    @BindView(R.id.ps_mode_switch)
    SwitchCompat ps_mode_switch;
    private long exitTime = 0;
    private MultiTypeAdapter adapter;
    private Items items = new Items();
    private ProgressHandler progressHandler = new ProgressHandler();
    private BroadcastReceiver mLocalReceiver;

    @Override
    public void onReceiveBleBroadcast(Context context, @NonNull Intent intent) {
        String address = intent.getStringExtra(LeProxy.EXTRA_ADDRESS);
        switch (intent.getAction()) {
            case LeProxy.ACTION_GATT_DISCONNECTED:// 断线
                Toast.makeText(MainActivity.this,address+"断线了",Toast.LENGTH_LONG).show();
                break;
            case LeProxy.ACTION_RSSI_AVAILABLE: // 更新rssi

                break;
            case LeProxy.ACTION_DATA_AVAILABLE:// 接收到从机数据
                displayRxData(intent);
                break;
            case LeProxy.ACTION_WRITE_SUCCESS:

                break;
            case LeProxy.ACTION_WRITE_FAILED:

                break;
        }
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mLocalReceiver = new MyReceiver(this);
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
            String address = LeProxyInstance.getConnectedDevice().getAddress();
            String code = isChecked?  "201801340003274A":"201801340002E68A";
            byte[] data = DataUtil.hexToByteArray(code);
            LeProxyInstance.send(address, data);
            Log.e(TAG, "ps_mode_switch: "+isChecked + code +" -> " + DataUtil.byteArrayToHex(data)+"["+address+"]");
            View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_text,null);
            TextView status = findViewById(R.id.dialog_text);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .create();
            progressHandler.setDialog(alertDialog);
            Observable
                    .create((ObservableOnSubscribe<Integer>) emitter -> {
                        //TODO 蓝牙发送相关的事情
                        int res = 1;
                        emitter.onNext(1);
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Integer integer) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        });
    }
    private static final class ProgressHandler extends Handler {

        private WeakReference<DialogInterface> Dialog = null;

        public WeakReference<DialogInterface> getDialog() {
            return Dialog;
        }

        public void setDialog(DialogInterface dialogInterface) {
            this.Dialog = new WeakReference<>(dialogInterface);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {//联系上了，msg.what肯定是DISMISS
                case 1:
                    ((DialogInterface.OnDismissListener) msg.obj).onDismiss(Dialog.get());//msg的obj就是Listener呗，调用其中的onDismiss方法。
                    break;
            }
        }
    }
    private void initRecyclerView(){
        for(int i=0;i<9;i++){
            items.add(new MainOption(R.drawable.ic_tmp_83,"速度参数"+i,i));
        }
        adapter = new MultiTypeAdapter(items);
        Register.registerMainOptionGridItem(adapter);
        recyclerView.setAdapter(adapter);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Toolbar的折叠特效
     */
    private void initToolbarAlpha(){
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
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
        });
    }

    /**
     * @param alpha don't care about it
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
