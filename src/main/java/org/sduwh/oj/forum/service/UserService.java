package org.sduwh.oj.forum.service;

import org.sduwh.oj.forum.exception.ParamException;
import org.sduwh.oj.forum.mapper.CommentMapper;
import org.sduwh.oj.forum.mapper.TopicMapper;
import org.sduwh.oj.forum.model.Comment;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("userService")
public class UserService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private CommentMapper commentMapper;

    public boolean compareUserAndTopicId(Integer userId, Integer topicId) {
        Topic topic = topicMapper.selectById(topicId);
        if (userId != topic.getUserId()) {
            throw new ParamException("您没有权限修改该话题！");
        }
        return true;
    }

    public boolean compareUserAndCommentId(Integer userId, Integer commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (userId != comment.getUserId()) {
            throw new ParamException("您没有权限修改该评论！");
        }
        return true;
    }
}
