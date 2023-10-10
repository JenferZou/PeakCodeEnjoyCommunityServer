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
 * 点赞记录
 * @TableName like_record
 */
@TableName(value ="like_record")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeRecord implements Serializable {
    /**
     * 自增ID
     */
    @TableId(value = "op_id", type = IdType.AUTO)
    private Integer op_id;

    /**
     * 操作类型0:文章点赞 1:评论点赞
     */
    @TableField(value = "op_type")
    private Integer op_type;

    /**
     * 主体ID
     */
    @TableField(value = "object_id")
    private String object_id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String user_id;

    /**
     * 发布时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 主体作者ID
     */
    @TableField(value = "author_user_id")
    private String author_user_id;

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
        LikeRecord other = (LikeRecord) that;
        return (this.getOp_id() == null ? other.getOp_id() == null : this.getOp_id().equals(other.getOp_id()))
            && (this.getOp_type() == null ? other.getOp_type() == null : this.getOp_type().equals(other.getOp_type()))
            && (this.getObject_id() == null ? other.getObject_id() == null : this.getObject_id().equals(other.getObject_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()))
            && (this.getAuthor_user_id() == null ? other.getAuthor_user_id() == null : this.getAuthor_user_id().equals(other.getAuthor_user_id()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOp_id() == null) ? 0 : getOp_id().hashCode());
        result = prime * result + ((getOp_type() == null) ? 0 : getOp_type().hashCode());
        result = prime * result + ((getObject_id() == null) ? 0 : getObject_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        result = prime * result + ((getAuthor_user_id() == null) ? 0 : getAuthor_user_id().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", op_id=").append(op_id);
        sb.append(", op_type=").append(op_type);
        sb.append(", object_id=").append(object_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", create_time=").append(create_time);
        sb.append(", author_user_id=").append(author_user_id);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}