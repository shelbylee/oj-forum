package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class TopicParam {

    private Integer id;

    @NotBlank(message = "标题不可以为空")
    private String title;

    @NotBlank(message = "内容不可以为空")
    private String content;

    private String createdAt;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    private Integer commentCount;

    private Integer likeCount;

    private Integer viewCount;

    private Integer problemId;

    private Integer contestId;

    private Integer visible;
}
