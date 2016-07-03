package com.longway.framework.cache;


import com.longway.framework.util.DigestUtils;

public class MD5FileNameGenerator implements FileNameGenerator {
    public MD5FileNameGenerator() {
    }

    public String generate(String key) {
        return DigestUtils.md5Encrypt(key);
    }
}
