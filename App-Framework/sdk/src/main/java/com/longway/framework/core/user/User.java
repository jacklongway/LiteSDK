package com.longway.framework.core.user;

/**
 * Created by longway on 16/6/11.
 * Email:longway1991117@sina.com
 */

public class User {
    public String userId;
    public String userName;
    public String nickname;
    public String sex;
    public int sexType;
    public long birthday;
    public String token;
    public long expireTime;
    public long expireLength;
    public String email;
    public String phoneNumber;
    public String national;
    public String city;
    public String like;
    public String position;
    public String remark;
    public String source;
    public String sourceType;
    public String avatarUrl;

    public User() {

    }

    public User(String userId, String userName, String nickname, String sex, int sexType, long birthday, String token, long expireTime, long expireLength, String email, String phoneNumber, String national, String city, String like, String position, String remark, String source, String sourceType, String avatarUrl) {
        this.userId = userId;
        this.userName = userName;
        this.nickname = nickname;
        this.sex = sex;
        this.sexType = sexType;
        this.birthday = birthday;
        this.token = token;
        this.expireTime = expireTime;
        this.expireLength = expireLength;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.national = national;
        this.city = city;
        this.like = like;
        this.position = position;
        this.remark = remark;
        this.source = source;
        this.sourceType = sourceType;
        this.avatarUrl = avatarUrl;
    }

    public static final class Sex {
        public static final int MAN = 0;
        public static final int FEMALE = 1;
    }

    public static final class Source {
        public static final int QQ = 0;
        public static final int WEXIN = 1;
        public static final int SINA = 2;
    }

}
