package com.jenfer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章板块信息
 * @TableName forum_board
 */
@TableName(value ="forum_board")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumBoard implements Serializable {
    /**
     * 板块ID
     */
    @TableId(value = "board_id")
    private Integer board_id;

    /**
     * 父级板块ID
     */
    @TableField(value = "p_board_id")
    private Integer p_board_id;

    /**
     * 板块名
     */
    @TableField(value = "board_name")
    private String board_name;

    /**
     * 封面
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 描述
     */
    @TableField(value = "board_desc")
    private String board_desc;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 0:只允许管理员发帖 1:任何人可以发帖
     */
    @TableField(value = "post_type")
    private Integer post_type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<ForumBoard> children;





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
        ForumBoard other = (ForumBoard) that;
        return (this.getBoard_id() == null ? other.getBoard_id() == null : this.getBoard_id().equals(other.getBoard_id()))
            && (this.getP_board_id() == null ? other.getP_board_id() == null : this.getP_board_id().equals(other.getP_board_id()))
            && (this.getBoard_name() == null ? other.getBoard_name() == null : this.getBoard_name().equals(other.getBoard_name()))
            && (this.getCover() == null ? other.getCover() == null : this.getCover().equals(other.getCover()))
            && (this.getBoard_desc() == null ? other.getBoard_desc() == null : this.getBoard_desc().equals(other.getBoard_desc()))
            && (this.getSort() == null ? other.getSort() == null : this.getSort().equals(other.getSort()))
            && (this.getPost_type() == null ? other.getPost_type() == null : this.getPost_type().equals(other.getPost_type()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getBoard_id() == null) ? 0 : getBoard_id().hashCode());
        result = prime * result + ((getP_board_id() == null) ? 0 : getP_board_id().hashCode());
        result = prime * result + ((getBoard_name() == null) ? 0 : getBoard_name().hashCode());
        result = prime * result + ((getCover() == null) ? 0 : getCover().hashCode());
        result = prime * result + ((getBoard_desc() == null) ? 0 : getBoard_desc().hashCode());
        result = prime * result + ((getSort() == null) ? 0 : getSort().hashCode());
        result = prime * result + ((getPost_type() == null) ? 0 : getPost_type().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", board_id=").append(board_id);
        sb.append(", p_board_id=").append(p_board_id);
        sb.append(", board_name=").append(board_name);
        sb.append(", cover=").append(cover);
        sb.append(", board_desc=").append(board_desc);
        sb.append(", sort=").append(sort);
        sb.append(", post_type=").append(post_type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}