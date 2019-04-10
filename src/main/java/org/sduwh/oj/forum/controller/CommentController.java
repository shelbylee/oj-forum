package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.model.Comment;
import org.sduwh.oj.forum.param.CommentParam;
import org.sduwh.oj.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 创建评论
    @PostMapping()
    public ResultMessage create(CommentParam param) {
        Comment comment = commentService.saveComment(param);
        return ResultMessage.success(comment);
    }

    // 编辑评论
    @PutMapping()
    public ResultMessage edit(CommentParam param) {
        Comment comment = commentService.editCommentById(param);
        return ResultMessage.success(comment);
    }

    // 删除评论
    @DeleteMapping()
    public ResultMessage delete(CommentParam param) {
        commentService.deleteCommentById(param);
        return ResultMessage.success();
    }
}
