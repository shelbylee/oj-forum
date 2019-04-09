package org.sduwh.oj.forum.service;

import com.google.common.base.Preconditions;
import org.sduwh.oj.forum.common.CacheKeyConstants;
import org.sduwh.oj.forum.common.RequestHolder;
import org.sduwh.oj.forum.exception.ParamException;
import org.sduwh.oj.forum.mapper.TopicMapper;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.CommentParam;
import org.sduwh.oj.forum.param.OjContestParam;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.util.DateUtil;
import org.sduwh.oj.forum.util.IpUtil;
import org.sduwh.oj.forum.util.JsonUtil;
import org.sduwh.oj.forum.util.SpringRestTemplateUtil;
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
    @Autowired
    private SpringRestTemplateUtil restTemplateUtil;

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

    public TopicParam getTopicFromContest(Integer topicId, Integer contestId) {

        TopicParam topic;

        // 先判断用户身份
        Integer userId = userService.getUserId();
        OjContestParam.OjData.CreatorData creatorData = restTemplateUtil.getContestCreatorData(contestId);
        Integer creatorId = creatorData.getId();

        // 如果是contest创建者，则可以看到所有帖子，无论contest的discuss的状态
        if (userId.equals(creatorId)) {
            topic = getTopicById(topicId);
        } else { // 如果非contest的创建者，则只能看到自己发的帖子
            topic = getOwnTopic(userId, topicId);
        }

        return topic;
    }

    /**
     * 让用户只能获取自己所发的帖子
     * @return
     */
    private TopicParam getOwnTopic(Integer userId, Integer topicId) {

        TopicParam topicParam;

        Topic topic = getTopicByIdWithoutComment(topicId);
        if (topic.getUserId().equals(userId)) {
            List<CommentParam> comments = commentService.selectByTopicId(topicId);
            topic = addViewCount(topic);
            topicParam = JsonUtil.jsonToObject(JsonUtil.objectToJson(topic), TopicParam.class);
            Preconditions.checkNotNull(topicParam);
            if (!StringUtils.isEmpty(comments))
                topicParam.setComments(comments);
        } else {
            return null;
        }

        return topicParam;
    }

    public Topic saveTopic(TopicParam param) {
        Topic topic = new Topic();
        buildTopic(param, topic);
        topicMapper.insert(topic);
        return topic;
    }

    public Topic saveContestTopic(TopicParam param) {

        Topic topic = new Topic();

        // 1为允许
        if (param.getDiscussStatus() == 1) {
            // 正常创建topic
            buildTopic(param, topic);
            topicMapper.insert(topic);
        }
        // 0为禁止
        else if (param.getDiscussStatus() == 0) {
            // 判断用户身份
            String userType = userService.getUserType();
            // 如果用户是普通用户，则发帖仅createdById可见
            // user type:    Regular User, Admin, Super Admin
            // 方便测试，暂时先写成Super Admin
            if (userType.equals("Super Admin")) {
                OjContestParam.OjData.CreatorData creatorData = restTemplateUtil.getContestCreatorData(param.getContestId());
                Integer contestCreatorId = creatorData.getId();
                buildTopic(param, topic);
                topic.setContestCreatorId(contestCreatorId);
                topicMapper.insert(topic);
            }
        }

        return topic;
    }

    private void buildTopic(TopicParam from, Topic to) {
        to.setTitle(from.getTitle());
        to.setContent(from.getContent());
        to.setUserId(userService.getUserId());
        to.setCreatedAt(DateUtil.formatDateTime(new Date()));
        to.setCommentCount(0);
        to.setLikeCount(0);
        to.setViewCount(0);
        to.setProblemId(from.getProblemId());
        to.setContestId(from.getContestId());
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
