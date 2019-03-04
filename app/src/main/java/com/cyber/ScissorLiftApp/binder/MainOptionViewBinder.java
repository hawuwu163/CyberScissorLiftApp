package com.cyber.ScissorLiftApp.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.module.mainOption.MainOption;
import com.cyber.ScissorLiftApp.module.parameter.ParameterListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.binder
 * @date 2019/3/1 21:09
 * @Description:
 */
public class MainOptionViewBinder extends ItemViewBinder<MainOption, MainOptionViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_main_option, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull MainOption mainOption) {
        holder.mainOptionImage.setImageResource(mainOption.getImageId());
        holder.mainOptionName.setText(mainOption.getName());
        holder.itemView.setOnClickListener(v -> ParameterListActivity.launch(R.string.ECU_working_parameter2,"机器模式"));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.main_option_image)
        ImageView mainOptionImage;
        @BindView(R.id.main_option_name)
        TextView mainOptionName;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
