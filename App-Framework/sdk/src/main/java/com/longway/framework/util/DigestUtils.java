package com.longway.framework.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/1/22.
 */
public class DigestUtils {
    private static final String TAG = "EncryptionUtils";
    private static final String[] HEX = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    private DigestUtils(){

    }

    public static String md5Encrypt(String source) {
        if (null == source || "".equals(source)) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] output = messageDigest.digest(source.getBytes());
            Log.i(TAG, "digest len <<" + output.length);
            return getString(output);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return source;
    }

    private static String getString(byte[] output) {
        int len = output.length;
        StringBuilder result = new StringBuilder(len * 2);
        for (int i = 0; i < len; i++) {
            byte value = output[i];
            int low = value & 0x0F;
            int high = value >>> 4 & 0x0F;
            result.append(HEX[high]).append(HEX[low]);
        }
        return result.toString();
    }

    public static String fileMD5(File file) {
        long start = System.currentTimeMillis();
        if (file == null || !file.exists()) {
            return "";
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            int len;
            byte[] buffer = new byte[8192];
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            while ((len = inputStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, len);
            }
            long end = System.currentTimeMillis();
            Log.i(TAG, (end - start) + "");
            return getString(messageDigest.digest());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return "";
    }

}
