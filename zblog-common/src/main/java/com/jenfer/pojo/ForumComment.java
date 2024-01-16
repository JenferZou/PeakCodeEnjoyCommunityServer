package com.jenfer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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



    //queryExtend
    @TableField(exist = false)
    private List<ForumComment> sub_forumComment_list;

    //点赞类型 0:未点赞 1：已点赞
    @TableField(exist = false)
    private Integer likeType;








}