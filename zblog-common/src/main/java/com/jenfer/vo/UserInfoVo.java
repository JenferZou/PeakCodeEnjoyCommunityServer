package com.jenfer.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserInfoVo {

    private String user_id;

    /**
     * 昵称
     */
    private String nick_name;


    /**
     * 0:女 1:男
     */
    private Integer sex;

    /**
     * 个人描述
     */
    private String person_description;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date join_time;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH",timezone = "GMT+8")
    private Date last_login_time;


    /**
     * 当前积分
     */
    private Integer current_integral;


    private Integer post_count;

    private Integer like_count;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPerson_description() {
        return person_description;
    }

    public void setPerson_description(String person_description) {
        this.person_description = person_description;
    }

    public Date getJoin_time() {
        return join_time;
    }

    public void setJoin_time(Date join_time) {
        this.join_time = join_time;
    }

    public Date getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public Integer getCurrent_integral() {
        return current_integral;
    }

    public void setCurrent_integral(Integer current_integral) {
        this.current_integral = current_integral;
    }

    public void setPost_count(Integer post_count) {
        this.post_count = post_count;
    }

    public Integer getPost_count() {
        return post_count;
    }

    public Integer getLike_count() {
        return like_count;
    }

    public void setLike_count(Integer like_count) {
        this.like_count = like_count;
    }
}
