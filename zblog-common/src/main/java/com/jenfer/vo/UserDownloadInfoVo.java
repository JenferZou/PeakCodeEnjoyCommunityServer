package com.jenfer.vo;

public class UserDownloadInfoVo {
    private Integer user_integral;

    private Boolean have_download;

    public UserDownloadInfoVo() {
    }

    public UserDownloadInfoVo(Integer user_integral, Boolean have_download) {
        this.user_integral = user_integral;
        this.have_download = have_download;
    }

    public Integer getUser_integral() {
        return user_integral;
    }

    public void setUser_integral(Integer user_integral) {
        this.user_integral = user_integral;
    }

    public Boolean getHave_download() {
        return have_download;
    }

    public void setHave_download(Boolean have_download) {
        this.have_download = have_download;
    }


    @Override
    public String toString() {
        return "UserDownloadInfoVo{" +
                "user_integral=" + user_integral +
                ", have_download=" + have_download +
                '}';
    }
}
