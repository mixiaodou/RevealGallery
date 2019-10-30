package com.brucej.revealgallery;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import java.util.List;


public class GalleryScrollView extends HorizontalScrollView
        implements View.OnTouchListener, View.OnScrollChangeListener {
    private String TAG = "GalleryScrollView--";
    private LinearLayout linearLayout;

    public GalleryScrollView(Context context) {
        this(context, null);
    }

    public GalleryScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
        );
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        setOnTouchListener(this);
        setOnScrollChangeListener(this);
    }

    private List<RevealDrawable> drawables;

    public void setDrawables(List<RevealDrawable> drawables) {
        this.drawables = drawables;
        for (int i = 0; i < drawables.size(); i++) {
            RevealDrawable drawable = drawables.get(i);
            ImageView imgView = new ImageView(getContext());
            imgView.setLayoutParams(
                    new ViewGroup.LayoutParams(MarginLayoutParams.WRAP_CONTENT,
                            MarginLayoutParams.WRAP_CONTENT));
            imgView.setImageDrawable(drawable);
            linearLayout.addView(imgView, i);
            if (i == 0) {
                //第一个默认全部显示
                imgView.setImageLevel(5000);
            }
        }
        addView(linearLayout);
    }

    private int childWidth;
    private int childHeight;
    private int centerX;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (linearLayout.getChildCount() == 0) {
            return;
        }
        View child = linearLayout.getChildAt(0);
        childWidth = child.getWidth();
        childHeight = child.getHeight();
        //
        centerX = getWidth() / 2 - childWidth / 2;
        linearLayout.setPadding(centerX, 0, centerX, 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                reveal();
                break;
        }
        return false;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY,
                               int oldScrollX, int oldScrollY) {
        reveal();
    }

    //
    private void reveal() {
        int scrollX = getScrollX();
        //左图片的下标
        int leftIndex = scrollX / childWidth;
        float leftRatio = ((float) scrollX % childWidth) / childWidth;
        leftRatio = (float) Math.round(leftRatio * 100) / 100;
        Log.i(TAG, "leftIndex=" + leftIndex + ",leftRatio=" + leftRatio);
        int childCount = linearLayout.getChildCount();
        if (childCount == 0) {
            return;
        }
        int rightIndex = -1;
        if (leftIndex < childCount - 1) {
            rightIndex = leftIndex + 1;
        }
        //右图片 滚动距离占比,和左图片一致;
        float rightRatio = leftRatio;
        for (int i = 0; i < childCount; i++) {
            ImageView v = (ImageView) linearLayout.getChildAt(i);
            if (i == leftIndex) {
                if (leftRatio == 0f) {
                    v.setImageLevel(5000);
                } else {
                    v.setImageLevel((int) (leftRatio * 100));
                }
            } else if (i == rightIndex) {
                if (rightRatio == 0f) {
                    v.setImageLevel(0);
                } else {
                    v.setImageLevel((int) (5000 + (rightRatio * 100)));
                }
            } else {
                v.setImageLevel(0);
            }
        }
    }


}
