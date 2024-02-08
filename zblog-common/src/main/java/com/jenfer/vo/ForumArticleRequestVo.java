package com.jenfer.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumArticleRequestVo {



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

    private Integer pageNo;

    private Integer pageSize;



}
