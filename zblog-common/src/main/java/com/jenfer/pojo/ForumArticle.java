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
 * 文章信息
 * @TableName forum_article
 */
@TableName(value ="forum_article")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumArticle implements Serializable {
    /**
     * 文章ID
     */
    @TableId(value = "article_id")
    private String article_id;

    /**
     * 板块ID
     */
    @TableField(value = "board_id")
    private Integer board_id;

    /**
     * 板块名称
     */
    @TableField(value = "board_name")
    private String board_name;

    /**
     * 父级板块ID
     */
    @TableField(value = "p_board_id")
    private Integer p_board_id;

    /**
     * 父板块名称
     */
    @TableField(value = "p_board_name")
    private String p_board_name;

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
     * 最后登录ip地址
     */
    @TableField(value = "user_ip_address")
    private String user_ip_address;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 封面
     */
    @TableField(value = "cover")
    private String cover;

    /**
     * 内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * markdown内容
     */
    @TableField(value = "markdown_content")
    private String markdown_content;

    /**
     * 0:富文本编辑器 1:markdown编辑器
     */
    @TableField(value = "editor_type")
    private Integer editor_type;

    /**
     * 摘要
     */
    @TableField(value = "summary")
    private String summary;

    /**
     * 发布时间
     */
    @TableField(value = "post_time")
    private Date post_time;

    /**
     * 最后更新时间
     */
    @TableField(value = "last_update_time")
    private Date last_update_time;

    /**
     * 阅读数量
     */
    @TableField(value = "read_count")
    private Integer read_count;

    /**
     * 点赞数
     */
    @TableField(value = "good_count")
    private Integer good_count;

    /**
     * 评论数
     */
    @TableField(value = "comment_count")
    private Integer comment_count;

    /**
     * 0未置顶  1:已置顶
     */
    @TableField(value = "top_type")
    private Integer top_type;

    /**
     * 0:没有附件  1:有附件
     */
    @TableField(value = "attachment_type")
    private Integer attachment_type;

    /**
     * -1已删除 0:待审核  1:已审核 
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
        ForumArticle other = (ForumArticle) that;
        return (this.getArticle_id() == null ? other.getArticle_id() == null : this.getArticle_id().equals(other.getArticle_id()))
            && (this.getBoard_id() == null ? other.getBoard_id() == null : this.getBoard_id().equals(other.getBoard_id()))
            && (this.getBoard_name() == null ? other.getBoard_name() == null : this.getBoard_name().equals(other.getBoard_name()))
            && (this.getP_board_id() == null ? other.getP_board_id() == null : this.getP_board_id().equals(other.getP_board_id()))
            && (this.getP_board_name() == null ? other.getP_board_name() == null : this.getP_board_name().equals(other.getP_board_name()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getNick_name() == null ? other.getNick_name() == null : this.getNick_name().equals(other.getNick_name()))
            && (this.getUser_ip_address() == null ? other.getUser_ip_address() == null : this.getUser_ip_address().equals(other.getUser_ip_address()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getCover() == null ? other.getCover() == null : this.getCover().equals(other.getCover()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getMarkdown_content() == null ? other.getMarkdown_content() == null : this.getMarkdown_content().equals(other.getMarkdown_content()))
            && (this.getEditor_type() == null ? other.getEditor_type() == null : this.getEditor_type().equals(other.getEditor_type()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getPost_time() == null ? other.getPost_time() == null : this.getPost_time().equals(other.getPost_time()))
            && (this.getLast_update_time() == null ? other.getLast_update_time() == null : this.getLast_update_time().equals(other.getLast_update_time()))
            && (this.getRead_count() == null ? other.getRead_count() == null : this.getRead_count().equals(other.getRead_count()))
            && (this.getGood_count() == null ? other.getGood_count() == null : this.getGood_count().equals(other.getGood_count()))
            && (this.getComment_count() == null ? other.getComment_count() == null : this.getComment_count().equals(other.getComment_count()))
            && (this.getTop_type() == null ? other.getTop_type() == null : this.getTop_type().equals(other.getTop_type()))
            && (this.getAttachment_type() == null ? other.getAttachment_type() == null : this.getAttachment_type().equals(other.getAttachment_type()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getArticle_id() == null) ? 0 : getArticle_id().hashCode());
        result = prime * result + ((getBoard_id() == null) ? 0 : getBoard_id().hashCode());
        result = prime * result + ((getBoard_name() == null) ? 0 : getBoard_name().hashCode());
        result = prime * result + ((getP_board_id() == null) ? 0 : getP_board_id().hashCode());
        result = prime * result + ((getP_board_name() == null) ? 0 : getP_board_name().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getNick_name() == null) ? 0 : getNick_name().hashCode());
        result = prime * result + ((getUser_ip_address() == null) ? 0 : getUser_ip_address().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCover() == null) ? 0 : getCover().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        result = prime * result + ((getMarkdown_content() == null) ? 0 : getMarkdown_content().hashCode());
        result = prime * result + ((getEditor_type() == null) ? 0 : getEditor_type().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getPost_time() == null) ? 0 : getPost_time().hashCode());
        result = prime * result + ((getLast_update_time() == null) ? 0 : getLast_update_time().hashCode());
        result = prime * result + ((getRead_count() == null) ? 0 : getRead_count().hashCode());
        result = prime * result + ((getGood_count() == null) ? 0 : getGood_count().hashCode());
        result = prime * result + ((getComment_count() == null) ? 0 : getComment_count().hashCode());
        result = prime * result + ((getTop_type() == null) ? 0 : getTop_type().hashCode());
        result = prime * result + ((getAttachment_type() == null) ? 0 : getAttachment_type().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", article_id=").append(article_id);
        sb.append(", board_id=").append(board_id);
        sb.append(", board_name=").append(board_name);
        sb.append(", p_board_id=").append(p_board_id);
        sb.append(", p_board_name=").append(p_board_name);
        sb.append(", user_id=").append(user_id);
        sb.append(", nick_name=").append(nick_name);
        sb.append(", user_ip_address=").append(user_ip_address);
        sb.append(", title=").append(title);
        sb.append(", cover=").append(cover);
        sb.append(", content=").append(content);
        sb.append(", markdown_content=").append(markdown_content);
        sb.append(", editor_type=").append(editor_type);
        sb.append(", summary=").append(summary);
        sb.append(", post_time=").append(post_time);
        sb.append(", last_update_time=").append(last_update_time);
        sb.append(", read_count=").append(read_count);
        sb.append(", good_count=").append(good_count);
        sb.append(", comment_count=").append(comment_count);
        sb.append(", top_type=").append(top_type);
        sb.append(", attachment_type=").append(attachment_type);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}