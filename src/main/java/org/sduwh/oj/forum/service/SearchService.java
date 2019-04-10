package org.sduwh.oj.forum.service;

import com.google.common.base.Preconditions;
import org.sduwh.oj.forum.mapper.TopicMapper;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.SearchParam;
import org.sduwh.oj.forum.param.TopicParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("searchService")
public class SearchService {

    @Autowired
    private TopicService topicService;

    @Resource
    private TopicMapper topicMapper;

    public List<TopicParam> search(SearchParam searchParam) {

        List<TopicParam> topicParamList = new ArrayList<>();

        List<Topic> topics;

        String keywords = "%" + searchParam.getKeywords() + "%";
        Integer contestId = searchParam.getContestId();
        Integer problemId = searchParam.getProblemId();

        Preconditions.checkNotNull(keywords, "搜索内容不能为空");

        // contest id没传，说明是在problem或者社区下面搜索的
        if (StringUtils.isEmpty(contestId)) {
            // problem id没传，说明是在社区下面搜索的
            if (StringUtils.isEmpty(problemId)) {
                topics = topicMapper.searchByKeywords(keywords);
                for (Topic topic : topics) {
                    topicParamList.add(topicService.getTopicById(topic.getId()));
                }
            } else { // 传了problem id，没传contest id，说明是再problem下面搜索的
                topics = topicMapper.searchByKeywordsWithProblemId(keywords, problemId);
                for (Topic topic : topics) {
                    topicParamList.add(topicService.getTopicById(topic.getId()));
                }
            }
        } else {
            // 传了contest id和problem id，说明是在contest下的problem中搜索的
            topics = topicMapper.searchByKeywordsWithProblemAndContestId(keywords, problemId, contestId);
            for (Topic topic : topics) {
                topicParamList.add(topicService.getTopicById(topic.getId()));
            }
        }

        return topicParamList;
    }
}
