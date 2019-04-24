package org.sduwh.oj.forum.controller;

import lombok.extern.slf4j.Slf4j;
import org.sduwh.oj.forum.common.ResultMessage;
import org.sduwh.oj.forum.model.Contest;
import org.sduwh.oj.forum.model.Topic;
import org.sduwh.oj.forum.param.ContestParam;
import org.sduwh.oj.forum.param.TopicParam;
import org.sduwh.oj.forum.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/contests")
public class ContestController {

    @Autowired
    private ContestService contestService;

    // 查询contest中某个话题
    @Deprecated
    @GetMapping("/{contestId}/topic/{topicId}")
    public ResultMessage select(@PathVariable("contestId") Integer contestId, @PathVariable("topicId") Integer topicId) {
        TopicParam topic = contestService.getTopicFromContest(contestId, topicId);
        if (StringUtils.isEmpty(topic))
            return ResultMessage.success(null, "Permission denied");
        return ResultMessage.success(topic);
    }

    // 查询contest中某个problem下的所有话题
    @GetMapping("/{contestId}/problem/{problemId}/topics")
    public ResultMessage selectFromContestProblem(@PathVariable("contestId") Integer contestId, @PathVariable("problemId") Integer problemId) {
        List<TopicParam> topics = contestService.getTopicsFromContestProblem(contestId, problemId);
        if (StringUtils.isEmpty(topics))
            return ResultMessage.success(null, "Permission denied");
        return ResultMessage.success(topics);
    }

    // 创建话题
    @PostMapping("/problem/topic")
    public ResultMessage create(@RequestBody TopicParam param) {
        Topic topic = contestService.saveContestTopic(param);
        return ResultMessage.success(topic);
    }

    // 排序话题
    @GetMapping("/{contestId}/problem/{problemId}/sort/{sortType}")
    public ResultMessage sort(@PathVariable("contestId") Integer contestId, @PathVariable("problemId") Integer problemId, @PathVariable("sortType") Integer sortType) {
        List<TopicParam> topics = contestService.sort(contestId, problemId, sortType);
        return ResultMessage.success(topics);
    }

    // 设置讨论区状态
    @PostMapping("/{contestId}/discuss-status")
    public ResultMessage saveDiscussStatus(@PathVariable("contestId") Integer contestId, @RequestParam Integer discussStatus) {
        Contest contest = contestService.saveDiscussStatus(contestId, discussStatus);
        return ResultMessage.success(contest);
    }

    // 查询讨论区状态
    @GetMapping("/{contestId}/discuss-status")
    public ResultMessage getDiscussStatus(@PathVariable("contestId") Integer contestId) {
        Integer discussStatus = contestService.getDiscussStatus(contestId);
        if (StringUtils.isEmpty(discussStatus))
            return ResultMessage.fail(400, "The discuss status of this contest hasn't been set yet");
        return ResultMessage.success(discussStatus);
    }

    // 修改讨论区状态
    @PutMapping("/{contestId}/discuss-status")
    public ResultMessage updateDiscussStatus(@PathVariable("contestId") Integer contestId, @RequestParam Integer discussStatus) {
        ContestParam contestParam = contestService.updateDiscussStatus(contestId, discussStatus);
        return ResultMessage.success(contestParam);
    }

}
