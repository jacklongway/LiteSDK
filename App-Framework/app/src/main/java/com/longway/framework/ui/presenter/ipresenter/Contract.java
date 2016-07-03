package com.longway.framework.ui.presenter.ipresenter;

import com.longway.framework.core.network.base.Request;
import com.longway.framework.ui.presenter.INetView;

import java.lang.reflect.Type;

/**
 * Created by longway on 16/6/1.
 * Email:longway1991117@sina.com
 */

public interface Contract {
    interface IList extends INetView<IListPresenter> {
        Type getType(String tag);
    }

    interface IListPresenter {
        void getList(Request request);
    }
}
