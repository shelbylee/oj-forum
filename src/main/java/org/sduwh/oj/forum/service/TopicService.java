package org.sduwh.oj.forum.service;

import com.google.common.base.Preconditions;
import org.sduwh.oj.forum.common.CacheKeyConstants;
import org.sduwh.oj.forum.common.RequestHolder;
import org.sduwh.oj.forum.mapper.TopicMapper;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.CommentParam;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.util.DateUtil;
import org.sduwh.oj.forum.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("topicService")
public class TopicService {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Resource
    private TopicMapper topicMapper;

    public TopicParam getTopicById(Integer topicId) {

        List<CommentParam> comments = commentService.selectByTopicId(topicId);

        Topic topic = getTopicByIdWithoutComment(topicId);

        TopicParam topicWithComments = JsonUtil.jsonToObject(JsonUtil.objectToJson(topic), TopicParam.class);

        Preconditions.checkNotNull(topicWithComments);
        if (!StringUtils.isEmpty(comments))
            topicWithComments.setComments(comments);

        return topicWithComments;
    }

    public Topic getTopicByIdWithoutComment(Integer topicId) {

        String cacheTopic = cacheService.getFromCache(CacheKeyConstants.FORUM_TOPIC_KEY, String.valueOf(topicId));

        Topic topicResponse = JsonUtil.jsonToObject(cacheTopic, Topic.class);

        if (StringUtils.isEmpty(topicResponse)) {
            Topic topic = topicMapper.selectById(topicId);
            if (!StringUtils.isEmpty(topic)) {
                cacheService.saveCache(JsonUtil.objectToJson(topic), 3600, CacheKeyConstants.FORUM_TOPIC_KEY, String.valueOf(topicId));
            }
            return topic;
        }

        return topicResponse;
    }

    public Topic saveTopic(TopicParam param) {

        Topic topic = new Topic();
        topic.setTitle(param.getTitle());
        topic.setContent(param.getContent());
        topic.setUserId(RequestHolder.getCurrentUser().getUserId());
        topic.setCreatedAt(DateUtil.formatDateTime(new Date()));
        topic.setCommentCount(0);
        topic.setLikeCount(0);
        topic.setViewCount(0);
        topic.setProblemId(param.getProblemId());
        topic.setContestId(param.getContestId());

        topicMapper.insert(topic);

        return topic;
    }

    public Topic editTopicById(TopicParam param) {

        Integer topicId = param.getId();
        Topic topic = topicMapper.selectById(topicId);

        Integer userId = RequestHolder.getCurrentUser().getUserId();

        String title = param.getTitle();
        String content = param.getContent();

        if (userService.compareUserAndTopicId(userId, topicId)) {
            topic.setTitle(title);
            topic.setContent(content);
            topic.setUpdatedAt(DateUtil.formatDateTime(new Date()));

            topicMapper.update(topic);
        }

        return topic;
    }

    public void deleteTopicById(TopicParam param) {
        Integer topicId = param.getId();
        Integer userId = RequestHolder.getCurrentUser().getUserId();

        if (userService.compareUserAndTopicId(userId, topicId)) {
            topicMapper.deleteById(topicId);
        }
    }

}
