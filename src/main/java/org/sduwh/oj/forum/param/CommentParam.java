package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CommentParam {
    private Integer id;
    private String content;
    private Integer topicId;
    private Integer userId;
    private String createdAt;
    private Integer commentId;
}
