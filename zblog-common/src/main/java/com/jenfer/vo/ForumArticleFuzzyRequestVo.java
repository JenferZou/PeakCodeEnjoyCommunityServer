package com.jenfer.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForumArticleFuzzyRequestVo {

    private String titleFuzzy;

    private String nickNameFuzzy;

    private Integer attachmentType;

    private Integer status;


    private Integer pageNo;

    private Integer pageSize;



}
