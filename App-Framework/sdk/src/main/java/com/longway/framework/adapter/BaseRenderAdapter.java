package com.longway.framework.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longway.framework.service.SystemServiceManager;

import java.util.List;

/**
 * 通用数据适配器
 *
 * @param <T>
 * @author longway
 */
public abstract class BaseRenderAdapter<T> extends android.widget.BaseAdapter {
    protected AdapterCollection<T> mAdapterCollection;
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    public BaseRenderAdapter(Context context, AdapterCollection<T> adapterCollection) {
        this.mContext = context;
        this.mAdapterCollection = adapterCollection;
        this.mLayoutInflater = (LayoutInflater) SystemServiceManager.getInstance().getService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int size() {
        return mAdapterCollection != null ? mAdapterCollection.size() : 0;
    }

    public boolean isEmpty() {
        return mAdapterCollection != null ? mAdapterCollection.isEmpty() : true;
    }

    public boolean remove(T data) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            boolean result = adapterCollection.remove(data);
            if (result) {
                notifyDataSetChanged();
            }
            return result;
        }
        return false;
    }

    public T remove(int pos) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            return (T) adapterCollection.remove(pos);
        }
        return null;
    }

    public Context getContext() {
        return mContext;
    }

    public BaseRenderAdapter() {
        // TODO Auto-generated constructor stub
    }

    public List<T> getData() {
        return mAdapterCollection != null ? mAdapterCollection.getData() : null;
    }

    public void setData(List<T> dataSource) {
        AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            adapterCollection.setData(dataSource);
        }
    }

    public void clear() {
        clear(false);
    }

    public void clear(boolean isNotify) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            if (adapterCollection.clear() && isNotify) {
                notifyDataSetChanged();
            }
        }
    }


    public boolean addData(List<T> dataSource) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            if (adapterCollection.addData(dataSource)) {
                notifyDataSetChanged();
            }
        }
        return false;
    }

    public boolean addData(T data) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            boolean result = adapterCollection.addData(data);
            if (result) {
                notifyDataSetChanged();
            }
        }
        return false;
    }

    public void putData(T data) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            adapterCollection.putData(data);
            notifyDataSetChanged();
        }
    }

    public void putData(T data, int index) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            adapterCollection.putData(data, index);
            notifyDataSetChanged();
        }
    }

    public boolean putData(List<T> dataSource) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            boolean result = adapterCollection.putData(dataSource);
            if (result) {
                notifyDataSetChanged();
            }
        }
        return false;
    }

    public boolean putData(List<T> dataSource, int index) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            boolean result = adapterCollection.putData(dataSource, index);
            if (result) {
                notifyDataSetChanged();
            }
        }
        return false;
    }

    public int lastIndexOf(T data) {
        final AdapterCollection adapterCollection = mAdapterCollection;
        if (adapterCollection != null) {
            return adapterCollection.lastIndexOf(data);
        }
        return -1;
    }

    @Override
    public int getCount() {
        return size();
    }

    @Override
    public T getItem(int arg0) {
        // TODO Auto-generated method stub
        return mAdapterCollection != null ? mAdapterCollection.getItem(arg0) : null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return getViewType(getItem(position));
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        int count = getBindViewTypeCount();
        return count <= 0 ? 1 : count;
    }

    @Override
    public View getView(int pos, View contentView, ViewGroup parent) {
        // TODO Auto-generated method stub
        T item = getItem(pos);
        ViewHold hold;
        if (ViewHold.hasViewHold(contentView)) {
            hold = (ViewHold) contentView.getTag();
        } else {
            hold = ViewHold.getViewHold(mLayoutInflater, contentView,
                    getLayoutId(item));
        }
        bindDataToView(hold, item, pos, parent);
        return hold.getContentView();
    }

    /**
     * bind data to view
     *
     * @param hold
     * @param item
     * @param pos
     * @param parent
     */
    public abstract void bindDataToView(ViewHold hold, T item, int pos, ViewGroup parent);

    /**
     * get layoutId
     *
     * @param item
     * @return
     */
    public abstract int getLayoutId(T item);

    /**
     * get viewType
     *
     * @param item
     * @return
     */
    public abstract int getViewType(T item);

    /**
     * get viewTypeCount
     *
     * @return
     */
    public abstract int getBindViewTypeCount();

}
