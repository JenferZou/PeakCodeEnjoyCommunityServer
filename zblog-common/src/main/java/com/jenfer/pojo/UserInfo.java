package com.jenfer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "user_id")
    private String user_id;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nick_name;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 0:女 1:男
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 个人描述
     */
    @TableField(value = "person_description")
    private String person_description;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "join_time")
    private Date join_time;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "last_login_time")
    private Date last_login_time;

    /**
     * 最后登录IP
     */
    @TableField(value = "last_login_ip")
    private String last_login_ip;

    /**
     * 最后登录ip地址
     */
    @TableField(value = "last_login_ip_address")
    private String last_login_ip_address;

    /**
     * 积分
     */
    @TableField(value = "total_integral")
    private Integer total_integral;

    /**
     * 当前积分
     */
    @TableField(value = "current_integral")
    private Integer current_integral;

    /**
     * 0:禁用 1:正常
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserInfo other = (UserInfo) that;
        return (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getNick_name() == null ? other.getNick_name() == null : this.getNick_name().equals(other.getNick_name()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getSex() == null ? other.getSex() == null : this.getSex().equals(other.getSex()))
            && (this.getPerson_description() == null ? other.getPerson_description() == null : this.getPerson_description().equals(other.getPerson_description()))
            && (this.getJoin_time() == null ? other.getJoin_time() == null : this.getJoin_time().equals(other.getJoin_time()))
            && (this.getLast_login_time() == null ? other.getLast_login_time() == null : this.getLast_login_time().equals(other.getLast_login_time()))
            && (this.getLast_login_ip() == null ? other.getLast_login_ip() == null : this.getLast_login_ip().equals(other.getLast_login_ip()))
            && (this.getLast_login_ip_address() == null ? other.getLast_login_ip_address() == null : this.getLast_login_ip_address().equals(other.getLast_login_ip_address()))
            && (this.getTotal_integral() == null ? other.getTotal_integral() == null : this.getTotal_integral().equals(other.getTotal_integral()))
            && (this.getCurrent_integral() == null ? other.getCurrent_integral() == null : this.getCurrent_integral().equals(other.getCurrent_integral()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getNick_name() == null) ? 0 : getNick_name().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getSex() == null) ? 0 : getSex().hashCode());
        result = prime * result + ((getPerson_description() == null) ? 0 : getPerson_description().hashCode());
        result = prime * result + ((getJoin_time() == null) ? 0 : getJoin_time().hashCode());
        result = prime * result + ((getLast_login_time() == null) ? 0 : getLast_login_time().hashCode());
        result = prime * result + ((getLast_login_ip() == null) ? 0 : getLast_login_ip().hashCode());
        result = prime * result + ((getLast_login_ip_address() == null) ? 0 : getLast_login_ip_address().hashCode());
        result = prime * result + ((getTotal_integral() == null) ? 0 : getTotal_integral().hashCode());
        result = prime * result + ((getCurrent_integral() == null) ? 0 : getCurrent_integral().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", user_id=").append(user_id);
        sb.append(", nick_name=").append(nick_name);
        sb.append(", email=").append(email);
        sb.append(", password=").append(password);
        sb.append(", sex=").append(sex);
        sb.append(", person_description=").append(person_description);
        sb.append(", join_time=").append(join_time);
        sb.append(", last_login_time=").append(last_login_time);
        sb.append(", last_login_ip=").append(last_login_ip);
        sb.append(", last_login_ip_address=").append(last_login_ip_address);
        sb.append(", total_integral=").append(total_integral);
        sb.append(", current_integral=").append(current_integral);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}