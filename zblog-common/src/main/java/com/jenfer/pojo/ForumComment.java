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
 * 评论
 * @TableName forum_comment
 */
@TableName(value ="forum_comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumComment implements Serializable {
    /**
     * 评论ID
     */
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer comment_id;

    /**
     * 父级评论ID
     */
    @TableField(value = "p_comment_id")
    private Integer p_comment_id;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private String article_id;

    /**
     * 回复内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 图片
     */
    @TableField(value = "img_path")
    private String img_path;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private String user_id;

    /**
     * 昵称
     */
    @TableField(value = "nick_name")
    private String nick_name;

    /**
     * 用户ip地址
     */
    @TableField(value = "user_ip_address")
    private String user_ip_address;

    /**
     * 回复人ID
     */
    @TableField(value = "reply_user_id")
    private String reply_user_id;

    /**
     * 回复人昵称
     */
    @TableField(value = "reply_nick_name")
    private String reply_nick_name;

    /**
     * 0:未置顶  1:置顶
     */
    @TableField(value = "top_type")
    private Integer top_type;

    /**
     * 发布时间
     */
    @TableField(value = "post_time")
    private Date post_time;

    /**
     * good数量
     */
    @TableField(value = "good_count")
    private Integer good_count;

    /**
     * 0:待审核  1:已审核
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
        ForumComment other = (ForumComment) that;
        return (this.getComment_id() == null ? other.getComment_id() == null : this.getComment_id().equals(other.getComment_id()))
            && (this.getP_comment_id() == null ? other.getP_comment_id() == null : this.getP_comment_id().equals(other.getP_comment_id()))
            && (this.getArticle_id() == null ? other.getArticle_id() == null : this.getArticle_id().equals(other.getArticle_id()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getImg_path() == null ? other.getImg_path() == null : this.getImg_path().equals(other.getImg_path()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getNick_name() == null ? other.getNick_name() == null : this.getNick_name().equals(other.getNick_name()))
            && (this.getUser_ip_address() == null ? other.getUser_ip_address() == null : this.getUser_ip_address().equals(other.getUser_ip_address()))
            && (this.getReply_user_id() == null ? other.getReply_user_id() == null : this.getReply_user_id().equals(other.getReply_user_id()))
            && (this.getReply_nick_name() == null ? other.getReply_nick_name() == null : this.getReply_nick_name().equals(other.getReply_nick_name()))
            && (this.getTop_type() == null ? other.getTop_type() == null : this.getTop_type().equals(other.getTop_type()))
            && (this.getPost_time() == null ? other.getPost_time() == null : this.getPost_time().equals(other.getPost_time()))
            && (this.getGood_count() == null ? other.getGood_count() == null : this.getGood_count().equals(other.getGood_count()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getComment_id() == null) ? 0 : getComment_id().hashCode());
        result = prime * result + ((getP_comment_id() == null) ? 0 : getP_comment_id().hashCode());
        result = prime * result + ((getArticle_id() == null) ? 0 : getArticle_id().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getImg_path() == null) ? 0 : getImg_path().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getNick_name() == null) ? 0 : getNick_name().hashCode());
        result = prime * result + ((getUser_ip_address() == null) ? 0 : getUser_ip_address().hashCode());
        result = prime * result + ((getReply_user_id() == null) ? 0 : getReply_user_id().hashCode());
        result = prime * result + ((getReply_nick_name() == null) ? 0 : getReply_nick_name().hashCode());
        result = prime * result + ((getTop_type() == null) ? 0 : getTop_type().hashCode());
        result = prime * result + ((getPost_time() == null) ? 0 : getPost_time().hashCode());
        result = prime * result + ((getGood_count() == null) ? 0 : getGood_count().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", comment_id=").append(comment_id);
        sb.append(", p_comment_id=").append(p_comment_id);
        sb.append(", article_id=").append(article_id);
        sb.append(", content=").append(content);
        sb.append(", img_path=").append(img_path);
        sb.append(", user_id=").append(user_id);
        sb.append(", nick_name=").append(nick_name);
        sb.append(", user_ip_address=").append(user_ip_address);
        sb.append(", reply_user_id=").append(reply_user_id);
        sb.append(", reply_nick_name=").append(reply_nick_name);
        sb.append(", top_type=").append(top_type);
        sb.append(", post_time=").append(post_time);
        sb.append(", good_count=").append(good_count);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}