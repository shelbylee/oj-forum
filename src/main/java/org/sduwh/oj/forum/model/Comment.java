package org.sduwh.oj.forum.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Comment {
    private Integer id;
    private String content;
    private Integer topicId;
    private Integer userId;
    private String createdAt;
    private String updatedAt;
    private Integer commentId;
    private String upIds;
}
