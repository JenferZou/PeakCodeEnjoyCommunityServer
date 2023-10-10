package com.jenfer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户积分记录表
 * @TableName user_integral_record
 */
@TableName(value ="user_integral_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIntegralRecord implements Serializable {
    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer record_id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String user_id;

    /**
     * 操作类型
     */
    @TableField(value = "oper_type")
    private Integer oper_type;

    /**
     * 积分
     */
    @TableField(value = "integral")
    private Integer integral;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

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
        UserIntegralRecord other = (UserIntegralRecord) that;
        return (this.getRecord_id() == null ? other.getRecord_id() == null : this.getRecord_id().equals(other.getRecord_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getOper_type() == null ? other.getOper_type() == null : this.getOper_type().equals(other.getOper_type()))
            && (this.getIntegral() == null ? other.getIntegral() == null : this.getIntegral().equals(other.getIntegral()))
            && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRecord_id() == null) ? 0 : getRecord_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getOper_type() == null) ? 0 : getOper_type().hashCode());
        result = prime * result + ((getIntegral() == null) ? 0 : getIntegral().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", record_id=").append(record_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", oper_type=").append(oper_type);
        sb.append(", integral=").append(integral);
        sb.append(", create_time=").append(create_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}