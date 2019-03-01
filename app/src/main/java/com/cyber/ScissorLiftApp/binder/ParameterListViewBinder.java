package com.cyber.ScissorLiftApp.binder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.contrarywind.interfaces.IPickerViewData;
import com.cyber.ScissorLiftApp.module.parameter.ParameterListFragment;
import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.module.parameter.ParameterIntegerBean;
import com.cyber.ScissorLiftApp.util.ErrorActionUtil;
import com.cyber.ScissorLiftApp.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.binder
 * @date 2019/2/19 10:34
 * @Description:
 */
public class ParameterListViewBinder extends ItemViewBinder<ParameterIntegerBean,ParameterListViewBinder.ViewHolder> {
    private static final String TAG = "ParameterListViewBinder";
    List<ParameterIntegerBean> list;

    public ParameterListViewBinder(List<?> list) {
        this.list = (List<ParameterIntegerBean>)list;
        Log.d(TAG, "ParameterListViewBinder: list:"+list.size());
    }

    @NonNull
    @Override
    protected ParameterListViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_parameter, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(@NonNull ParameterListViewBinder.ViewHolder holder, @NonNull ParameterIntegerBean item) {
        final Context context = holder.itemView.getContext();
        holder.setIsRecyclable(false);
        String description = item.getDescription();
        Integer val = item.getVal();
        String measurementUnit = item.getMeasurementUnit();
        Integer type = item.getType();
        Boolean enabled = item.getEnabled();
        try {
            holder.parameterName.setText(item.getName());
            if (description.isEmpty()) {
                holder.parameterDescription.setVisibility(View.GONE);
            } else {
                holder.parameterDescription.setText(description);
            }
            Log.d(TAG, "onBindViewHolder: "+item.toString());
            //开关量
            if (type == 3) {
                holder.parameterSwitch.setEnabled(enabled);
                holder.parameterSwitch.setChecked(val == 1);
                holder.parameterSwitch.setVisibility(View.VISIBLE);
                holder.parameterMeasurementUnit.setVisibility(View.GONE);
                holder.parameterVal.setVisibility(View.GONE);
            } else {
                holder.parameterSwitch.setVisibility(View.GONE);
                holder.parameterVal.setEnabled(type!=0 && enabled);
                holder.parameterVal.setVisibility(View.VISIBLE);
                holder.parameterVal.setText("" + val);
                if (measurementUnit.isEmpty()) {
                    holder.parameterMeasurementUnit.setVisibility(View.GONE);
                } else {
                    holder.parameterMeasurementUnit.setVisibility(View.VISIBLE);
                    holder.parameterMeasurementUnit.setText(measurementUnit);
                    holder.parameterMeasurementUnit.setEnabled(enabled);
                }
            }
        } catch (Exception e) {
            ErrorActionUtil.print(e);
        }
        if (enabled) {
            if (type != 3) {//非开关量
                holder.itemView.setOnClickListener(v -> {
                    if (holder.pvOptions==null)
                        holder.pvOptions = buildOptionsPickerView(holder, item);
                    holder.pvOptions.show();
                });
            } else {//开关量
                holder.parameterSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> RxBus.getInstance().post(ParameterListFragment.TAG, holder.getAdapterPosition() + " "+(isChecked? "1":"0")));

            }
        }
    }

    private OptionsPickerView buildOptionsPickerView(@NonNull ParameterListViewBinder.ViewHolder holder, ParameterIntegerBean item) {
        ArrayList<ItemOption> limit = new ArrayList<>();
        setOptionsLimit(item,limit);
        OptionsPickerView pvOptions = new OptionsPickerBuilder(holder.itemView.getContext(), (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = limit.get(options1).getPickerViewText();
            holder.parameterVal.setText(tx);
            RxBus.getInstance().post(ParameterListFragment.TAG, holder.getAdapterPosition() + " "+tx);
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("设置值")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.WHITE)//标题文字颜色
                .setSubmitColor(Color.WHITE)//确定按钮文字颜色
                .setCancelColor(Color.WHITE)//取消按钮文字颜色
                .setTitleBgColor(R.color.colorPrimary)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .setLabels(item.getMeasurementUnit().isEmpty()?"":item.getMeasurementUnit(), "", "")//设置选择的三级单位
                .setCyclic(true, false, false)//循环与否
                .setSelectOptions(limit.indexOf(new ItemOption(item.getVal())), 1, 1)  //设置默认选中项
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvOptions.setPicker(limit, null, null);//添加数据源
        Dialog mDialog = pvOptions.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            pvOptions.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
//                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.CENTER);//改成Bottom,底部显示
            }
        }
        return pvOptions;
    }
    private void setOptionsLimit(ParameterIntegerBean item, List<ItemOption> list) {
        int type = item.getType();
        List<Integer> limit = item.getLimit();
        // 枚举
        if (type == 1) {
            for(Integer i:limit) list.add(new ItemOption(i));
        } else if (type == 2) { //范围
            for (int i = 0, sz = limit.size(); i < sz; i += 2) {
                int beg = limit.get(i), end = limit.get(i + 1);
                for (int j = beg; j <= end; j++) list.add(new ItemOption(j));
            }
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.parameter_name) AppCompatTextView parameterName;
        @BindView(R.id.parameter_description) AppCompatTextView parameterDescription;
        AppCompatTextView parameterVal;
        @BindView(R.id.parameter_measurementUnit) AppCompatTextView parameterMeasurementUnit;
        SwitchCompat parameterSwitch;
        @BindView(R.id.parameter_total) LinearLayout parameter_total;
        OptionsPickerView pvOptions;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parameterVal = itemView.findViewById(R.id.parameter_val);
            parameterSwitch = itemView.findViewById(R.id.parameter_switch);
        }
    }

    public class ItemOption implements IPickerViewData {
        Integer val;

        ItemOption(Integer val) {
            this.val = val;
        }
        @Override
        public String getPickerViewText() {
            return val.toString();
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof ItemOption)
            return val.intValue() == ((ItemOption)o).val.intValue();
            return false;
        }

        @Override
        public int hashCode() {
            return val.hashCode();
        }
    }
}
