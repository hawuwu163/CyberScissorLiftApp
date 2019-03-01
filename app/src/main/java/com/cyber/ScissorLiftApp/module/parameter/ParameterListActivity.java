package com.cyber.ScissorLiftApp.module.parameter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.cyber.ScissorLiftApp.InitApp;
import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.module.base.BaseActivity;
import com.cyber.ScissorLiftApp.module.base.BaseWithBleActivity;

public class ParameterListActivity extends BaseWithBleActivity {

    private static final String TAG = "ParameterListActivity";
    private static final String JSON_LIST = "jsonList";
    private static final String TITLE = "title";
    ParameterListFragment instance;
    private long exitTime = 0;
    public static void launch(int jsonList,String title) {
        InitApp.AppContext.startActivity(new Intent(InitApp.AppContext, ParameterListActivity.class)
                .putExtra(JSON_LIST, jsonList)
                .putExtra(TITLE,title)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        Intent intent = getIntent();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,
                        instance = ParameterListFragment.newInstance(intent.getIntExtra(JSON_LIST,-1),intent.getStringExtra(TITLE)))
                .commit();
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (instance.isEditStatus){
            if ((currentTime - exitTime) < 2000) {
                instance.exitEditMode();
            } else {
                Toast.makeText(this, R.string.double_click_exit_edit_mode, Toast.LENGTH_SHORT).show();
                exitTime = currentTime;
            }
        }else super.onBackPressed();
    }
}
