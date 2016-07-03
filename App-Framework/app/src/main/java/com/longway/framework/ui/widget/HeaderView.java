package com.longway.framework.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.longway.elabels.R;

/**
 * Created by longway on 16/5/5.
 * Email:longway1991117@sina.com
 */
public class HeaderView extends FrameLayout {
    private RelativeLayout mHeaderContainer, mHeaderLeftContainer, mHeaderTitleContainer, mHeaderRightContainer;
    private ImageView mBack, mAction;
    private TextView mTitle;
    private OnClickListener mBackListener, mActionListener;

    public HeaderView(Context context) {
        super(context);
        init(context);
    }
    private void init(Context context) {
        View root = View.inflate(context, R.layout.header_layout, this);
        mHeaderContainer = (RelativeLayout) root.findViewById(R.id.framework_header_container);
        mHeaderLeftContainer = (RelativeLayout) root.findViewById(R.id.framework_left_container);
        mHeaderTitleContainer = (RelativeLayout) root.findViewById(R.id.framework_title_container);
        mHeaderRightContainer = (RelativeLayout) root.findViewById(R.id.framework_right_container);
        mBack = (ImageView) root.findViewById(R.id.framework_header_back);
        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBackListener != null) {
                    mBackListener.onClick(v);
                }
            }
        });
        mAction = (ImageView) root.findViewById(R.id.framework_header_action);
        mAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionListener != null) {
                    mActionListener.onClick(v);
                }
            }
        });
        mTitle = (TextView) root.findViewById(R.id.framework_title);
    }

    public RelativeLayout getHeaderContainer() {
        return mHeaderContainer;
    }

    public RelativeLayout getHeaderLeftContainer() {
        return mHeaderLeftContainer;
    }

    public RelativeLayout getHeaderTitleContainer() {
        return mHeaderTitleContainer;
    }

    public RelativeLayout getHeaderRightContainer() {
        return mHeaderRightContainer;
    }

    public void setHeaderBackground(int color) {
        mHeaderContainer.setBackgroundColor(color);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setHeaderBackground(Drawable d) {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
            mHeaderContainer.setBackground(d);
        } else {
            mHeaderContainer.setBackgroundDrawable(d);
        }
    }

    public void setHeaderBackgroundResId(int resId) {
        mHeaderContainer.setBackgroundResource(resId);
    }

    public void setHeaderBackground(Bitmap b) {
        setHeaderBackground(new BitmapDrawable(getResources(), b));
    }

    public void setBackIcon(int resId) {
        mBack.setImageResource(resId);
    }

    public void setBackIcon(Drawable d) {
        mBack.setImageDrawable(d);
    }

    public void setBackVisible(boolean visible) {
        mBack.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public void setBackListener(OnClickListener ocl) {
        mBackListener = ocl;
    }

    private RelativeLayout.LayoutParams generateParams(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null || !(params instanceof RelativeLayout.LayoutParams)) {
            params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        return (RelativeLayout.LayoutParams) params;
    }

    private void setCustomView(View view, ViewGroup parent, int rule) {
        if (view == null) {
            return;
        }
        if (parent == null) {
            return;
        }
        ViewGroup p = (ViewGroup) view.getParent();
        if (p != null) {
            p.removeView(view);
        }
        parent.removeAllViews();
        RelativeLayout.LayoutParams rp = generateParams(view);
        rp.addRule(rule);
        parent.addView(view, rp);

    }

    public void setCustomLeftView(View view) {
        setCustomView(view, mHeaderLeftContainer, RelativeLayout.CENTER_VERTICAL | RelativeLayout.ALIGN_PARENT_LEFT);
    }

    public void setCustomLeftView(int layoutId) {
        try {
            setCustomLeftView(View.inflate(getContext(), layoutId, null));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void setCustomTitleContent(View view) {
        setCustomView(view, mHeaderTitleContainer, RelativeLayout.CENTER_IN_PARENT);
    }

    public void setCustomTitleContent(int layoutId) {
        try {
            setCustomTitleContent(View.inflate(getContext(), layoutId, null));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    public void setTitle(int resId) {
        mTitle.setText(resId);
    }

    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    public void setTitleColor(ColorStateList list) {
        mTitle.setTextColor(list);
    }

    public void setTitleTextSize(float size) {
        mTitle.setTextSize(size);
    }

    public void setTitleTextSize(int unit, float size) {
        mTitle.setTextSize(unit, size);
    }

    public void setActionIcon(int resId) {
        mAction.setImageResource(resId);
    }

    public void setActionIcon(Drawable d) {
        mAction.setImageDrawable(d);
    }

    public void setActionVisible(boolean visible) {
        mAction.setVisibility(visible ? VISIBLE : INVISIBLE);
    }

    public void setActionListener(OnClickListener ocl) {
        this.mActionListener = ocl;
    }

    public void setActionCustomView(View view) {
        setCustomView(view, mHeaderRightContainer, RelativeLayout.CENTER_VERTICAL | RelativeLayout.ALIGN_PARENT_RIGHT);
    }

    public void setActionCustomView(int layoutId) {
        try {
            setActionCustomView(View.inflate(getContext(), layoutId, null));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }
}
