package com.jenfer.dto;

/**
 * 审核设置
 */
public class SysSettingAuditDto {

    /**
     * 帖子是否需要审核
     */
    private Boolean postAudit;

    /**
     * 评论是否需要审核
     */
    private Boolean commentAudit;

    public Boolean getPostAudit() {
        return postAudit;
    }

    public void setPostAudit(Boolean postAudit) {
        this.postAudit = postAudit;
    }

    public Boolean getCommentAudit() {
        return commentAudit;
    }

    public void setCommentAudit(Boolean commentAudit) {
        this.commentAudit = commentAudit;
    }
}
