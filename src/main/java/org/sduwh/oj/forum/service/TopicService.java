package org.sduwh.oj.forum.service;

import com.google.common.base.Preconditions;
import org.sduwh.oj.forum.common.CacheKeyConstants;
import org.sduwh.oj.forum.common.RequestHolder;
import org.sduwh.oj.forum.exception.ParamException;
import org.sduwh.oj.forum.mapper.TopicMapper;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.CommentParam;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.util.DateUtil;
import org.sduwh.oj.forum.util.IpUtil;
import org.sduwh.oj.forum.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

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

        topic = addViewCount(topic);

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
        topic.setUserId(userService.getUserId());
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

        Integer userId = userService.getUserId();

        String title = param.getTitle();
        String content = param.getContent();

        if (userService.compareUserAndTopicId(userId, topicId)) {
            topic.setTitle(title);
            topic.setContent(content);
            topic.setUpdatedAt(DateUtil.formatDateTime(new Date()));

            this.update(topic);
        }

        return topic;
    }

    public void deleteTopicById(TopicParam param) {
        Integer topicId = param.getId();
        Integer userId = userService.getUserId();

        if (userService.compareUserAndTopicId(userId, topicId)) {
            topicMapper.deleteById(topicId);
            cacheService.delCache(CacheKeyConstants.FORUM_TOPIC_KEY, String.valueOf(topicId));
        }
    }

    public Integer vote(Integer topicId) {

        String userId = String.valueOf(userService.getUserId());
        Topic topic = topicMapper.selectById(topicId);

        // 判断话题是否还存在
        Preconditions.checkNotNull(topic, "该话题可能已经被删除");

        // 用户不能给自己点赞
        if (Integer.valueOf(userId) == topic.getUserId()) {
            throw new ParamException("您不能给自己的帖子点赞！");
        }

        String upIds = topic.getUpIds();

        Set<String> idSet = StringUtils.commaDelimitedListToSet(upIds);

        // cancel vote
        if (idSet.contains(userId)) {
            idSet.remove(userId);
        } else { // vote
            idSet.add(userId);
        }

        topic.setUpIds(StringUtils.collectionToCommaDelimitedString(idSet));

        this.update(topic);

        return idSet.size();
    }

    public Topic addViewCount(Topic topic) {

        String ip = IpUtil.getIpAddr(RequestHolder.getCurrentRequest());
        ip = ip.replace(":", "_").replace(".", "_");

        String topicId = String.valueOf(topic.getId());

        String viewCountKey = cacheService.getFromCache(CacheKeyConstants.FORUM_TOPIC_VIEW_COUNT_KEY, ip, topicId);
        if (StringUtils.isEmpty(viewCountKey)) {
            topic.setViewCount(topic.getViewCount() + 1);
            this.update(topic);
            cacheService.saveCache(topicId, 3600, CacheKeyConstants.FORUM_TOPIC_VIEW_COUNT_KEY, ip, topicId);
        }

        return topic;
    }

    public void update(Topic topic) {
        topicMapper.update(topic);
        cacheService.saveCache(JsonUtil.objectToJson(topic), 3600, CacheKeyConstants.FORUM_TOPIC_KEY, String.valueOf(topic.getId()));
    }

}
