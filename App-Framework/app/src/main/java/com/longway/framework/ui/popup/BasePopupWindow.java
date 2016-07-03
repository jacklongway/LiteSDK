package com.longway.framework.ui.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import butterknife.ButterKnife;

/**
 * Created by longway on 15/12/21.
 */
public abstract class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context) {
        super(context);
        initContext(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initContext(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initContext(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initContext(context);
    }

    private void initContext(Context context) {
        //1. 获取内容视图
        View view = View.inflate(context, getLayoutId(), null);
        //2. 设置内容视图
        setContentView(view);
        //3. 设置视图宽
        setWidth(popupWindowWidth());
        //4. 设置视图高
        setHeight(popupWindowHeight());
        //5. 设置背景
        setBackgroundDrawable(popupWindowBackground());
        //6. 设置是否可以touch外部
        setOutsideTouchable(outsideTouchable());
        //7. 设置是否可以焦点
        setFocusable(focusable());
        //8. 设置是否可以touch
        setTouchable(touchable());
        //9. 注入视图
        ButterKnife.inject(this, view);
        //10. 初始化视图
        initView(view);
        //11. 注册事件 view 事件 通知事件等
        registerEvent();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    public abstract int getLayoutId();

    public abstract void initView(View contentView);

    public abstract void registerEvent();

    protected int popupWindowWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int popupWindowHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }


    protected boolean outsideTouchable() {
        return true;
    }

    protected boolean focusable() {
        return true;
    }

    protected boolean touchable() {
        return true;
    }

    protected Drawable popupWindowBackground() {
        return new BitmapDrawable();
    }

}
