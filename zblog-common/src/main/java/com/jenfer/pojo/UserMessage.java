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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户消息
 * @TableName user_message
 */
@TableName(value ="user_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage implements Serializable {
    /**
     * 自增ID
     */
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer message_id;

    /**
     * 接收人用户ID
     */
    @TableField(value = "received_user_id")
    private String received_user_id;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private String article_id;

    /**
     * 文章标题
     */
    @TableField(value = "article_title")
    private String article_title;

    /**
     * 评论ID
     */
    @TableField(value = "comment_id")
    private Integer comment_id;

    /**
     * 发送人用户ID
     */
    @TableField(value = "send_user_id")
    private String send_user_id;

    /**
     * 发送人昵称
     */
    @TableField(value = "send_nick_name")
    private String send_nick_name;

    /**
     * 0:系统消息 1:评论 2:文章点赞  3:评论点赞 4:附件下载
     */
    @TableField(value = "message_type")
    private Integer message_type;

    /**
     * 消息内容
     */
    @TableField(value = "message_content")
    private String message_content;

    /**
     * 1:未读 2:已读
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
        UserMessage other = (UserMessage) that;
        return (this.getMessage_id() == null ? other.getMessage_id() == null : this.getMessage_id().equals(other.getMessage_id()))
            && (this.getReceived_user_id() == null ? other.getReceived_user_id() == null : this.getReceived_user_id().equals(other.getReceived_user_id()))
            && (this.getArticle_id() == null ? other.getArticle_id() == null : this.getArticle_id().equals(other.getArticle_id()))
            && (this.getArticle_title() == null ? other.getArticle_title() == null : this.getArticle_title().equals(other.getArticle_title()))
            && (this.getComment_id() == null ? other.getComment_id() == null : this.getComment_id().equals(other.getComment_id()))
            && (this.getSend_user_id() == null ? other.getSend_user_id() == null : this.getSend_user_id().equals(other.getSend_user_id()))
            && (this.getSend_nick_name() == null ? other.getSend_nick_name() == null : this.getSend_nick_name().equals(other.getSend_nick_name()))
            && (this.getMessage_type() == null ? other.getMessage_type() == null : this.getMessage_type().equals(other.getMessage_type()))
            && (this.getMessage_content() == null ? other.getMessage_content() == null : this.getMessage_content().equals(other.getMessage_content()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreate_time() == null ? other.getCreate_time() == null : this.getCreate_time().equals(other.getCreate_time()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMessage_id() == null) ? 0 : getMessage_id().hashCode());
        result = prime * result + ((getReceived_user_id() == null) ? 0 : getReceived_user_id().hashCode());
        result = prime * result + ((getArticle_id() == null) ? 0 : getArticle_id().hashCode());
        result = prime * result + ((getArticle_title() == null) ? 0 : getArticle_title().hashCode());
        result = prime * result + ((getComment_id() == null) ? 0 : getComment_id().hashCode());
        result = prime * result + ((getSend_user_id() == null) ? 0 : getSend_user_id().hashCode());
        result = prime * result + ((getSend_nick_name() == null) ? 0 : getSend_nick_name().hashCode());
        result = prime * result + ((getMessage_type() == null) ? 0 : getMessage_type().hashCode());
        result = prime * result + ((getMessage_content() == null) ? 0 : getMessage_content().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreate_time() == null) ? 0 : getCreate_time().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", message_id=").append(message_id);
        sb.append(", received_user_id=").append(received_user_id);
        sb.append(", article_id=").append(article_id);
        sb.append(", article_title=").append(article_title);
        sb.append(", comment_id=").append(comment_id);
        sb.append(", send_user_id=").append(send_user_id);
        sb.append(", send_nick_name=").append(send_nick_name);
        sb.append(", message_type=").append(message_type);
        sb.append(", message_content=").append(message_content);
        sb.append(", status=").append(status);
        sb.append(", create_time=").append(create_time);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}