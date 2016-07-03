package com.longway.framework.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by longway on 16/4/18.
 * Email:longway1991117@sina.com
 */
public class AdapterCollectionImpl<T> implements AdapterCollection<T> {
    private List<T> mDataSource;

    public AdapterCollectionImpl(List<T> dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public boolean remove(T data) {
        if (data == null) {
            return false;
        }
        List<T> dataSource = mDataSource;
        if (dataSourceEmpty(dataSource)) {
            return false;
        }
        boolean result = dataSource.remove(data);
        return result;
    }

    /***
     * 数据元为空
     *
     * @param dataSource
     * @return
     */
    protected boolean dataSourceEmpty(List<T> dataSource) {
        return dataSource == null || dataSource.isEmpty();
    }


    @Override
    public T remove(int pos) {
        List<T> dataSource = mDataSource;
        if (dataSourceEmpty(dataSource)) {
            return null;
        }
        if (pos < 0 || pos >= dataSource.size()) {
            return null;
        }
        T data = dataSource.remove(pos);
        return data;
    }

    @Override
    public boolean clear() {
        List<T> dataSource = mDataSource;
        if (!dataSourceEmpty(dataSource)) {
            dataSource.clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean addData(List<T> dataSource) {
        if (dataSourceEmpty(dataSource)) {
            return false;
        }
        boolean result = ensureNotNull().addAll(dataSource);
        return result;
    }

    /**
     * 保证数据源存在
     *
     * @return
     */
    private List<T> ensureNotNull() {
        List<T> source = mDataSource;
        if (source == null) {
            source = new ArrayList<T>();
            mDataSource = source;
        }
        return source;
    }

    @Override
    public boolean addData(T data) {
        if (data == null) {
            return false;
        }
        if (ensureNotNull().contains(data)) {
            return false;
        }
        boolean result = ensureNotNull().add(data);
        return result;
    }

    @Override
    public void putData(T data) {
        if (data == null) {
            return;
        }
        ensureNotNull().add(0, data);
    }

    @Override
    public void putData(T data, int index) {
        if (data == null) {
            return;
        }
        if (!indexValidate(index)) {
            return;
        }
        ensureNotNull().add(index, data);
    }

    private boolean indexValidate(int index) {
        return index >= 0 && index < ensureNotNull().size();
    }

    @Override
    public boolean putData(List<T> dataSource) {
        if (dataSourceEmpty(dataSource)) {
            return false;
        }
        boolean result = ensureNotNull().addAll(0, dataSource);
        return result;
    }

    @Override
    public boolean putData(List<T> dataSource, int index) {
        if (dataSourceEmpty(dataSource)) {
            return false;
        }
        if (!indexValidate(index)) {
            return false;
        }
        boolean result = ensureNotNull().addAll(index, dataSource);
        return result;
    }

    @Override
    public int lastIndexOf(T data) {
        List<T> dataSource = mDataSource;
        if (dataSource != null && dataSource.contains(data)) {
            return mDataSource.lastIndexOf(data);
        } else {
            return -1;
        }
    }

    @Override
    public int size() {
        return ensureNotNull().size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public List<T> getData() {
        return ensureNotNull();
    }

    @Override
    public void setData(List<T> dataSource) {
        if (dataSource == null) {
            return;
        }
        this.mDataSource = dataSource;
    }

    @Override
    public T getItem(int position) {
        if (!indexValidate(position)) {
            return null;
        }
        return mDataSource != null ? mDataSource.get(position) : null;
    }
}
