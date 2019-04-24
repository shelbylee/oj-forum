package org.sduwh.oj.forum.service;

import com.google.common.base.Preconditions;
import org.sduwh.oj.forum.common.CacheKeyConstants;
import org.sduwh.oj.forum.common.RequestHolder;
import org.sduwh.oj.forum.common.SortType;
import org.sduwh.oj.forum.exception.ParamException;
import org.sduwh.oj.forum.mapper.CommentMapper;
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
import java.util.*;

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
    @Resource
    private CommentMapper commentMapper;

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

    public List<TopicParam> getAllTopic() {
        List<TopicParam> topicParamList = new ArrayList<>();

        List<Topic> topics = topicMapper.selectAll();

        for (Topic topic : topics) {
            topicParamList.add(getTopicById(topic.getId()));
        }

        return topicParamList;
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

    /**
     * 让用户只能获取自己所发的帖子
     *
     * @return
     */
    public TopicParam getOwnTopic(Integer userId, Integer topicId) {

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

    public void buildTopic(TopicParam from, Topic to) {
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

        Preconditions.checkNotNull(param.getId(), "topic id不能为空！");

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

    public void deleteTopicById(Integer topicId) {
        Integer userId = userService.getUserId();
        if (userService.compareUserAndTopicId(userId, topicId) || userService.isAdmin()) {
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
        if (Integer.valueOf(userId).equals(topic.getUserId())) {
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
        topic.setLikeCount(idSet.size());

        this.update(topic);

        return idSet.size();
    }

    public Boolean ifUserHadVoted(Integer topicId) {
        String upIds = topicMapper.selectUpIds(topicId);
        Set<String> idSet = StringUtils.commaDelimitedListToSet(upIds);
        if (idSet.contains(userService.getUserId().toString()))
            return true;
        return false;
    }

    public Topic addViewCount(Topic topic) {

        String ip = IpUtil.getIpAddr(RequestHolder.getCurrentRequest());
        ip = ip.replace(":", "_").replace(".", "_");

        Preconditions.checkNotNull(topic, "该topic不存在！");
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

    public Integer getViewCount(Integer topicId) {
        Integer viewCount = topicMapper.getViewCount(topicId);
        return viewCount;
    }

    public Integer getPostCount(Integer topicId) {
        Integer postCount = commentMapper.getPostCount(topicId);
        return postCount;
    }

    public List<TopicParam> sort(Integer sortType) {

        List<TopicParam> topicParamList = new ArrayList<>();

        if (sortType.equals(SortType.CREATE_TIME_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByCreateTimeDESC();
            for (Topic topic : topics)
                topicParamList.add(getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.CREATE_TIME_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByCreateTimeASC();
            for (Topic topic : topics)
                topicParamList.add(getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.VOTES_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByVotesDESC();
            for (Topic topic : topics)
                topicParamList.add(getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.VOTES_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByVotesASC();
            for (Topic topic : topics)
                topicParamList.add(getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.POSTS_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByPostsDESC();
            for (Topic topic : topics)
                topicParamList.add(getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.POSTS_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByPostsASC();
            for (Topic topic : topics)
                topicParamList.add(getTopicById(topic.getId()));
        }
        else
            throw new ParamException("无效的参数");

        return topicParamList;
    }

}
