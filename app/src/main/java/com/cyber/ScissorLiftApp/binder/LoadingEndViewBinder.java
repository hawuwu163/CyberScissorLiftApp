package com.cyber.ScissorLiftApp.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyber.ScissorLiftApp.R;
import com.cyber.ScissorLiftApp.module.bean.LoadingEndBean;

import me.drakeet.multitype.ItemViewBinder;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.binder
 * @date 2019/2/19 13:16
 * @Description:
 */

public class LoadingEndViewBinder extends ItemViewBinder<LoadingEndBean, LoadingEndViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected LoadingEndViewBinder.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_loading_end, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull LoadingEndBean item) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
