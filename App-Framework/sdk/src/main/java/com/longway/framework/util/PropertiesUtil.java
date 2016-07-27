package com.longway.framework.util;

import com.facebook.stetho.common.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 配置文件读写类
 */
public class PropertiesUtil {
    private static final String TAG = "PropertiesUtil";

    /**
     * 读取配置。注意，如果读取失败，会返回null。
     * @return 返回加载的配置
     */
    public static Properties loadConfig(final String path) {
        if (path == null || path.length() <= 0) {
            return null;
        }
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            properties.load(in);
        } catch (Throwable e) {
            LogUtil.w(TAG, e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch(Throwable e){
                LogUtil.w(TAG, e);
            }
        }
        return properties;
    }
    // 保存配置文件
    public static boolean saveConfig(final String path, final Properties properties) {
        FileOutputStream out = null;
        try {
            File file = new File(path);
            if(!file.exists() && !file.createNewFile()) {
                return false;
            }
            out = new FileOutputStream(path);
            properties.store(out, "");
        } catch (Throwable e) {
            LogUtil.w(TAG, e);
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch(Throwable e){
                LogUtil.w(TAG, e);
            }
        }
        return true;
    }
}
