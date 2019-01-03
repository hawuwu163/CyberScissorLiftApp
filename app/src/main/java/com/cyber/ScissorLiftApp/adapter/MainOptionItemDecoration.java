package com.cyber.ScissorLiftApp.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
public class MainOptionItemDecoration extends RecyclerView.ItemDecoration {
    private int spacex;
    private int spacey;
    int spanCount;
    public MainOptionItemDecoration(int spacex,int spacey,int spanCount) {
        this.spacex = spacex;
        this.spacey = spacey;
        this.spanCount=spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        int pos = parent.getChildLayoutPosition(view)%spanCount;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        //先初始化一个Paint来简单指定一下Canvas的颜色，就黑的吧！
//        Paint paint = new Paint();
//        paint.setColor(parent.getContext().getResources().getColor(android.R.color.black));
//
//        //获得RecyclerView中总条目数量
//        int childCount = parent.getChildCount();
//
//        //遍历一下
//        for (int i = 0; i < childCount; i++) {
//            if (i == 0) {
//                //如果是第一个条目，那么我们就不画边框了
//                continue;
//            }
//            //获得子View，也就是一个条目的View，准备给他画上边框
//            View childView = parent.getChildAt(i);
//
//            //先获得子View的长宽，以及在屏幕上的位置，方便我们得到边框的具体坐标
//            float x = childView.getX();
//            float y = childView.getY();
//            int width = childView.getWidth();
//            int height = childView.getHeight();
//
//            //根据这些点画条目的四周的线
//            c.drawLine(x, y, x + width, y, paint);
//            c.drawLine(x, y, x, y + height, paint);
//            c.drawLine(x + width, y, x + width, y + height, paint);
//            c.drawLine(x, y + height, x + width, y + height, paint);
//
//            //当然了，这里大家肯定是要根据自己不同的设计稿进行画线，或者画一些其他的东西，都可以在这里搞，非常方便
//        }
        super.onDraw(c, parent, state);
    }

}
