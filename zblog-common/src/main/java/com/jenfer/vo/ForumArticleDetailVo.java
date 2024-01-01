package com.jenfer.vo;

import java.io.Serializable;

public class ForumArticleDetailVo implements Serializable {
    private ForumArticleVo forumArticleVo;

    private ForumArticleAttachmentVo forumArticleAttachmentVo;
    private Boolean haveLike = false;

    public ForumArticleVo getForumArticleVo() {
        return forumArticleVo;
    }

    public void setForumArticleVo(ForumArticleVo forumArticleVo) {
        this.forumArticleVo = forumArticleVo;
    }

    public ForumArticleAttachmentVo getForumArticleAttachmentVo() {
        return forumArticleAttachmentVo;
    }

    public void setForumArticleAttachmentVo(ForumArticleAttachmentVo forumArticleAttachmentVo) {
        this.forumArticleAttachmentVo = forumArticleAttachmentVo;
    }

    public Boolean getHaveLike() {
        return haveLike;
    }

    public void setHaveLike(Boolean haveLike) {
        this.haveLike = haveLike;
    }
}
