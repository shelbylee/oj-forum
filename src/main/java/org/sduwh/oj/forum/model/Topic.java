package org.sduwh.oj.forum.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Topic {
    private Integer id;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private Integer userId;
    private Integer commentCount;
    private String upIds;
    private Integer likeCount;
    private Integer viewCount;
    private Integer problemId;
    private Integer contestId;
    private Integer visible;
    private Integer contestCreatorId;
}
