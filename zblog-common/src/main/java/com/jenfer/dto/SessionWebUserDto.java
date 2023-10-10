package com.jenfer.dto;

public class SessionWebUserDto {
    private String nickName;

    private String province;
    private String userId;

    private Boolean isAdmin;

    public SessionWebUserDto() {
    }

    public SessionWebUserDto(String nickName, String province, String userId, Boolean isAdmin) {
        this.nickName = nickName;
        this.province = province;
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
