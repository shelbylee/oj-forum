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
import org.sduwh.oj.forum.util.SensitiveWordsFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private SensitiveWordsFilterUtil sensitiveWordsFilterUtil;
    @Resource
    private CommentMapper commentMapper;

    public Comment saveComment(CommentParam param) {

        Comment comment = new Comment();
        comment.setCommentId(param.getCommentId());
        Preconditions.checkNotNull(param.getTopicId(), "必须绑定topic id！");
        comment.setTopicId(param.getTopicId());
        comment.setContent(param.getContent());
        comment.setUserId(userService.getUserId());
        comment.setCreatedAt(DateUtil.formatDateTime(new Date()));

        Topic topic = topicService.getTopicByIdWithoutComment(param.getTopicId());
        Preconditions.checkNotNull(topic, "你晚了一步，话题可能已经被删除了");

        insertCommentAndUpdateTopic(comment, topic);

        return comment;
    }

    @Transactional
    public void insertCommentAndUpdateTopic(Comment comment, Topic topic) {
        sensitiveWordsFilterUtil.filterComment(comment);
        commentMapper.insert(comment);
        topic.setCommentCount(commentMapper.getPostCount(topic.getId()));
        topicService.update(topic);
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
            sensitiveWordsFilterUtil.filterComment(comment);
            commentMapper.update(comment);
        }

        return comment;
    }

    public void deleteCommentById(Integer commentId) {
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

        if (Strings.isNullOrEmpty(cacheComments) || cacheComments.length() == 0 || cacheComments.equals("[]")) {
            comments = commentMapper.selectByTopicId(topicId);
            cacheService.saveCache(JsonUtil.objectToJson(comments), 3600, CacheKeyConstants.FORUM_COMMENTS_KEY, String.valueOf(topicId));
        } else {
            Type type = new TypeToken<List<CommentParam>>() {
            }.getType();
            comments = JsonUtil.jsonToObject(cacheComments, type);
        }
        return comments;
    }
}
