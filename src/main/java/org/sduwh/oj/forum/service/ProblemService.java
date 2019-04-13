package org.sduwh.oj.forum.service;

import org.sduwh.oj.forum.common.SortType;
import org.sduwh.oj.forum.exception.ParamException;
import org.sduwh.oj.forum.mapper.TopicMapper;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.util.SpringRestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("problemService")
public class ProblemService {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SpringRestTemplateUtil restTemplateUtil;

    @Resource
    private TopicMapper topicMapper;

    public List<TopicParam> getTopicsByProblemId(Integer problemId) {
        List<TopicParam> topicParamList = new ArrayList<>();

        List<Topic> topics = topicMapper.selectByProblemId(problemId);

        for (Topic topic : topics) {
            topicParamList.add(topicService.getTopicById(topic.getId()));
        }

        return topicParamList;
    }

    public List<TopicParam> sort(Integer problemId, Integer sortType) {

        List<TopicParam> topicParamList = new ArrayList<>();

        if (sortType.equals(SortType.CREATE_TIME_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByCreateTimeDESCWithProblemId(problemId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.CREATE_TIME_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByCreateTimeASCWithProblemId(problemId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.VOTES_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByVotesDESCWithProblemId(problemId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.VOTES_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByVotesASCWithProblemId(problemId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.POSTS_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByPostsDESCWithProblemId(problemId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.POSTS_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByPostsASCWithProblemId(problemId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        }
        else
            throw new ParamException("无效的参数");

        return topicParamList;
    }
}
