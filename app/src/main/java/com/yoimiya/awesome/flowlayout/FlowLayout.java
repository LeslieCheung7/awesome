package com.yoimiya.awesome.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    private static final String TAG = "FlowLayout";
    private static final int mHorizontalSpacing = 16;
    private static final int mVerticalSpacing = 8;

    private List<View> mLineViews;          // 记录某一行的views
    private List<List<View>> mAllLineViews; // 记录所有行的views，用于onLayout
    private List<Integer> mLineHeights;     // 记录每一行的行高，用于onLayout

    public FlowLayout(Context context) {
        super(context);
        initMeasureParams();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMeasureParams();
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMeasureParams();
    }

    // 不在绘制流程中创建对象，防止内存抖动
    private void initMeasureParams() {
        mLineViews = new ArrayList<>();
        mAllLineViews = new ArrayList<>();
        mLineHeights = new ArrayList<>();
    }

    // 构造函数只调用一次，会有脏数据，所以每次绘制前清楚数据
    private void clearMeasureParams() {
        mLineViews.clear();
        mAllLineViews.clear();
        mLineHeights.clear();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");

        clearMeasureParams();

        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);   // viewGroup解析的宽度
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec); // viewGroup解析的高度

        int parentNeededWidth = 0;  // measure过程中，子view要求的父viewGroup的宽
        int parentNeededHeight = 0; // measure过程中，子view要求的父viewGroup的高

        // 度量子view
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int lineWidthUsed = 0; // 该行已经使用的宽度
        int lineHeight = 0;    // 该行的高度

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LayoutParams childLP = childView.getLayoutParams();

            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    paddingLeft + paddingRight, childLP.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    paddingTop + paddingBottom, childLP.height);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            // 获取子view的宽高
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();

            // 需要换行
            if (childMeasuredWidth + lineWidthUsed > selfWidth) {
                mAllLineViews.add(mLineViews);
                mLineHeights.add(lineHeight);

                // 换行前记录当前行需要的宽高
                parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);
                parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;

                mLineViews = new ArrayList<>();
                lineWidthUsed = 0;
                lineHeight = 0;
            }
            mLineViews.add(childView);
            lineWidthUsed = lineWidthUsed + childMeasuredWidth + mHorizontalSpacing;
            lineHeight = Math.max(lineHeight, childMeasuredHeight);

            // 处理最后一行数据
            if (i == childCount - 1) {
                mAllLineViews.add(mLineViews);
                mLineHeights.add(lineHeight);

                parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpacing);
                parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpacing;
            }
        }

        // 作为一个viewGroup，它自己也是一个view，它的大小也需要根据它的父亲来提供的宽高来度量
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfWidth : parentNeededWidth;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selfHeight : parentNeededHeight;

        // 确定自己的大小
        setMeasuredDimension(realWidth, realHeight);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout");

        int curLeft = getPaddingLeft();
        int curTop = getPaddingTop();
        int lineCount = mAllLineViews.size();
        for (int i = 0; i < lineCount; i++) {
            List<View> lineViews = mAllLineViews.get(i);
            int lineHeight = mLineHeights.get(i);

            // 计算子view的位置
            for (int j = 0; j < lineViews.size(); j++) {
                View view = lineViews.get(j);
                int left = curLeft;

                // getMeasuredWidth()在onMeasure()以后有值，getWidth()在onLayout()以后有值
                int right = left + view.getMeasuredWidth();
                int bottom = curTop + view.getMeasuredHeight();
                view.layout(left, curTop, right, bottom);
                curLeft = right + mHorizontalSpacing;
            }

            // 换行时更新起始位置
            curLeft = getPaddingLeft();
            curTop = curTop + lineHeight + mVerticalSpacing;
        }
    }
}
