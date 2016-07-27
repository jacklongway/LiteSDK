package com.longway.framework.util;

import com.facebook.stetho.common.LogUtil;
import com.longway.framework.AndroidApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by rick on 2015/11/18.
 * Assert中资源管理类
 */
public final class AssertUtils {
    private static final String TAG = "AssertUtils";

    /**
     * 从Assert中复制资源到目标目录中
     * @param descDir 目标目录。只是目录
     * @param descFile 目标文件名，只需要文件名，不需要路径
     * @param assertPath Assert里资源的路径
     * @param md5 传入需要比较的MD5，如果选择了强制复制，这里传入参数无效。如果不需要检验MD5，可以传入null
     * @param force 强制复制，而不进行文件重复检查
     * @return 是否复制成功
     */
    public static boolean copyFileFromAssert(final String descDir, final String descFile, final String assertPath, String md5, boolean force) {
        File dir = new File(descDir);
        if (! dir.exists() && ! dir.mkdir()) {
            return false; // 插件目录不能访问到，则不能做下一步的事情
        }
        File targetFile = new File(descDir + File.separator + descFile);
        if (! force) {
            // 文件不存在，则直接往下走进行复制
            // 文件存在，MD5不需要检验，直接返回true
            // 文件存在，MD5需要检验，MD5不相同，继续往下走；如果相同，返回true
            if (targetFile.exists() && (md5 == null || DigestUtils.fileMD5(targetFile).equals(md5))) {
                return true;
            }
        }

        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            InputStream in = AndroidApplication.getInstance().getAssets().open(assertPath);
            inBuff = new BufferedInputStream(in);
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] b = new byte[1024 * 10];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
            return true;
        } catch (Throwable e) {
            LogUtil.w(TAG, e);
            return false;
        } finally {
            try {
                if (inBuff != null) {
                    inBuff.close();
                }
                if (outBuff != null) {
                    outBuff.close();
                }
            } catch (Throwable e) {
                LogUtil.w(TAG, e);
            }
        }
    }

}
