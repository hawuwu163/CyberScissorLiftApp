package com.cyber.ScissorLiftApp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.module.bean.MainOption;

import java.util.List;



public class MainOptionAdapter extends RecyclerView.Adapter<MainOptionAdapter.ViewHolder> {

    //数据源
    private List<MainOption> mMainOptionList;
    //接口
    private ClickInterface clickInterface;

    public MainOptionAdapter(List<MainOption> mMainOptionList) {
        this.mMainOptionList = mMainOptionList;
    }

    //--------------------点击事件-------------------------------------------//
    public void setOnclick(ClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    //回调接口
    public interface ClickInterface {
        void onButtonClick(View view, int position);

        void onItemClick(View view, int position);
    }
    //--------------------点击事件-------------------------------------------//

    //自定义内部类 ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        //        ImageView MainOptionImage;
        TextView MainOptionName;
        ImageButton button;
        LinearLayout itemDetail;

        ViewHolder(View itemView) {
            super(itemView);
//            MainOptionImage = (ImageView) itemView.findViewById(R.id.MainOption_image);
            MainOptionName = itemView.findViewById(R.id.main_option_name);
            button = itemView.findViewById(R.id.main_option_button);
            itemDetail = itemView.findViewById(R.id.main_item_option_detail);
        }
    }

    //用于创建ViewHolder实例，并加载布局
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext()).inflate(R.layout.main_item_grid_option, parent, false);
        return new ViewHolder(view);
    }

    //对RecyclerView子项数据进行赋值
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MainOption mainOption = mMainOptionList.get(position);
//        holder.MainOptionImage.setImageResource(MainOption.getImageId());
        holder.button.setImageResource(mainOption.getImageId());
        holder.MainOptionName.setText(mainOption.getName());

        //--------------------点击事件-------------------------------------------//
        //Button点击事件
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickInterface != null) {
                    clickInterface.onButtonClick(v, position);
                }
            }
        });
        //item点击事件
        holder.itemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickInterface != null) {
                    clickInterface.onItemClick(v, position);
                }
            }
        });
        //--------------------点击事件-------------------------------------------//
    }



    //RecyclerView子项的个数
    @Override
    public int getItemCount() {
        return mMainOptionList.size();
    }
}
