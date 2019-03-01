package com.cyber.ScissorLiftApp.module.parameter;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.cyber.ScissorLiftApp.module.protocol.IProtocol;
import com.cyber.ScissorLiftApp.util.LeProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.module
 * @date 2019/1/18 15:55
 * @Description:
 */
public class ParameterListPresenter<T extends IProtocol> implements IParameterList.Presenter {
    private static final String TAG = "ParameterListPresenter";
    private IParameterList.View view;
    private String jsonList;
    private int offset = 0;
    private List<ParameterIntegerBean> parameterIntegerBeanList ;
    T parser;
    LeProxy leProxy = LeProxy.getInstance();
    public ParameterListPresenter(IParameterList.View view) {
        this.view = view;
    }


    /**
     * @param arg
     * arg[0] json,用于解析出参数的固定不变的文本值
     * arg[1] 0:只读模式 1:编辑模式
     * arg[2] 编辑模式下,要修改的第arg[2]个参数
     * arg[3] 编辑模式下,第arg[2]个参数的值修改为arg[3]
     */
    @Override
    public void doLoadData(String jsonList,String mode,String ...arg) {
        if ("0".equals(mode)) {
            try {
                if(this.jsonList==null){
                    this.jsonList = jsonList;
                    setParameterContent();
                }
                doBleConnectWork();
                for (ParameterIntegerBean bean:parameterIntegerBeanList) bean.setEnabled(false);
                ((Runnable) () -> {

                }).run();
                doSetAdapter(parameterIntegerBeanList);
            } catch (Exception e) {
//            int[] tmp = {1, 2, 3, 4};
//            list.add(new ParameterIntegerBean("机器模式","机器模式的描述",0,"单位",3, tmp));
//            list.add(new ParameterIntegerBean("机器模式","机器模式的描述",1,"单位",2, tmp));
//            list.add(new ParameterIntegerBean("机器模式","机器模式的描述",2,"单位",1, tmp));
//            list.add(new ParameterIntegerBean("机器模式","机器模式的描述",3,"单位",0, tmp));
                e.printStackTrace();
            }
        } else if ("1".equals(mode)) {
            List<ParameterIntegerBean> list = new ArrayList<>(parameterIntegerBeanList);
            if (arg.length == 2) {
                String rec = "";
                for(String s : arg) rec += s;
                Log.d(TAG, "doLoadData: "+ rec);
                int[] args = new int[arg.length];
                for (int i = 0; i < arg.length; i++) {
                    args[i] = Integer.parseInt(arg[i]);
                }
                for (int i = 0; i < arg.length; i+=2) {
                    list.get(args[i]).setVal(args[i+1]);
                }
            }
            //3.修改完成后重新设置adapter
            for (ParameterIntegerBean bean:list) if( bean.getType() != 0 )bean.setEnabled(true);
            doSetAdapter(list);
        }
    }
    @Override
    public boolean isEditable() {
        if( parameterIntegerBeanList == null || parameterIntegerBeanList.size() ==0 ) return false;
        for (ParameterIntegerBean i : parameterIntegerBeanList) {
            if(i.getType() != 0) return true;
        }
        return false;
    }
    boolean doBleConnectWork() {
        //TODO 蓝牙通讯找设备值
//        List<Byte> vals =
        BluetoothDevice bleDevice = leProxy.getConnectedDevice();
        if (bleDevice == null) {
            return false;
        }
        return LeProxy.getInstance().send(bleDevice.getAddress(), parser.getRequireCode());
    }
    void printData() {
        Log.d(TAG, "printData: "+parameterIntegerBeanList);
    }
    /** 从json中获取参数列set表内容
     */
    void setParameterContent() {
        Log.d(TAG, "setParameterContent: "+jsonList);
        parameterIntegerBeanList = JSONObject.parseArray(jsonList, ParameterIntegerBean.class);
    }
    @Override
    public void doSetAdapter(List<ParameterIntegerBean> list) {
        parameterIntegerBeanList= list;
        view.onSetAdapter(parameterIntegerBeanList);
        view.onHideLoading();
    }

    @Override
    public void doShowNoMore() {
        view.onShowNoMore();
        view.onHideLoading();
    }

    @Override
    public void doRefresh() {
        doLoadData(jsonList,"0");
    }

    @Override
    public void doShowError(int val) {
        view.onHideLoading();
        view.onError(val);
    }
}
