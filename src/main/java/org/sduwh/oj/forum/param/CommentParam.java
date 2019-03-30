package org.sduwh.oj.forum.param;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
public class CommentParam {

    private Integer id;

    @NotBlank(message = "内容不能为空")
    private String content;

    @NotNull(message = "必须提供评论所在的话题")
    private Integer topicId;

    private Integer userId;

    private String createdAt;

    private String updatedAt;

    private Integer commentId;
}
