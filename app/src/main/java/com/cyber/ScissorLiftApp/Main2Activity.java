package com.cyber.ScissorLiftApp;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cyber.ScissorLiftApp.module.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends BaseActivity {
    private static final String TAG = "Main2Activity";
    @BindView(R.id.my_rv)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        List<myOption> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add(new myOption("this is " + i + "th option", "val is " + i,i>15? 0:1));
        }
        MyAdapter baseMultiItemQuickAdapter = new MyAdapter(list);
        baseMultiItemQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemClick: ");
                Toast.makeText(Main2Activity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(baseMultiItemQuickAdapter);

    }

    class MyAdapter extends BaseMultiItemQuickAdapter<myOption, BaseViewHolder> {
        public MyAdapter(List<myOption> data) {
            super(data);
            addItemType(myOption.TEXT, R.layout.my_item_list_option);
            addItemType(myOption.SWITCH, R.layout.my_item_list_option2);
        }

        @Override
        protected void convert(BaseViewHolder helper, myOption item) {
            switch (helper.getItemViewType()) {
                case myOption.TEXT:
                    helper.setText(R.id.my_text1, item.getStr1());
                    helper.setText(R.id.my_text2, item.getStr2());
                    break;
                case myOption.SWITCH:
                    helper.setText(R.id.my_text1, item.getStr1());
                    ((SwitchCompat)helper.getView(R.id.my_switch1)).setChecked(new Random().nextBoolean());
                    break;
            }
        }
    }
    class myOption implements MultiItemEntity {
        public static final int TEXT = 0;
        public static final int SWITCH = 1;
        String str1, str2;
        int type;

        @Override
        public int getItemType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public myOption(String str1, String str2, int type) {
            this.str1 = str1;
            this.str2 = str2;
            this.type = type;
            Log.d(TAG, "myOption: constructor"+this.toString());
        }

        public void setStr1(String str1) {
            this.str1 = str1;
        }

        public void setStr2(String str2) {
            this.str2 = str2;
        }

        public String getStr1() {
            return str1;
        }

        public String getStr2() {
            return str2;
        }

        @Override
        public String toString() {
            return "myOption{" +
                    "str1='" + str1 + '\'' +
                    ", str2='" + str2 + '\'' +
                    ", type=" + type +
                    '}';
        }
    }
}
