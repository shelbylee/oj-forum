package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.service.ContestService;
import org.sduwh.oj.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/contest/topic")
public class ContestController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private ContestService contestService;

    // 查询contest中某个话题
    @GetMapping()
    public ResultMessage select(@RequestParam Integer contestId, @RequestParam Integer topicId) {
        TopicParam topic = contestService.getTopicFromContest(contestId, topicId);
        if (StringUtils.isEmpty(topic))
            return ResultMessage.success(null, "Permission denied");
        return ResultMessage.success(topic);
    }

    // 查询contest中某个problem下的所有话题
    @GetMapping("/problem")
    public ResultMessage selectFromContestProblem(@RequestParam Integer contestId, @RequestParam Integer problemId) {
        List<TopicParam> topics = contestService.getTopicsFromContestProblem(contestId, problemId);
        if (StringUtils.isEmpty(topics))
            return ResultMessage.success(null, "Permission denied");
        return ResultMessage.success(topics);
    }

    // 创建话题
    @PostMapping("/create")
    public ResultMessage create(TopicParam param) {
        Topic topic = contestService.saveContestTopic(param);
        return ResultMessage.success(topic);
    }
}
