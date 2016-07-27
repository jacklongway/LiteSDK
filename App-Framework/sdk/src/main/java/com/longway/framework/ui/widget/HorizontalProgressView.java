package com.longway.framework.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.longway.elabels.R;
import com.longway.framework.util.DensityUtils;

/*********************************
 * Created by longway on 16/5/12 上午10:29.
 * packageName:com.longway.framework.ui.widget
 * projectName:trunk
 * Email:longway1991117@sina.com
 ********************************/
public class HorizontalProgressView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mProgressViewHeight = 3;// dp
    private int maxProgress;
    private int mCurrentProgress;
    private int mColor;

    public HorizontalProgressView(Context context) {
        super(context);
        initView(context, null, 0, 0);
    }

    public HorizontalProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }


    public HorizontalProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HorizontalProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalProgressView);
        maxProgress = array.getInt(R.styleable.HorizontalProgressView_maxProgress, 100);
        mColor = array.getColor(R.styleable.HorizontalProgressView_progressColor, Color.YELLOW);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        array.recycle();
    }

    private Looper mUIThread = Looper.myLooper();

    private void dirtyView() {
        if (mUIThread != Looper.myLooper()) {
            postInvalidate();
        } else {
            invalidate();
        }
    }

    public void setColor(int color) {
        if (mColor != color) {
            mColor = color;
            mPaint.setColor(color);
            dirtyView();
        }
    }

    public void setCurrentProgress(int progress) {
        if (progress > maxProgress) {
            return;
        }
        if (mCurrentProgress != progress) {
            mCurrentProgress = progress;
            dirtyView();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = DensityUtils.dp2px(getContext(), mProgressViewHeight);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float r = (mCurrentProgress * 1.0f / maxProgress) * getWidth();
        canvas.drawRect(0, 0, r, getHeight(), mPaint);
    }
}
