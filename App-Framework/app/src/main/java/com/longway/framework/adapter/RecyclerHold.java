package com.longway.framework.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by longway on 16/4/16.
 * Email:longway1991117@sina.com
 */
public abstract class RecyclerHold<T> extends RecyclerView.ViewHolder {
    public RecyclerHold(View itemView) {
        super(itemView);
    }

    protected abstract void initView(View itemView);

    protected abstract void bind(T item, int position);
}
