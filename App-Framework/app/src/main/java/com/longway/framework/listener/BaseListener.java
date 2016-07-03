package com.longway.framework.listener;

/**
 * Created by longway on 15/12/12.
 * 基本回调对象
 */
public interface BaseListener<T, E, F> {
    void onAction(T caller, E params, F obj);
}
