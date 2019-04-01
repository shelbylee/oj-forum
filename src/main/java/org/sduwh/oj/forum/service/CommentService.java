package org.sduwh.oj.forum.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import org.sduwh.oj.forum.common.CacheKeyConstants;
import org.sduwh.oj.forum.mapper.CommentMapper;
import org.sduwh.oj.forum.model.Comment;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.CommentParam;
import org.sduwh.oj.forum.util.DateUtil;
import org.sduwh.oj.forum.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Service("commentService")
public class CommentService {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;
    @Resource
    private CommentMapper commentMapper;

    public Comment saveComment(CommentParam param) {

        Comment comment = new Comment();
        comment.setCommentId(param.getCommentId());
        comment.setTopicId(param.getTopicId());
        comment.setContent(param.getContent());
        comment.setUserId(userService.getUserId());
        comment.setCreatedAt(DateUtil.formatDateTime(new Date()));

        Topic topic = topicService.getTopicByIdWithoutComment(param.getTopicId());
        Preconditions.checkNotNull(topic, "你晚了一步，话题可能已经被删除了");

        commentMapper.insert(comment);

        return comment;
    }

    public Comment editCommentById(CommentParam param) {

        Integer userId = userService.getUserId();
        String content = param.getContent();
        Integer commentId = param.getId();

        Comment comment = commentMapper.selectById(commentId);
        Preconditions.checkNotNull(comment, "这个评论可能已经被删除了，多发点对别人有帮助的评论吧");

        if (userService.compareUserAndCommentId(userId, commentId)) {
            comment.setContent(content);
            comment.setUpdatedAt(DateUtil.formatDateTime(new Date()));

            commentMapper.update(comment);
        }

        return comment;
    }

    public void deleteCommentById(CommentParam param) {
        Integer commentId = param.getId();
        Integer userId = userService.getUserId();

        Comment comment = commentMapper.selectById(commentId);
        Preconditions.checkNotNull(comment, "这个评论可能已经被删除了，多发点对别人有帮助的评论吧");

        if (userService.compareUserAndCommentId(userId, commentId)) {
            commentMapper.deleteById(commentId);
        }
    }

    public List<CommentParam> selectByTopicId(Integer topicId) {

        List<CommentParam> comments;

        String cacheComments = cacheService.getFromCache(CacheKeyConstants.FORUM_COMMENTS_KEY, String.valueOf(topicId));

        if (Strings.isNullOrEmpty(cacheComments)) {
            comments = commentMapper.selectByTopicId(topicId);
            cacheService.saveCache(JsonUtil.objectToJson(comments), 3600, CacheKeyConstants.FORUM_COMMENTS_KEY, String.valueOf(topicId));
        } else {
            Type type = new TypeToken<List<CommentParam>>() {}.getType();
            comments = JsonUtil.jsonToObject(cacheComments, type);
        }
        return comments;
    }
}
