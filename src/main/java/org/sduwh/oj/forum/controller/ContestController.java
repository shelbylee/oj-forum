package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/contest/topic")
public class ContestController {

    @Autowired
    private TopicService topicService;

    // 查询话题
    @GetMapping()
    public ResultMessage select(@RequestParam Integer topicId, @RequestParam Integer contestId) {
        TopicParam topic = topicService.getTopicFromContest(topicId, contestId);
        if (StringUtils.isEmpty(topic))
            return ResultMessage.success(null, "Permission denied");
        return ResultMessage.success(topic);
    }

    // 创建话题
    @PostMapping("/create")
    public ResultMessage create(TopicParam param) {
        Topic topic = topicService.saveContestTopic(param);
        return ResultMessage.success(topic);
    }
}
