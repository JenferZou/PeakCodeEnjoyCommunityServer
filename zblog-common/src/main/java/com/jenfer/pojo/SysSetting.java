package com.jenfer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.*;

/**
 * 系统设置信息
 * @TableName sys_setting
 */
@TableName(value ="sys_setting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SysSetting implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "code")
    private String code;

    /**
     * 设置信息
     */
    @TableField(value = "json_content")
    private String json_content;

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
        SysSetting other = (SysSetting) that;
        return (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getJson_content() == null ? other.getJson_content() == null : this.getJson_content().equals(other.getJson_content()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getJson_content() == null) ? 0 : getJson_content().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", code=").append(code);
        sb.append(", json_content=").append(json_content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}