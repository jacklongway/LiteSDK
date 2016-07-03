package com.longway.framework.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by longway on 16/4/16.
 * Email:longway1991117@sina.com
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHold> {
    private List<T> mData;
    private LayoutInflater mLayoutInflater;

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this.mData = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = mLayoutInflater.inflate(getLayoutId(viewType), null);
        RecyclerHold recyclerHold = createViewHold(contentView, viewType);
        recyclerHold.initView(contentView);
        return recyclerHold;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(getItem(position));
    }


    @Override
    public void onBindViewHolder(RecyclerHold holder, int position) {
        holder.bind(getItem(position), position);
    }

    private T getItem(int position) {
        return mData != null && position >= 0 && position < mData.size() ? mData.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    protected abstract RecyclerHold createViewHold(View view, int viewType);

    protected abstract int getLayoutId(int viewType);

    protected abstract int getViewType(T item);
}
