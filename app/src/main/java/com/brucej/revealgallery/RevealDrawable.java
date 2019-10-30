package com.brucej.revealgallery;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//渐变Drawable
public class RevealDrawable extends Drawable {
    private String TAG = "RevealDrawable--";
    private Drawable selectedDrawable;
    private Drawable unSelectedDrawable;

    public RevealDrawable(Drawable unSelectedDrawable, Drawable selectedDrawable) {
        this.selectedDrawable = selectedDrawable;
        this.unSelectedDrawable = unSelectedDrawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float ratio = 0;
        boolean isLeft = true;
        int level = getLevel();
        if (level == 0) {
            //显示未选择
            unSelectedDrawable.draw(canvas);
            return;
        } else if (level == 5000) {
            //显示选择
            selectedDrawable.draw(canvas);
            return;
        } else if (level < 5000) {
            //左图片 部分显示
            isLeft = true;
            ratio = level / 100f;
        } else {//大于5000
            //右图片 部分显示
            isLeft = false;
            ratio = (level - 5000) / 100f;
        }
        if (isLeft) {
            Rect bounds = getBounds();//获取 当前drawable的绘制区域
            //定义一个矩形区域，用来存储
            Rect outRect = new Rect();
            //应用布局区域到 指定的区域对象;
            Gravity.apply(Gravity.LEFT, (int) (bounds.width() * ratio), bounds.height(),
                    bounds, outRect);
            canvas.save();
            canvas.clipRect(outRect);//(画布裁剪一个区域并相交)
            unSelectedDrawable.draw(canvas);
            canvas.restore();

            Rect outRect1 = new Rect();
            Gravity.apply(Gravity.RIGHT, (int) (bounds.width() * (1f - ratio)), bounds.height(),
                    bounds, outRect1);
            canvas.clipRect(outRect1);
            selectedDrawable.draw(canvas);
        } else {
            Rect bounds = getBounds();//获取 当前drawable的绘制区域
            //定义一个矩形区域，用来存储
            Rect outRect = new Rect();
            //应用布局区域到 指定的区域对象;
            Gravity.apply(Gravity.LEFT, (int) (bounds.width() * ratio), bounds.height(),
                    bounds, outRect);
            canvas.save();
            canvas.clipRect(outRect);//(画布裁剪一个区域并相交)
            selectedDrawable.draw(canvas);
            canvas.restore();

            Rect outRect1 = new Rect();
            Gravity.apply(Gravity.RIGHT, (int) (bounds.width() * (1f - ratio)), bounds.height(),
                    bounds, outRect1);
            canvas.clipRect(outRect1);
            unSelectedDrawable.draw(canvas);
        }
    }

    //drawable level发生变化时调用
    @Override
    protected boolean onLevelChange(int level) {
        Log.i(TAG, "onLevelChange level=" + level);
        invalidateSelf();//drawable重新绘制（实际回调的方式通知View,View调用View.invalidate）
        return super.onLevelChange(level);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        //设置drawable的宽高;bounds是绘制边界
        selectedDrawable.setBounds(bounds);
        unSelectedDrawable.setBounds(bounds);
    }

    //获取 真实的宽度
    @Override
    public int getIntrinsicWidth() {
        //return super.getIntrinsicWidth();
        return Math.max(selectedDrawable.getIntrinsicWidth(),
                unSelectedDrawable.getIntrinsicWidth());
    }

    //获取 真实的高度
    @Override
    public int getIntrinsicHeight() {
        //return super.getIntrinsicHeight();
        return Math.max(selectedDrawable.getIntrinsicHeight(),
                unSelectedDrawable.getIntrinsicHeight());
    }


    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
