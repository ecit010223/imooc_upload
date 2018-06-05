package com.year18.imooc_upload.entity;

/**
 * 作者：张玉辉 on 2018/5/1 14:55.
 */
@Deprecated
public class UserInfo {
    private String userName;
    private String password;
    private String sex;

    public UserInfo() {
    }

    public UserInfo(String userName, String password, String sex) {
        this.userName = userName;
        this.password = password;
        this.sex = sex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
