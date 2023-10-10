package com.jenfer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息
 * @TableName forum_article_attachment
 */
@TableName(value ="forum_article_attachment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumArticleAttachment implements Serializable {
    /**
     * 文件ID
     */
    @TableId(value = "file_id")
    private String file_id;

    /**
     * 文章ID
     */
    @TableField(value = "article_id")
    private String article_id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private String user_id;

    /**
     * 文件大小
     */
    @TableField(value = "file_size")
    private Long file_size;

    /**
     * 文件名称
     */
    @TableField(value = "file_name")
    private String file_name;

    /**
     * 下载次数
     */
    @TableField(value = "download_count")
    private Integer download_count;

    /**
     * 文件路径
     */
    @TableField(value = "file_path")
    private String file_path;

    /**
     * 文件类型
     */
    @TableField(value = "file_type")
    private Integer file_type;

    /**
     * 下载所需积分
     */
    @TableField(value = "integral")
    private Integer integral;

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
        ForumArticleAttachment other = (ForumArticleAttachment) that;
        return (this.getFile_id() == null ? other.getFile_id() == null : this.getFile_id().equals(other.getFile_id()))
            && (this.getArticle_id() == null ? other.getArticle_id() == null : this.getArticle_id().equals(other.getArticle_id()))
            && (this.getUser_id() == null ? other.getUser_id() == null : this.getUser_id().equals(other.getUser_id()))
            && (this.getFile_size() == null ? other.getFile_size() == null : this.getFile_size().equals(other.getFile_size()))
            && (this.getFile_name() == null ? other.getFile_name() == null : this.getFile_name().equals(other.getFile_name()))
            && (this.getDownload_count() == null ? other.getDownload_count() == null : this.getDownload_count().equals(other.getDownload_count()))
            && (this.getFile_path() == null ? other.getFile_path() == null : this.getFile_path().equals(other.getFile_path()))
            && (this.getFile_type() == null ? other.getFile_type() == null : this.getFile_type().equals(other.getFile_type()))
            && (this.getIntegral() == null ? other.getIntegral() == null : this.getIntegral().equals(other.getIntegral()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFile_id() == null) ? 0 : getFile_id().hashCode());
        result = prime * result + ((getArticle_id() == null) ? 0 : getArticle_id().hashCode());
        result = prime * result + ((getUser_id() == null) ? 0 : getUser_id().hashCode());
        result = prime * result + ((getFile_size() == null) ? 0 : getFile_size().hashCode());
        result = prime * result + ((getFile_name() == null) ? 0 : getFile_name().hashCode());
        result = prime * result + ((getDownload_count() == null) ? 0 : getDownload_count().hashCode());
        result = prime * result + ((getFile_path() == null) ? 0 : getFile_path().hashCode());
        result = prime * result + ((getFile_type() == null) ? 0 : getFile_type().hashCode());
        result = prime * result + ((getIntegral() == null) ? 0 : getIntegral().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", file_id=").append(file_id);
        sb.append(", article_id=").append(article_id);
        sb.append(", user_id=").append(user_id);
        sb.append(", file_size=").append(file_size);
        sb.append(", file_name=").append(file_name);
        sb.append(", download_count=").append(download_count);
        sb.append(", file_path=").append(file_path);
        sb.append(", file_type=").append(file_type);
        sb.append(", integral=").append(integral);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}