package com.bainiaohe.dodo.model;

import java.io.Serializable;

/**
 * Created by Lewis on 2015/1/30.
 */
public class User implements Serializable {

    private String userId;
    private String token;
    private String name;
    private String sex;
    private String phone;
    private String email;
    private String nickName;

    //用户的头像
    private String avatar;

    public User(String userId, String token, String name, String sex, String phone, String email, String avatar) {
        this.userId = userId;
        this.token = token;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;

    }


    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName(){
        return nickName;
    }
    public void setNickName(String nickName){
        this.nickName=nickName;
    }
    @Override
    public String toString() {
        return "Person [id=" + userId + ", name=" + name + ", token=" + token + ", phone=" + phone + ", sex=" + sex + ", email=" +
                email + ", avatar=" + avatar;
    }
}
