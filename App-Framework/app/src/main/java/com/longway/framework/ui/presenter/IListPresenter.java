package com.longway.framework.ui.presenter;

import java.util.HashMap;

/**
 * Created by longway on 16/4/26.
 * Email:longway1991117@sina.com
 */
public interface IListPresenter {
    void getList(String url, HashMap<String, String> params, HashMap<String, String> header, int actionType);
}
