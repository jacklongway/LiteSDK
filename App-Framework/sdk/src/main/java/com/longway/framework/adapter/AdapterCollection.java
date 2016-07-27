package com.longway.framework.adapter;

import java.util.List;

/**
 * Created by longway on 16/4/18.
 * Email:longway1991117@sina.com
 */
public interface AdapterCollection<T> {
    boolean remove(T data);

    T remove(int pos);

    boolean clear();

    boolean addData(List<T> dataSource);

    boolean addData(T data);

    void putData(T data);

    void putData(T data, int index);

    boolean putData(List<T> dataSource);

    boolean putData(List<T> dataSource, int index);

    int lastIndexOf(T data);

    int size();

    boolean isEmpty();

    List<T> getData();

    void setData(List<T> dataSource);

    T getItem(int position);
}
