package org.sduwh.oj.forum.service;

import com.google.common.base.Preconditions;
import org.sduwh.oj.forum.common.SortType;
import org.sduwh.oj.forum.exception.ParamException;
import org.sduwh.oj.forum.mapper.ContestMapper;
import org.sduwh.oj.forum.mapper.TopicMapper;
import org.sduwh.oj.forum.model.Contest;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.ContestParam;
import org.sduwh.oj.forum.param.OjContestParam;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.util.SpringRestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("contestService")
public class ContestService {

    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private SpringRestTemplateUtil restTemplateUtil;

    @Resource
    private TopicMapper topicMapper;
    @Resource
    private ContestMapper contestMapper;

    public TopicParam getTopicFromContest(Integer contestId, Integer topicId) {

        TopicParam topic;

        Integer userId = userService.getUserId();

        // 如果是管理员，可以看到所有topic
        if (isContestCreator(contestId))
            topic = topicService.getTopicById(topicId);
        else
            topic = topicService.getOwnTopic(userId, topicId);

        return topic;
    }

    public List<TopicParam> getTopicsFromContestProblem(Integer contestId, Integer problemId) {

        List<TopicParam> topicParamList = new ArrayList<>();

        List<Topic> topics = topicMapper.selectByProblemContestId(contestId, problemId);

        if (isContestCreator(contestId) || userService.isAdmin()) {
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else {
            for (Topic topic : topics)
                if (getDiscussStatus(contestId).equals(0)) {
                    TopicParam topicParam = topicService.getOwnTopic(userService.getUserId(), topic.getId());
                    if (!StringUtils.isEmpty(topicParam))
                        topicParamList.add(topicParam);
                }
                else if (getDiscussStatus(contestId).equals(1))
                    topicParamList.add(topicService.getTopicById(topic.getId()));
        }

        return topicParamList;
    }

    public Integer getDiscussStatus(Integer contestId) {
        return contestMapper.selectDiscussStatusById(contestId);
    }

    public ContestParam updateDiscussStatus(Integer contestId, Integer discussStatus) {

        Preconditions.checkNotNull(contestId, "contest id不能为空！");
        Preconditions.checkNotNull(discussStatus, "contest discuss status不能为空！");

        ContestParam param = new ContestParam();
        param.setContestId(contestId);
        param.setDiscussStatus(discussStatus);

        ContestParam contestParam = contestMapper.select(param.getContestId());
        Preconditions.checkNotNull(contestParam, "该比赛可能已被删除！");

        contestMapper.update(param);
        return contestMapper.select(param.getContestId());
    }

    public Topic saveContestTopic(TopicParam param) {

        Topic topic = new Topic();

        Preconditions.checkNotNull(param.getContestId(), "必须绑定contest id！");
        Preconditions.checkNotNull(param.getProblemId(), "必须绑定problem id！");

        // 1为允许
        if (param.getDiscussStatus() == 1) {
            // 正常创建topic
            topicService.buildTopic(param, topic);
            topicMapper.insert(topic);
        }
        // 0为禁止
        else if (param.getDiscussStatus() == 0) {
            // 判断用户身份
            // user type:   Regular User, Admin, Super Admin
            String userType = userService.getUserType();
            // 如果用户是普通用户，则发帖仅createdById可见
            if (userType.equals("Regular User")) {
                OjContestParam.OjData.CreatorData creatorData = restTemplateUtil.getContestCreatorData(param.getContestId());
                Integer contestCreatorId = creatorData.getId();
                topicService.buildTopic(param, topic);
                topic.setContestCreatorId(contestCreatorId);
                topicMapper.insert(topic);
            } else if (userService.isAdmin()) {
                // 如果是管理员，则正常创建topic，topic对所有人可见
                topicService.buildTopic(param, topic);
                topicMapper.insert(topic);
            }
        }

        return topic;
    }

    public Contest saveDiscussStatus(Integer contestId, Integer discussStatus) {

        Preconditions.checkNotNull(contestId, "contest id不能为空！");
        Preconditions.checkNotNull(discussStatus, "contest discuss status不能为空！");

        Preconditions.checkNotNull(contestId, "contest id不能为空！");
        Preconditions.checkNotNull(discussStatus, "contest discuss status不能为空！");

        Contest contest = new Contest();
        contest.setContestId(contestId);
        contest.setDiscussStatus(discussStatus);

        contestMapper.insert(contest);

        return contest;
    }

    private Boolean isContestCreator(Integer contestId) {
        // 先判断用户身份
        Integer userId = userService.getUserId();
        OjContestParam.OjData.CreatorData creatorData = restTemplateUtil.getContestCreatorData(contestId);
        Integer creatorId = creatorData.getId();

        // 如果是contest创建者，则可以看到所有帖子，无论contest的discuss的状态
        if (userId.equals(creatorId))
            return true;
        else  // 如果非contest的创建者，则只能看到自己发的帖子
            return false;

    }

    /**
     * 排序
     *
     * @param sortType 0：最新发表 1: 最旧发表
     *                 2：投票最多 3: 投票最少
     *                 4：评论最多 5: 评论最少
     */
    public List<TopicParam> sort(Integer contestId, Integer problemId, Integer sortType) {

        List<TopicParam> topicParamList = new ArrayList<>();

        if (sortType.equals(SortType.CREATE_TIME_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByCreateTimeDESCWithContestAndProblemId(problemId, contestId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.CREATE_TIME_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByCreateTimeASCWithContestAndProblemId(problemId, contestId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.VOTES_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByVotesDESCWithContestAndProblemId(problemId, contestId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.VOTES_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByVotesASCWithContestAndProblemId(problemId, contestId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.POSTS_DESC.getIdx())) {
            List<Topic> topics = topicMapper.sortByPostsDESCWithContestAndProblemId(problemId, contestId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        } else if (sortType.equals(SortType.POSTS_ASC.getIdx())) {
            List<Topic> topics = topicMapper.sortByPostsASCWithContestAndProblemId(problemId, contestId);
            for (Topic topic : topics)
                topicParamList.add(topicService.getTopicById(topic.getId()));
        }
        else
            throw new ParamException("无效的参数");

        return topicParamList;
    }
}
