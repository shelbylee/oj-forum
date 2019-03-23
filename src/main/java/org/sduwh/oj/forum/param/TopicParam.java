package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TopicParam {
    private Integer id;
    private String title;
    private String content;
    private String createdAt;
    private Integer userId;
    private Integer commentCount;
    private Integer likeCount;
    private Integer viewCount;
}
