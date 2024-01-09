package com.jenfer.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ForumArticleVo {


    private String article_id;

    private Integer board_id;

    private String board_name;


    private Integer p_board_id;


    private String p_board_name;

    private String user_id;

    private String nick_name;

    private String user_ip_address;

    private String title;

    private String cover;

    private String content;

    private String markdown_content;

    private String summary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date post_time;


    private Integer read_count;


    private Integer good_count;

    private Integer comment_count;

    private Integer top_type;

    private Integer attachment_type;

    private Integer editor_type;

    public Integer getP_board_id() {
        return p_board_id;
    }

    public void setP_board_id(Integer p_board_id) {
        this.p_board_id = p_board_id;
    }

    public Integer getGood_count() {
        return good_count;
    }

    public void setGood_count(Integer good_count) {
        this.good_count = good_count;
    }

    private Integer status;

    public ForumArticleVo() {
    }


    public String getMarkdown_content() {
        return markdown_content;
    }

    public void setMarkdown_content(String markdown_content) {
        this.markdown_content = markdown_content;
    }

    public Integer getEditor_type() {
        return editor_type;
    }

    public void setEditor_type(Integer editor_type) {
        this.editor_type = editor_type;
    }

    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }

    public Integer getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Integer board_id) {
        this.board_id = board_id;
    }

    public String getBoard_name() {
        return board_name;
    }

    public void setBoard_name(String board_name) {
        this.board_name = board_name;
    }



    public String getP_board_name() {
        return p_board_name;
    }

    public void setP_board_name(String p_board_name) {
        this.p_board_name = p_board_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_ip_address() {
        return user_ip_address;
    }

    public void setUser_ip_address(String user_ip_address) {
        this.user_ip_address = user_ip_address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getPost_time() {
        return post_time;
    }

    public void setPost_time(Date post_time) {
        this.post_time = post_time;
    }

    public Integer getRead_count() {
        return read_count;
    }

    public void setRead_count(Integer read_count) {
        this.read_count = read_count;
    }
    

    public Integer getComment_count() {
        return comment_count;
    }

    public void setComment_count(Integer comment_count) {
        this.comment_count = comment_count;
    }

    public Integer getTop_type() {
        return top_type;
    }

    public void setTop_type(Integer top_type) {
        this.top_type = top_type;
    }

    public Integer getAttachment_type() {
        return attachment_type;
    }

    public void setAttachment_type(Integer attachment_type) {
        this.attachment_type = attachment_type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
